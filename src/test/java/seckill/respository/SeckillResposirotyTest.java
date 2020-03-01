package seckill.respository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seckill.Entity.Seckill;
import seckill.SeckillApplication;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class SeckillResposirotyTest {
    @Autowired
    private SeckillRespository seckillResposirory;

    @Test
    public void update() {
        //seckillResposiroty.saveBySeckillIdAndAndStartTimeBeforeAndEndTimeAfterAndNumberNot(1000L,new Date(),new Date(),0);
        seckillResposirory.update(1000L, new Date());
       // seckillResposiroty.update(1000l);
    }

    @Test
    public void findBySeckillId() {
        Seckill seckill =  seckillResposirory.findBySeckillId(1000l);
        System.out.println(seckill);
    }
}