package cn.kgc.itrip.biz.service.Impl;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripUser;
import cn.kgc.itrip.beans.pojo.ItripUserLinkUser;
import cn.kgc.itrip.beans.vo.ItripAddUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripModifyUserLinkUserVO;
import cn.kgc.itrip.beans.vo.ItripSearchUserLinkUserVO;
import cn.kgc.itrip.biz.service.LinkUserService;
import cn.kgc.itrip.mapper.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.kgc.itrip.utils.RedisApi;
import com.alibaba.fastjson.JSON;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkUserServiceImpl implements LinkUserService
{
    @Autowired
    private ItripUserLinkUserMapper itripUserLinkUserMapper;

    @Autowired
    private RedisApi redisApi;

    /**
     * 新增常用联系人
     */
    @Override
    public ServerResponse addLinkUser(ItripAddUserLinkUserVO linkUser, String token)throws Exception
    {
        if(!redisApi.exists(token))
        {
            return ServerResponse.createByErrorMessage("token失效");
        }
        if(linkUser==null)
        {
            return ServerResponse.createByErrorMessage("不能提交空，请填写常用联系人信息");
        }
        ItripUserLinkUser User = new ItripUserLinkUser();
        BeanUtils.copyProperties(linkUser,User);
        ItripUser itripUser = JSON.parseObject(redisApi.get(token), ItripUser.class);
        Long userId = itripUser.getId();
        User.setUserId(userId.intValue());
        User.setCreatedBy(itripUser.getId());
        Integer integer = itripUserLinkUserMapper.insertItripUserLinkUser(User);
        if(integer>0)
        {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("新增常用联系人失败");
    }

    /**
     * 删除联系人
     */
    @Override
    public ServerResponse linkDel(Long[]ids, String token)throws Exception
    {
        try {
            if(!redisApi.exists(token))
            {
                return ServerResponse.createByErrorMessage("token失效");
            }
            if(ids.length==0)
            {
                return ServerResponse.createByErrorMessage("不能提交空，请填写常用联系人id");
            }
            //判断是否能删除
            boolean falg = true;
            for(int i=0;i<ids.length;i++)
            {
                Integer queryisdel = itripUserLinkUserMapper.queryisdel(ids[i]);
                if(queryisdel>0)
                {
                    falg = false;
                    break;
                }
            }
            if(!falg)
            {
                return ServerResponse.createByErrorMessage("所选的常用联系人中有与某条待支付的订单关联的项，无法删除");
            }

            //删除联系人
            for(int i=0;i<ids.length;i++)
            {
                Integer integer = itripUserLinkUserMapper.deleteItripUserLinkUserById(ids[i]);
                if(integer<=0)
                {
                    throw new RuntimeException("删除失败");
                }
            }
            return ServerResponse.createBySuccess();
        }catch (Exception e)
        {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("删除失败");
        }

    }

    /**
     * 查询常用人
     */
    @Override
    public ServerResponse queryLinkUser(ItripSearchUserLinkUserVO itripSearchUserLinkUserVO, String token ) throws Exception {
        try {
            if(!redisApi.exists(token))
            {
                return ServerResponse.createByErrorMessage("token失效");
            }
            ItripUser itripUser = JSON.parseObject(redisApi.get(token), ItripUser.class);
            Long userId = itripUser.getId();
            String linkUserName=null;
            if(!StringUtils.isEmpty(itripSearchUserLinkUserVO.getLinkUserName()))
            {
                linkUserName = "%"+itripSearchUserLinkUserVO.getLinkUserName()+"%";
            }
            Map<String,Object> param = new HashMap<>();
            param.put("userId",userId);
            param.put("linkUserName",linkUserName);
            List<ItripUserLinkUser> itripUserLinkUserListByMap = itripUserLinkUserMapper.getItripUserLinkUserListByMap(param);

            return ServerResponse.createBySuccess(itripUserLinkUserListByMap);
        }catch (Exception e)
        {
            return ServerResponse.createByErrorMessage("获取常用联系人信息失败");
        }
    }

    /**
     * 修改常用人
     */
    @Override
    public ServerResponse updateLinkUser(ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, String token)throws Exception
    {
        try {
            if (!redisApi.exists(token)) {
                return ServerResponse.createByErrorMessage("token失效");
            }
            if(itripModifyUserLinkUserVO.getId()==null)
            {
                return ServerResponse.createByErrorMessage("不能提交空，请填写常用联系人信息");
            }
            ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
            BeanUtils.copyProperties(itripModifyUserLinkUserVO,itripUserLinkUser);

            ItripUser itripUser = JSON.parseObject(redisApi.get(token), ItripUser.class);
            Long userId = itripUser.getId();

            itripUserLinkUser.setUserId(userId.intValue());
            itripUserLinkUser.setModifyDate(new Date());
            Integer integer = itripUserLinkUserMapper.updateItripUserLinkUser(itripUserLinkUser);
            if(integer<=0)
            {
                return ServerResponse.createByErrorMessage("修改常用联系人失败");
            }

            return ServerResponse.createBySuccess();

        }catch (Exception e)
        {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("系统异常");

        }
    }
}
