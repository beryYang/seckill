package seckill.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seckill.respository.cache.RedisDao;


@Configuration
public class Beans {
    //通过构造方法去注入redisDao
    //参数从application.yml获取
    @Bean
    public RedisDao redisDao(@Value("${spring.redis.host}") String ip, @Value("${spring.redis.port}") Integer port){
        return new RedisDao(ip,port);
    }
}