package com.yzy.controller;

import java.util.Date;
import java.util.List;

import com.yzy.dto.Exposer;
import com.yzy.dto.SeckillExecution;
import com.yzy.dto.SeckillResult;
import com.yzy.entity.Seckill;
import com.yzy.enums.SeckillStateEnum;
import com.yzy.exception.RepeatKillException;
import com.yzy.exception.SeckillCloseException;
import com.yzy.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // @Service @Componet
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分 /seckill/list
public class SeckillController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	/**
	   URL:http://localhost:8080/seckill/list

     >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 MyBatis读取MySQL的datetime字段, 结果总是null

	 因为你数据库中为create_time,而pojo中是createTime,这两个字段是不一样的,而你没有给出orm规则的情况下,
	 ibatis的默认处理规则是只有数据库字段与pojo字段完全一样才orm的

     那为啥ftl中为啥create_time就报错为null?? 一定要下面这样才行
	 <td>${sk.createTime?datetime}</td>
	 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);

		System.out.println("list="+list.get(1));

		// list.jsp + model = ModelAndView
		return "list";// WEB-INF/jsp/"list".jsp
	}

	/**
	 URL:http://localhost:8080/seckill/1000/detail
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	/***
	 *ajax json  返回抢购商品的信息,有了这个信息才能够调用下面的抢购接口
	 * @param seckillId
	 * @return
     */
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}

	/**
	 * 执行秒杀接口   调用存储过程的版本
	 * @param seckillId
	 * @param md5
	 * @param phone
     * @return
     */

	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
												   @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long phone) {
		// springmvc valid
		if (phone == null) {
			return new SeckillResult<>(false, "未注册");
		}
		try {
			// 存储过程调用
			//SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);

			SeckillExecution execution = seckillService.executeSeckill(seckillId,phone,md5);

			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
		}
	}

	/**
	 * 返回当前系统时间
	 * @return
     */

	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}

}
