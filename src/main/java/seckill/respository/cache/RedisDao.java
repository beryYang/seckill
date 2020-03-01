package seckill.respository.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import seckill.Entity.Seckill;
@Slf4j

public class RedisDao {
    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    public Seckill getSeckill(long seckillId){
        //redis操作
        try {
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化
                //拿到的是二进制数组===反序列化
                //protostuff -- pojo
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    Seckill seckill = schema.newMessage();//空对象
                    //将空对象反序列化
                    ProtobufIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            }

            finally {
                jedis.close();
            }

        }
        catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;

    }

    public String putSeckill(Seckill seckill){
        //序列化过程 将 对象  === 》 byte[]
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:"+seckill.getSeckillId();
                byte [] bytes = ProtobufIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60*60;
               String result =  jedis.setex(key.getBytes(),timeout,bytes);
               return result;

            }
            finally {
                jedis.close();
            }

        }
        catch (Exception e ){
            log.error(e.getMessage(),e);
        }
        return null;
    }

}
