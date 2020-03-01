package seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import seckill.Entity.Seckill;
import seckill.SeckillApplication;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;

import java.rmi.server.ExportException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
@Slf4j
public class SeckilServiceImplTest {
    @Autowired
    SeckilServiceImpl sellService;
    private final long id= 1000l;

    @Test
    public void querySeckillList() {
        List<Seckill> seckillList = sellService.querySeckillList();
        log.info("list={}",seckillList);
    }

    @Test
    public void findById() {
        Seckill seckill = sellService.findById(id);
        log.info("seckill = {}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
      Exposer exposer =  sellService.exportSeckillUrl(id);
      log.info("exposer = {}",exposer);
      //exposer = Exposer(exposed=false,
        // seckillId=1000,
        // md5=null,
        // now=1582704996345,
        // end=1581811200000,
        // start=1581897600000)

        //exposer = Exposer(exposed=true,
        // seckillId=1000,
        // md5=904eca900c18b8ed02a49cd141f5646b, n
        // ow=0, end=0, start=0)

    }

    @Test
    public void executeSeckill() throws ExportException {
        long userphone = 13477777132l;
        String md5 = "904eca900c18b8ed02a49cd141f5646b";
        SeckillExecution seckillExecution = sellService.executeProcedure(id,userphone,md5);
        log.info("seckillExecution = {}",seckillExecution);
        //seckill.exception.SeckillException: seckill data rewrite

        // seckillExecution =
        // SeckillExecution(seckill=1000, state=1, stateInfo=秒杀成功,
        // successKilled=SuccessKilled(seckillId=1000, userPhone=13481543132, state=-1, createTime=2020-02-27 00:25:58.0,
        // seckill=Seckill(seckillId=1000, name=10000秒杀ipad, number=97, startTime=2020-02-16 08:00:00.0, endTime=2020-03-17 08:00:00.0, createTime=2020-02-17 19:33:39.0)))
    }
}