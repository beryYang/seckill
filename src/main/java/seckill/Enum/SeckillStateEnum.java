package seckill.Enum;

import lombok.Getter;

//使用枚举表示常量数据字典
@Getter
public enum SeckillStateEnum {
    SUCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改")
    ;
    private int state;
    private  String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }
    //工具
    public static  SeckillStateEnum stateOf(int index){
        for(SeckillStateEnum stateEnum : values()){
            if(stateEnum.getState() == index)
                return stateEnum;
        }
        return null ;
    }
}
