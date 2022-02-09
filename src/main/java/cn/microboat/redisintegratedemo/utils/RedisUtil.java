package cn.microboat.redisintegratedemo.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhouwei
 */
@Component
public final class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //========================common==================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 key 获取过期时间
     * @param key 键，不能为 null
     * @return Long 时间（秒），返回 0 代表永久有效
     * */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     * @param key 键
     * @return boolean 存在返回true，不存在返回false
     * */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 一个或多个键
     * */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.stream(key).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return Object 对象
     * */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return 成功true 失败false
     * */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 离过期时间
     * @return boolean 成功true 失败false
     * */
    public boolean set(String key, Object value,long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递归因子，必须大于0
     * @return Long 递归后的值
     * */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递归因子必须大于0！");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 递归因子，必须大于0
     * @return Long 递归后的值
     * */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0！");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //========================hash==================

    /**
     * HashGet
     * @param key 键 不能为 null
     * @param item 项 不能为 null
     * */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的键值
     * */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return boolean 成功true 失败false
     * */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 键值对
     * @param time 离过期的时间
     * @return boolean 成功true 失败false
     * */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return boolean 成功true 失败false
     * */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建，并设置过期时间
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 离过期时间，如果已存在的hash表有时间，这里将会替换原有的时间
     * @return boolean 成功true 失败false
     * */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键
     * @param item 项 可以为多个，不能为 null
     * */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键，不能为 null
     * @param item 项，不能为 null
     * @return boolean 成功true 失败false
     * */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增，如果不存在，就会创建一个，并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 递归因子，要大于0
     * @return double 递增后的值
     * */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 递减因子，要小于0
     * @return double 递归后的值
     * */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //========================set==================

    /**
     * 根据键获取 Set中所有的值
     * @param key 键
     * @return Set 集合
     * */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个Set中查询其是否存在
     * @param key 键
     * @param value 值
     * @return Boolean 存在true 不存在false
     * */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入Set缓存
     * @param key 键
     * @param values 值，可以是多个
     * @return Long 成功放入缓存的个数
     * */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将 Set数据放入缓存
     * @param key 键
     * @param values 值，可以为多个
     * @param time 离过期的时间
     * @return Long 成功放入缓存的个数
     * */
    public Long sSet(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取Set缓存的长度
     * @param key 键
     * @return long 长度
     * */
    public Long slen(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除Set中值的value的键值对
     * @param key 键
     * @param values 值
     * @return 成功移除的个数
     * */
    public Long srem(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    //========================list==================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始下标，一般从 0 开始
     * @param end 结束下标，-1 表示倒数第一， -2表示倒数第二
     * @return List 缓存的全部内容
     * */
    public List<Object> lget(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list列表的长度
     * @param key 键
     * @return Long 列表的长度
     * */
    public Long llen(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 通过index索引获取list中对应的值
     * @param key 键
     * @param index 索引，0 第一个，1 第二个，-1 倒数第一个，-2 倒数第二个
     * @return Object 对应的值
     * */
    public Object lindex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 list 放入缓存
     * @param key 键
     * @param value 值
     * @return boolean 成功true，失败false
     * */
    public boolean lset(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 离过期的时间
     * @return boolean 成功true，失败false
     * */
    public boolean lset(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param values 值
     * @return boolean 成功true， 失败false
     * */
    public boolean lset(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     * @param key 键
     * @param values 值
     * @param time 离过期的时间，单位默认为秒
     * @return boolean 成功true，失败false
     * */
    public boolean lset(String key, List<Object> values, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改key对应值的index索引对应值为value
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return boolean 成功true，失败false
     * */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除 count 个为 value 的值
     * @param key 键
     * @param count 个数
     * @param value 值
     * @return Long 移除成功的个数
     * */
    public Long lrem(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


}
