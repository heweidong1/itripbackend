package cn.kgc.itrip.auth.service;

import cn.kgc.itrip.beans.common.ServerResponse;
import cn.kgc.itrip.beans.pojo.ItripUser;
import cn.kgc.itrip.beans.vo.ItripTokenVo;

public interface TokenService
{
    /**
     *  生成token
     */
    public String generatorToken(String userAgent, ItripUser user);

    public void save(String key,String value,Integer time)throws Exception;

    public ServerResponse delToken(String key);

    /**
     * token置换
     * @param userAgent 用户标识
     * @param token 就token
     * @return 客户端新token
     * @throws Exception a
     */
    ServerResponse<ItripTokenVo> replaceToken(String userAgent,String token)throws Exception;
}
