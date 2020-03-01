package seckill.respository.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seckill.Entity.Seckill;
import seckill.SeckillApplication;
import seckill.respository.SeckillRespository;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class RedisDaoTest {
    private  long id = 1000l;
    @Resource
    private  RedisDao redisDao;

    @Autowired
    private SeckillRespository seckillRespository;

    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null){
            seckill = seckillRespository.findBySeckillId(id);
            if(seckill != null){
                String  result =redisDao.putSeckill(seckill);
                System.out.println(result);
                Seckill seckill1 =redisDao.getSeckill(id);
                System.out.println(seckill1);
            }
        }
    }

    @Test
    public void putSeckill() {
    }
}