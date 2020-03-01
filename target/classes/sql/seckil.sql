--秒杀执行存储过程
--in入参  out 出参
delimiter $$
--row_count();返回上一条修改类型的影响行数
 --row_count 0  ：未修改  ；
 --         >0  : 修改的行数
 --         <0  : 错误

drop procedure  excute_seckill
create procedure `seckill`.`excute_seckill`
   ( in v_seckillId BIGINT,
     in v_phone BIGINT,
     in v_killTime timestamp ,
     out r_result int)
     begin
        declare insert_count int default 0;
        start transaction ;
        insert ignore into success_killed
        (seckill_id,user_phone,create_time)
        values (v_seckillId,v_phone,v_killTime);

        select row_count into insert_count;
        if(insert_count = 0) then
            rollback ;
            set r_result = -1;
        elseif(insert_count < 0) then
            rollback ;
            set r_result = -2;
        else
            update seckill
               set number  = number -1
             where seckill_id = v_seckillId
               and end_time >= v_killTime
               and start_time <= v_killTime
               and number >0;
            select row_count into insert_count;
            if(insert_count = 0) then
                rollback ;
                set r_result = -1;
            elseif(insert_count < 0) then
                rollback ;
                set r_result = -2;
            else
                commit ;
                set r_result = 1;
            end if;
      end if;
    end;
$$

--执行存储过程
delimiter ;
set  @r_result = -3;
call excute_seckill(1000,13481543132,now(),r_result);
select @r_result;


