package cn.kgc.itrip.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 工具类
 */
@Component
public class RedisApi
{
    @Autowired
    private JedisPool jedisPool;

    /**
     *    向redis中存值
     */

    public void ser(String name,String value)
    {
        //获取核心对象
        Jedis jedis = jedisPool.getResource();
        jedis.set(name,value);
        //归还资源
        jedisPool.returnBrokenResource(jedis);
    }

    /**
     * 向redis中存值 并设置过期时间
     * @param name 建
     * @param value 值
     * @param time 过期时间
     */
    public void setEx(String name,String value,Integer time)
    {
        Jedis resource = jedisPool.getResource();
        //时间已秒为单位
        resource.setex(name,time,value);
        //归还资源
        jedisPool.returnBrokenResource(resource);
    }

    public String get(String key)
    {
        Jedis resource = jedisPool.getResource();
        String value = resource.get(key);
        //归还资源
        jedisPool.returnBrokenResource(resource);
        return value;
    }

    /**
     * 根据key来删除
     * @param key 建
     * @return 0 失败 或不存在 大于0标识成功
     */
    public Long del(String... key)
    {
        Jedis resource = jedisPool.getResource();
        Long del = resource.del(key);
        //归还资源
        jedisPool.returnBrokenResource(resource);
        return del;
    }

    public long ttl(String key)
    {
        Jedis resource = jedisPool.getResource();
        //返回当前key 的剩余秒数
        Long ttl = resource.ttl(key);
        //归还资源
        jedisPool.returnBrokenResource(resource);
        return ttl;
    }

    public boolean exists(String key)
    {
        Jedis resource = jedisPool.getResource();
        //判断key是否存在
        Boolean exists = resource.exists(key);
        //归还资源
        jedisPool.returnBrokenResource(resource);
        return exists;
    }
}
