package seckill.respository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import seckill.Entity.Seckill;


import java.util.Date;
import java.util.List;
import java.util.Map;


public interface SeckillRespository extends Repository<Seckill, Long> {
    //减库存

    @Transactional
    @Query(value = "update seckill SET number  = number -1" +
            " where seckill_id = :seckillId" +
            " and start_time <= :killTime" +
            " and end_time >= :killTime" +
            " and number > 0", nativeQuery = true)
    @Modifying
    int update(@Param("seckillId")Long seckillId,@Param("killTime") Date killTime);

    //根据id查询秒杀对象
    Seckill findBySeckillId(long  seckillId);

    List<Seckill> findAll();

    @Transactional
    //自定义名称
    @Procedure(name = "executeSeckill")
        //sql中过程名称
    //@Procedure("sexcute_seckill")
     Integer killProcedure(@Param("seckillId") Long seckillId,@Param("userPhone")Long userPhone,@Param("killTime") Date killTime);

}

