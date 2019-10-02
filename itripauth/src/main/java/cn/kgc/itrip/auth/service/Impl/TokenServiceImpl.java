package cn.kgc.itrip.auth.service.Impl;

import cn.kgc.itrip.auth.service.TokenService;
import cn.kgc.itrip.beans.common.Const;
import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripUser;
import cn.kgc.itrip.beans.vo.ItripTokenVo;
import cn.kgc.itrip.beans.vo.ItripUserVO;
import cn.kgc.itrip.utils.MD5Util;
import cn.kgc.itrip.utils.RedisApi;
import cn.kgc.itrip.utils.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * token的业务逻辑
 */
@Slf4j
@Service("tokenService")
public class TokenServiceImpl implements TokenService
{
    @Autowired
    private RedisApi redisApi;

    /**
     *   生成token
     */
    @Override
    public String generatorToken(String userAgent, ItripUser user)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("token:");
        //判断终端
        if(UserAgentUtil.CheckAgent(userAgent))
        {
            stringBuffer.append("MOBILE-");
        }else
        {
            stringBuffer.append("PC-");
        }
        stringBuffer.append(StringUtils.substring(MD5Util.MD5EncodeUtf8(user.getUserCode()),0,32));
        stringBuffer.append("-");
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
        stringBuffer.append(time.format(new Date()));
        stringBuffer.append("-");
        stringBuffer.append(StringUtils.substring(MD5Util.MD5EncodeUtf8(userAgent),0,6));
        return stringBuffer.toString();
    }

    @Override
    public void save(String key, String value, Integer time) throws Exception {
        //将给redis来保存数据
        redisApi.setEx(key,value,time);
    }


    /**
     * 删除redis中的token
     * @param key
     * @return
     */
    @Override
    public ServerResponse delToken(String key)
    {
        log.info(key);
        redisApi.del(key);
        return  ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<ItripTokenVo> replaceToken(String userAgent, String token) throws Exception {
        if(StringUtils.isBlank(userAgent)||StringUtils.isBlank(token))
        {
            log.info("在token置换的时候参数丢失");
            return ServerResponse.createByErrorMessage("参数丢失");
        }
        //获取token的剩余时间
        long ttl = redisApi.ttl(token);
        if(ttl<-1)
        {
            log.info("当前token无效");
            return ServerResponse.createByErrorMessage("当前token失效");
        }
        //判断token是否在保护期
        if(ttl> Const.RedisConst.REPLACEMENT_PROTECTION_TIMEOUT)
        {
            log.info("当前token处于置换保护器内");
            return ServerResponse.createByErrorMessage("token 无法置换");
        }

        //将旧token重新设置过期时间
        redisApi.setEx(token,redisApi.get(token),Const.RedisConst.REPLACEMENT_DELAY);
        //生成新token
        String newToken = generatorToken(userAgent, JSON.parseObject(redisApi.get(token),
                ItripUser.class));
        ItripTokenVo itripTokenVo = new ItripTokenVo();
        itripTokenVo.setToken(newToken);
        itripTokenVo.setExpTime(System.currentTimeMillis()+Const.RedisConst.SESSION_TIMEOUT*1000L);
        itripTokenVo.setGentTime(System.currentTimeMillis());
        redisApi.setEx(newToken,redisApi.get(token),Const.RedisConst.SESSION_TIMEOUT);
        return  ServerResponse.createBySuccess(itripTokenVo);

    }
}
