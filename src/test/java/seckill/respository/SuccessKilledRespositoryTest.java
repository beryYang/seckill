package seckill.respository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seckill.SeckillApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class SuccessKilledRespositoryTest {
    @Autowired
    private SuccessKilledRespository successKilledRespository;

    @Test
    public void saveSuccessKilled() {
       successKilledRespository.saveSuccessKilled(1000l,123456789l);
    }

    @Test
    public void findByIdWithSeckill() {
        successKilledRespository.findByIdWithSeckill(1000l,123456789l);
    }
}