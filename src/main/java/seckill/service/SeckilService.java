package seckill.service;

import seckill.Entity.Seckill;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillException;

import java.rmi.server.ExportException;
import java.util.List;

//使用者的角度
public interface SeckilService {
    //查询所有秒杀记录

    List<Seckill>  querySeckillList();
    //查询单个秒杀记录
    Seckill findById(long seckillId);
    //秒杀开启时 输出秒杀接口地址  否则 系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);
    //执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone , String md5)
    throws SeckillException, ExportException, RepeatKillException;

}
