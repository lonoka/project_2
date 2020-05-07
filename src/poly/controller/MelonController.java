package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.MelonDTO;
import poly.service.IMelonService;

@Controller
public class MelonController {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="MelonService")
	private IMelonService melonService;
	
	/**
	 * 멜론 정보 수집
	 */
	@RequestMapping(value="melon/collectMelonRank")
	@ResponseBody
	public String collectMelonRank(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info(this.getClass().getName()+"collectMelonRank start!");
		melonService.collectMelonRank();
		log.info(this.getClass().getName()+"collectMelonRank end!");
		return "success";
	}
	/**
	 * 멜론 데이터 가져오는 일반 화면
	 */
	@RequestMapping(value="melon/melonTop100")
	public String melonTOp100(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info(this.getClass().getName()+"melonTOp100 start!");
		log.info(this.getClass().getName()+"melonTOp100 end!");
		return "/melon/melonTop100";
	}
	
	/**
	 * 멜론 정보 가져오기
	 */
	@RequestMapping(value="melon/getRank")
	@ResponseBody
	public List<MelonDTO> getRank(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info(this.getClass().getName()+"getRank start!");
		List<MelonDTO> rList = melonService.getRank();
		
		if(rList ==null) {
			rList = new ArrayList<MelonDTO>();
		}
		log.info(this.getClass().getName()+"getRank end!");
		return rList;
	}
}
