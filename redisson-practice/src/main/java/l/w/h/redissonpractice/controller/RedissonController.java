package l.w.h.redissonpractice.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lwh
 * @since 2022/2/18 12:01
 **/
@RestController
public class RedissonController {

    @Resource
    private RedissonClient redissonClient;

    @GetMapping("/test")
    public String test(){
        RLock lock = redissonClient.getLock("test");
        lock.lock();
        System.out.println("test");
        lock.lock();
        lock.unlock();
        lock.unlock();
        System.out.println("rte1");
        redissonClient.getHyperLogLog("testlog").add("d");
        return "成功！";
    }

}
