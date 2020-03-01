package seckill.respository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import seckill.Entity.SuccessKilled;

public interface SuccessKilledRespository extends Repository<SuccessKilled, Long> {
    //插入购买明细
    @Transactional
    @Query(value = "insert ignore into  success_killed (seckill_id,user_phone)" +
            " values (?1,?2)" , nativeQuery = true)
    @Modifying
    int saveSuccessKilled(long seckillId,long usePhone);
    //查询成功的


    @Query(value = " SELECT sk.*,s.seckill_id as s_seckill_id,s.name,s.number,s.start_time,s.end_time,s.create_time as s_create_time "  +
                   "  FROM success_killed sk INNER JOIN seckill s ON s.seckill_id = sk.seckill_id " +
                   "  WHERE sk.seckill_id=:seckillId AND sk.user_phone=:userPhone", nativeQuery = true)
    SuccessKilled findByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
