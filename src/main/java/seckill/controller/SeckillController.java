package seckill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seckill.Entity.Seckill;
import seckill.Enum.SeckillStateEnum;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.dto.SeckillResult;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.service.impl.SeckilServiceImpl;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckilServiceImpl seckilService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        List<Seckill> seckillList = seckilService.querySeckillList();
        model.addAttribute("list",seckillList);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId")  Long seckillId,Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckilService.findById(seckillId);
        if(seckill == null){
            return "redirect:/seckill/list";//可以写 return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    //ajax  返回json
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer exposer = seckilService.exportSeckillUrl(seckillId);
            result = new SeckillResult(true,exposer);
        }
        catch (Exception e){
            log.info(e.getMessage(),e);
            result = new SeckillResult(false,e.getMessage());

        }
        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                @CookieValue(value = "killPhone",required = false) Long usePhone){
        if(usePhone == null ){
            return  new SeckillResult(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;
        try{
            //SeckillExecution execution = seckilService.executeSeckill(seckillId,usePhone,md5);
            SeckillExecution execution = seckilService.executeProcedure(seckillId,usePhone,md5);
            return result = new SeckillResult(true,execution);
        }
        catch (RepeatKillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return  result = new SeckillResult(true,execution);

        }
        catch (SeckillCloseException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return result = new SeckillResult(true,execution);

        }
        catch (Exception e){
            log.info(e.getMessage(),e);
           return result = new SeckillResult(true,e.getMessage());

        }

    }
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return  new SeckillResult<Long>(true,now.getTime());

    }

}
