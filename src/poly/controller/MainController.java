package poly.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.DataDTO;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Controller
public class MainController {
	private Logger log = Logger.getLogger(this.getClass());

	// 크롤링 데이터를 확인 및 들고오기 위한 서비스
	@Resource(name = "CommuService")
	private ICommuService commuService;

	// 메인 페이지
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + " index start!");
		int result = 0;
		// 값 있는지 확인
		result = commuService.checkCrawlingData();

		result = commuService.checkAnalysisData();

		String colNm = "AnalysisDcCom_" + DateUtil.getDateTime("yyyyMMddHH");

		List<DataDTO> rList = commuService.getAnalysisData(colNm);
		colNm = "WriterDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<DataDTO> wList = commuService.getAnalysisData(colNm);
		colNm = "TimeDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<DataDTO> tList = commuService.getAnalysisData(colNm);
		colNm = "OpinionDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<DataDTO> oList = commuService.getAnalysisData(colNm);
		// 있는경우 값 들고와서 워드클라우드 보여주기

		model.addAttribute("rList", rList);
		model.addAttribute("wList", wList);
		model.addAttribute("tList", tList);
		model.addAttribute("oList", oList);


		log.info(this.getClass().getName() + " index end!");

		return "/user/index";

	}

}
