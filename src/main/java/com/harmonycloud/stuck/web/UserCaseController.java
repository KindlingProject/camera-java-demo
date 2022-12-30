package com.harmonycloud.stuck.web;


import com.google.common.cache.Cache;
import com.harmonycloud.stuck.bean.Result;
import com.harmonycloud.stuck.conf.CacheGuava;
import com.harmonycloud.stuck.util.userCase.ServiceA;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


@Api(value = "userCase", tags = {"userCase"})
@RestController
@RequestMapping("/userCase")
public class UserCaseController {

    private static Logger LOG = LogManager.getLogger(UserCaseController.class);

    private static Map<String, User> userMap = new ConcurrentHashMap<>();

    @Resource
    private CacheGuava cacheGuava;
    @Resource
    private ServiceA serviceA;

    @ApiOperation(value = "concurrentHashMap线程安全性")
    @RequestMapping(value = "/currentHashMap", method = RequestMethod.GET)
    public Result currentHashMap() {

        try {
            UserService userService = new UserService();

            User user = new User();
            user.setUsername("张三");
            user.setAge(18);
            userService.register(user);
//            int threadCount = 20;
//
//            ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
//            forkJoinPool.execute(() -> IntStream.range(0, threadCount)
//                    .mapToObj(i -> new User("张三", i))
//                    .parallel().forEach(userService::register));
//

//            forkJoinPool.shutdown();
            Thread.sleep(500);
            userMap.remove("张三");

        } catch (Exception e) {
            return Result.fail("error" + e);
        }
        return Result.success("success");

    }


    @RequestMapping(value = "/guavaChache", method = RequestMethod.GET)
    public Result cache() throws ExecutionException {
        this.handleStock();
        return Result.success("success");
    }


    @Synchronized
    public void handleStock() throws ExecutionException {
        Cache<String, Object> guavaChache = cacheGuava.getGuavaChache();
        Integer stock = (Integer) guavaChache.get("stock", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1000;
            }
        });
        LOG.info("当前库存={}", stock);
        LOG.info("库存-1");
        stock = stock - 1;
        guavaChache.put("stock", stock);
    }

    @RequestMapping(value = "/setStock", method = RequestMethod.GET)
    public Result setStock() {
        Cache<String, Object> guavaChache = cacheGuava.getGuavaChache();
        guavaChache.put("stock", 20);
        return Result.success("success");
    }


    @RequestMapping(value = "/createManySpan", method = RequestMethod.GET)
    public Result createManySpan() throws Exception {
        Cache<String, Object> guavaChache = cacheGuava.getGuavaChache();
        Integer stock = (Integer) guavaChache.get("stock", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1000;
            }
        });
        serviceA.doSomethingA();
        Thread.sleep(550);
        return Result.success("success");
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class User {
        /**
         * 用户名，也是Map的key
         */
        private String username;

        private int age;
    }

    class UserService {

        /**
         * 用户注册
         *
         * @param user
         * @return
         */
        boolean register(User user) {
            if (userMap.containsKey(user.getUsername())) {
                LOG.info("用户已存在");

                return false;
            } else {
                userMap.put(user.getUsername(), user);
                LOG.info("用户" + user.getUsername() + "注册成功");

                return true;
            }
        }
    }


}
