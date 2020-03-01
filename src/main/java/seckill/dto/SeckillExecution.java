package seckill.dto;

import lombok.Data;


import seckill.Entity.SuccessKilled;
import seckill.Enum.SeckillStateEnum;

@Data
//秒杀执行后的结果
public class SeckillExecution {
    private  long seckill;
    //状态
    private  int  state;

    private String stateInfo;
    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckill, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled) {
        this.seckill = seckill;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckill, SeckillStateEnum seckillStateEnum) {
        this.seckill = seckill;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
    }
}
