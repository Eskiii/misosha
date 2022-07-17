package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname RedisService
 * @Description Redis 数据库的 存和取 方法
 * @Date 2022/4/26 21:26
 * @Created by Eskii
 */
@Service
@Slf4j
public class RedisService {

    private final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    JedisPool jedisPool;

    /**
     * 根据 keyPrefix + key 拼接成的键，获取对应的 value
     * @param keyPrefix 前缀，用于区分不同请求来源
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(keyPrefix.getPrefix() + key);
            logger.info(keyPrefix.getPrefix() + key + " value = " + value);
            T t = Serialization.stringToObj(value, clazz);
            logger.info(keyPrefix.getPrefix() + key + " get " + t);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 根据 key 获取 value
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        log.info(jedisPool.toString());
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            T t = Serialization.stringToObj(value, clazz);
            logger.info(key + " get " + t);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 向 redis 数据库中加入一个键值对，注意 keyPrefix 是可能包含 expireTime 的，所以要进行判断
     * 注意，如果数据库中已经包含了一个 key 相同的value，那么这个方式的插入会覆盖之前的value
     * 所以，这种set方式只要jedis 是连接上数据库的，都会返回true
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueStr = Serialization.objToString(value);
            if(valueStr == null) return false;
            int expTime = keyPrefix.getExpireSeconds();
            String realKey = keyPrefix.getPrefix() + key;
            if(expTime <= 0) {
                jedis.set(realKey, valueStr);
            } else {
                jedis.setex(realKey, expTime, valueStr);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 插入 键值对
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueStr = Serialization.objToString(value);
            if(valueStr == null) return false;
            jedis.set(key, valueStr);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 以 NX 和 EX 的方式设置键值
     * 如果数据库中已经存在了相同 key 的数据，则插入失败
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean setNXEX(KeyPrefix keyPrefix, String key, T value) {
        int expTime = keyPrefix.getExpireSeconds();
        if(expTime <= 0) {  // 必须设置一个 expTime
            throw new RuntimeException("[SET EX NX]必须设置超时时间！");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueStr = Serialization.objToString(value);
            SetParams setParams = new SetParams();  // 构造插入参数
            setParams.ex(expTime);
            setParams.nx();
            String res = jedis.set(keyPrefix.getPrefix() + key, valueStr, setParams);
            return "OK".equals(res);    //redis 返回 OK 状态码则表示插入成功
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 根据 key 判断数据库中是否存在相应数据
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(keyPrefix.getPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 根据 key 删除键值对
     * @param keyPrefix
     * @param key
     * @return 是否成功删除
     */
    public boolean delete(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long res = jedis.del(keyPrefix.getPrefix() + key);  // jedis.del()返回被成功删除的 key 的数量
            return res > 0; //删除的key个数 > 0 则表示成功删除
        } finally {
            returnToPool(jedis);
        }
    }

    /**根据 key 对 value 增一
     *注意： If the key does not exist or contains a value of a wrong type,
     *      set the key to the value of "0" before to perform the increment operation.
     * @param keyPrefix
     * @param key
     * @return incr 之后的 value
     */
    public Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(keyPrefix.getPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 根据 key 对 value 减一
     * 注意：  If the key does not exist or contains a value of a wrong type,
     *        set the key to the value of "0" before to perform the decrement operation.
     *        如果 value 不是数值类型的，那么会将value置为 0，再减一
     * @param keyPrefix
     * @param key
     * @return decr 之后的value
     */
    public Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(keyPrefix.getPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 批量删除 key 中包含了 prefix 的 key
     * @param prefix
     * @return
     */
    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        //获取包含 prefix 的 key 数组
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys == null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArray = keys.toArray(new String[0]); // dump the list into a newly allocated array of String
            jedis.del(keysArray);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 扫描数据库并获取包含关键字 prefix 的 key数组
     * 注：使用了redis 的 scan cursor match pattern count 方法
     * @param prefix
     * @return 包含关键字 key 的 键数组
     */
    public List<String> scanKeys(String prefix) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            String cursor = "0";   // 扫描的游标
            ScanParams sp = new ScanParams();
            sp.match("*"+prefix+"*");  // 匹配表达式
            sp.count(100);  // 获取扫描集中的 100 个元素
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp); // 获取本轮扫描到的 value
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getCursor();
//                        .getStringCursor();
            }while(!cursor.equals("0"));   //游标重新置 0 表示扫描结束
            return keys;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * “关闭”一条 jedis 连接，如果没有损坏则将其返回 jedis 连接池中
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }


}
