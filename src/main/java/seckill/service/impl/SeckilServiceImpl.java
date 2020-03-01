package seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import seckill.Entity.Seckill;
import seckill.Entity.SuccessKilled;
import seckill.Enum.SeckillStateEnum;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.exception.SeckillException;
import seckill.respository.SeckillRespository;
import seckill.respository.SuccessKilledRespository;
import seckill.service.SeckilService;

import java.rmi.server.ExportException;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class SeckilServiceImpl implements SeckilService {
    @Autowired
    private SeckillRespository seckillRespository;

    @Autowired
    private SuccessKilledRespository successKilledRespository;
    //盐值  用于混淆md5
    private  final String slat = "berry";

    @Override
    public List<Seckill> querySeckillList() {
        return seckillRespository.findAll();
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillRespository.findBySeckillId(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化
        /*
        get from cache
        if null
        get from db
        else
           put cache
         */

        Seckill seckill = seckillRespository.findBySeckillId(seckillId);
        if(seckill == null){
           return new  Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();//当前时间
        if(nowTime.getTime()  < startTime.getTime() || nowTime.getTime() > endTime.getTime() ){
            return  new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //加密 转化特定字符串过程 不可逆
        String md5 = getMd5(seckillId);
        return new Exposer(true,seckillId,md5);
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, ExportException, RepeatKillException {
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀
        Date nowDate = new Date();
        try{
            //先保存 可以过滤 重复的点击执行更新操作 缩短行级锁的时间
            Integer saveCount =successKilledRespository.saveSuccessKilled(seckillId,userPhone);
            if(saveCount <=0 ){
                //重复秒杀
                throw  new RepeatKillException("seckill repeat");
            }
            else{
                //减库存   热点商品竞争
                Integer updateCount = seckillRespository.update(seckillId,nowDate);
                if(updateCount <= 0){
                    //秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                }
                else
                    {
                        //秒杀成功
                        SuccessKilled successKilled = successKilledRespository.findByIdWithSeckill(seckillId,userPhone);
                        return  new SeckillExecution(seckillId, SeckillStateEnum.SUCESS,successKilled); }
            }
        }
        catch (SeckillCloseException e1){
            throw  e1;
        }
        catch (RepeatKillException e2){
            throw  e2;
        }
        catch (Exception e){
            log.error(e.getMessage(),e);
            //所有编译异常
            throw  new SeckillException("seckill inner error"+e.getMessage());

        }

    }

    private String getMd5(long seckillId){
        String base = seckillId +"/" +slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return  md5;
    }

    public SeckillExecution executeProcedure(long seckillId, long userPhone, String md5){
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowDate = new Date();
        try{
            //优化方案 使用存储过程秒杀  不使用声明式事务，减少行锁等待时间
            Integer result = seckillRespository.killProcedure(seckillId,userPhone,nowDate);
            if(result==1){
                //秒杀成功
                SuccessKilled successKilled = successKilledRespository.findByIdWithSeckill(seckillId,userPhone);
                return  new SeckillExecution(seckillId, SeckillStateEnum.SUCESS,successKilled);
            }else{
            return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }

    }

}
