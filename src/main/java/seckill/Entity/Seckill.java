package seckill.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
//秒杀存储的定义
@NamedStoredProcedureQueries(
        @NamedStoredProcedureQuery(name = "executeSeckill", procedureName = "excute_seckill",
                parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "seckillId", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "userPhone", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "killTime", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Integer.class)
                }))
public class Seckill {
    @Id
    private long seckillId;
    private  String name;
    private int number;
    private Date startTime;
    private Date endTime;
    private Date createTime;

}
