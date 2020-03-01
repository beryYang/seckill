package seckill.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class SuccessKilled {
    @Column(insertable=false,updatable=false)
    private long seckillId;
    @Id
    private long userPhone;
    private short state;
    private Date createTime;
    @ManyToOne
    @JoinColumn(name = "seckillId")
    private Seckill seckill;
}
