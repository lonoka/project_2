package poly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Controller
public class MainController {
	private Logger log = Logger.getLogger(this.getClass());

	// 크롤링 데이터를 확인 및 들고오기 위한 서비스
	@Resource(name = "CommuService")
	private ICommuService commuService;

	// r 라이브러리 호출
	@RequestMapping(value = "rConnection")
	@ResponseBody
	public String rConnection(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + "rConnection start!");
		RConnection c = new RConnection();
		c.eval("library(tidyverse)");
		c.eval("library(KoNLP)");
		c.eval("useNIADic()");
		c.eval("library(stringr)");
		c.eval("library(reshape2)");
		c.eval("library(dplyr)");
		c.eval("negative <- readLines('c:\\\\word\\\\negative.txt', encoding = 'UTF-8')");
		c.eval("positive <- readLines('c:\\\\word\\\\positive.txt', encoding = 'UTF-8')");

		c.close();

		log.info(this.getClass().getName() + "rConnection end!");

		return "success";
	}

	// 메인 페이지
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + " index start!");
		int result = 0;
		// 값 있는지 확인
		result = commuService.checkCrawlingData();
		result = commuService.checkAnalysisData();

		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		sList.add("Slr_");
		List<Map> pList = new ArrayList<Map>();
		Map<String, Object> pMap = new HashMap();
		String colNm = "";
		for (int i = 0; i < sList.size(); i++) {
			pMap = new HashMap<String, Object>();
			colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> bList = commuService.getData(colNm);
			colNm = "Analysis" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> rList = commuService.getAnalysisData(colNm);
			colNm = "Writer" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> wList = commuService.getAnalysisData(colNm);
			colNm = "Time" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> tList = commuService.getAnalysisData(colNm);
			colNm = "Opinion" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> oList = commuService.getAnalysisData(colNm);
			pMap.put("bList", bList);
			pMap.put("rList", rList);
			pMap.put("wList", wList);
			pMap.put("tList", tList);
			pMap.put("oList", oList);
			pList.add(pMap);
			pMap = null;

		}

		// 있는경우 값 들고와서 워드클라우드 보여주기

		model.addAttribute("pList", pList);

		log.info(this.getClass().getName() + " index end!");

		return "/user/index";

	}

}
