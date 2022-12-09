package com.harmonycloud.stuck.conf;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheGuava {

   private Cache<String ,Object> guavaChache = CacheBuilder.newBuilder()
           .initialCapacity(12)
           .concurrencyLevel(4)
           .maximumSize(10)
           .expireAfterWrite(600, TimeUnit.SECONDS)
           .recordStats()
           .build();

   public Cache<String, Object> getGuavaChache(){
       return guavaChache;
   }


}
