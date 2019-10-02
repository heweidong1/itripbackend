package cn.kgc.itrip.auth.service.Impl;

import cn.kgc.itrip.auth.service.IUserService;
import cn.kgc.itrip.auth.service.TokenService;
import cn.kgc.itrip.beans.common.Const;
import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripUser;
import cn.kgc.itrip.beans.vo.ItripTokenVo;
import cn.kgc.itrip.beans.vo.ItripUserVO;
import cn.kgc.itrip.mapper.itripUser.ItripUserMapper;
import cn.kgc.itrip.utils.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service("iUserService")
@Slf4j
public class UserServiceImpl implements IUserService
{
    @Resource
    private ItripUserMapper itripUserMapper;

    @Resource
    private TokenService tokenService;

    @Autowired
    private SMSSendUtil smsSendUtil;

    @Autowired
    private RedisApi redisApi;

    @Autowired
    private SendMailText sendMailText;

    /**
     * 用户登录
     * @param userCode 用户名
     * @param password 密码
     * @param userAgent 设备标识
     * @return 成功是否 vo
     */
    @Override
    public ServerResponse userLogin(String userCode, String password, String userAgent) throws Exception {
        log.info("用户登录 Service");
        if(StringUtils.isBlank(userCode)||StringUtils.isBlank(password))
        {
            log.info("在用户登录时发生参数错误..");
            return ServerResponse.createByErrorMessage("参数丢失");
        }
        //校验用户名
        Integer integer = itripUserMapper.queryUserByUserCode(userCode);
        if(integer<=0)
        {
            log.info("用户名不存在");
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //加密密码
        String md5password = MD5Util.MD5EncodeUtf8(password);
        ItripUser itripUser = itripUserMapper.queryUserBuUserCodeAndPassword(userCode, md5password);
        if(itripUser==null)
        {
            log.info("密码不正确");
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        log.info(itripUser.getUserName()+"登录成功");
        //密码滞空
        itripUser.setUserPassword(StringUtils.EMPTY);
        log.info(itripUser.getUserName()+"密码滞空完成");
        //TODO 登录成功 等待token生成 PC 还是 手机
        String token = tokenService.generatorToken(userAgent, itripUser);
        log.info(itripUser.getUserName()+"token生成完成"+token);
        //TODO 保存token
        //判断用户是否是手机
        if(UserAgentUtil.CheckAgent(userAgent))
        {
            tokenService.save(token, JSONObject.toJSONString(itripUser),-1);
        }else
        {
            tokenService.save(token, JSONObject.toJSONString(itripUser), Const.RedisConst.SESSION_TIMEOUT);
        }
        log.info("token保存完成");
        //组装vo
        ItripTokenVo itripTokenVo = new ItripTokenVo();
        //token的生成时间
        itripTokenVo.setGentTime(System.currentTimeMillis());
        //token的过期时间
        itripTokenVo.setExpTime(System.currentTimeMillis()+ Const.RedisConst.SESSION_TIMEOUT*1000);
        itripTokenVo.setToken(token);
        return ServerResponse.createBySuccess(itripTokenVo);
    }


    /**
     * 发送短信
     */
    @Override
    public ServerResponse registerbyphone(ItripUserVO itripUserVO)throws Exception
    {
        if(!validatePhone(itripUserVO.getUserCode()))
        {
            return ServerResponse.createByErrorMessage("手机格式不正确");
        }
        //判断手机号是否注册
        boolean b = isuserCode(itripUserVO.getUserCode());
        if(!b)
        {
            return ServerResponse.createByErrorMessage("该账户已经被注册");
        }

        //生成随机数
        Integer randomCode = MD5.getRandomCode();
        //将生成的随机数保存到redis中
        redisApi.setEx(itripUserVO.getUserCode(),randomCode.toString(),60);
        //发送短信
        String[]str={randomCode.toString(),"5"};
        smsSendUtil.sendSms(itripUserVO.getUserCode(),"1",str);


        //加密密码
        String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(itripUserVO.getUserPassword());
        //添加用户，并设置未注册
        return  addUserActicated0(itripUserVO,md5EncodeUtf8);
    }


    /**
     * 激活操作
     * @param
     * @return
     */
    @Override
    public ServerResponse validatephone(String user, String code)throws Exception
    {
        //判断手机号是否注册
       if(redisApi.exists(user))
       {
           //redis存在
           //判断验证码是否正确
           String authentication = redisApi.get(user);
           if(!authentication.equals(code))
           {
               return ServerResponse.createByErrorMessage("验证码不正确");
           }
           ItripUser itripUser =new ItripUser();
           itripUser.setUserCode(user);
           itripUser.setActivated(1);
           //修改账号为激活状态
           Integer integer = itripUserMapper.updateItripUser(itripUser);
           if(integer<0)
           {
               return ServerResponse.createByErrorMessage("发生未知错误");
           }
           redisApi.del(user);
           return ServerResponse.createBySuccessMessage("激活成功");
       }
       return ServerResponse.createByErrorMessage("账号不正确或验证码失效");
    }

    /**
     *   验证手机格式
     */
    public boolean validatePhone(String phone) {
        String regex = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        return Pattern.compile(regex).matcher(phone).find();
    }


    /**
     *   判断用户是否存在
     *   true可以注册
     *   false 不可以
     */
    @Override
    public boolean isuserCode(String usercode) throws Exception {
        Integer count = itripUserMapper.queryUserByUserCode(usercode);
        if(count>0)
        {
            //TODO 校验用户名是否激活
            ItripUser user = itripUserMapper.queryUserIsExistsByUserCode(usercode);
            if(user.getActivated()==1)
            {
                return false;
            }
            if(user.getActivated()==0)
            {
                //查询redis
                String s = redisApi.get(usercode);
                if(s==null)
                {
                    //删除此记录
                    itripUserMapper.deleUserByCode(usercode);
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    /**
     *   邮箱发送
     */
    @Override
    public ServerResponse doregister(ItripUserVO itripUserVO)throws Exception
    {
        //生成随机数
        int randomCode = MD5.getRandomCode();

        //将生成的随机数保存到redis中
        redisApi.setEx(itripUserVO.getUserCode(),randomCode+"",60);
        //发送邮件
        sendMailText.main(itripUserVO.getUserCode(),randomCode+"");
        //加密密码
        String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(itripUserVO.getUserPassword());
        //添加用户，并设置未注册
        return  addUserActicated0(itripUserVO,md5EncodeUtf8);
    }

    /**
     * 向数据库添加注册未激活的用户
     */
    public ServerResponse addUserActicated0(ItripUserVO itripUserVO,String password) throws Exception {
        //添加用户，并设置未注册
        ItripUser user= new ItripUser();
        user.setUserCode(itripUserVO.getUserCode());
        user.setUserName(itripUserVO.getUserName());
        user.setUserPassword(password);
        user.setActivated(0);

        Integer integer = itripUserMapper.insertItripUser(user);
        if(integer<0)
        {
            return ServerResponse.createByErrorMessage("未知错误");
        }
        return ServerResponse.createBySuccess();
    }
}
