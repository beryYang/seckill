package seckill.dto;

import lombok.Data;

//暴露秒杀地址
@Data
public class Exposer {
    //是否开启秒杀
    private boolean exposed;
    private long seckillId;
    //加密
    private  String md5;
    //系统当前时间(毫秒)
    private  long now;
    private  long end;
    private  long start;

    public Exposer(boolean exposed, long seckillId, String md5) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.md5 = md5;
    }

    public Exposer(boolean exposed, long seckillId,long now, long end, long start) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.end = end;
        this.start = start;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
}
