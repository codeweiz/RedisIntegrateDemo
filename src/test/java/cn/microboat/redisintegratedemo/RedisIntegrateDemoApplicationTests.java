package cn.microboat.redisintegratedemo;

import cn.microboat.redisintegratedemo.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class RedisIntegrateDemoApplicationTests {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        System.out.println(redisUtil.set("k1", "v1"));
        System.out.println(redisUtil.set("k2", "v2"));
        System.out.println(redisUtil.set("k3", "v3"));
        System.out.println(redisUtil.get("k1"));
        System.out.println(redisUtil.get("k2"));
        System.out.println(redisUtil.get("k3"));
        redisUtil.del("k1", "k2", "k3");
        System.out.println(redisUtil.get("k1"));
        System.out.println(redisUtil.get("k2"));
        System.out.println(redisUtil.get("k3"));
    }

}
