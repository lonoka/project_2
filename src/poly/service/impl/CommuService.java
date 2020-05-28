package poly.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Service;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.dto.MelonDTO;
import poly.persistance.mongo.ICommuMapper;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Service("CommuService")
public class CommuService implements ICommuService {

	@Resource(name = "CommuMapper")
	private ICommuMapper commuMapper;

	@Resource(name = "CommuService")
	private ICommuService commuService;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int collectDcComData() throws Exception {
		log.info(this.getClass().getName() + " collectDcComData Start!");

		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 1; i <= 10; i++) {
			String url = "https://gall.dcinside.com/board/lists/?id=pridepc_new3&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table.gall_list");

			Iterator<Element> postList = element.select("tr.us-post").iterator();

			while (postList.hasNext()) {
				Element postInfo = postList.next();
				String title = postInfo.select("td.gall_tit a").eq(0).text();
				String writer = postInfo.select("td.gall_writer").attr("data-nick");
				String time = postInfo.select("td.gall_date").attr("title");

				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setTime(time);
				pDTO.setTitle(title);
				pDTO.setWriter(writer);

				pList.add(pDTO);
			}
		}

		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		log.info(this.getClass().getName() + " collectDcComData end!");
		return res;
	}

	// 크롤링 데이터 가져오기
	@Override
	public List<CommuDTO> getData(String colNm) throws Exception {
		log.info(this.getClass().getName() + " getData start!");

		log.info("colNm : " + colNm);

		List<CommuDTO> rList = commuMapper.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}

		log.info(this.getClass().getName() + " getData end!");

		return rList;
	}

	// 크롤링 데이터 가져오기
	@Override
	public List<DataDTO> getAnalysisData(String colNm) throws Exception {
		log.info(this.getClass().getName() + " getData start!");

		log.info("colNm : " + colNm);

		List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

		if (rList == null) {
			rList = new ArrayList<DataDTO>();
		}

		log.info(this.getClass().getName() + " getData end!");

		return rList;
	}

	// 크롤링 데이터 있는지 없는지 확인
	@Override
	public int checkCrawlingData() throws Exception {
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		log.info(colNm);
		List<CommuDTO> rList = commuMapper.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		log.info(rList.size());
		if (rList.size() == 0) {
			commuService.collectDcComData();
		}
		return 1;
	}

	// 분석 데이터 있는지 없는지 확인
	@Override
	public int checkAnalysisData() throws Exception {
		String colNm = "AnalysisDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		log.info(colNm);
		List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

		if (rList == null) {
			rList = new ArrayList<DataDTO>();
		}
		log.info(rList.size());
		if (rList.size() == 0) {
			commuService.AnalysisData();
		}
		return 1;
	}

	// 데이터 분석하기
	@Override
	public int AnalysisData() throws Exception {

		log.info(this.getClass().getName() + " AnalysisDcComData start!");

		// R 연결 후 라이브러리 추가
		RConnection c = new RConnection();

		c.eval("library(KoNLP)");
		c.eval("library(dplyr)");
		c.eval("useNIADic()");
		c.eval("library(reshape2)");
		c.eval("library(stringr)");
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> rList = commuService.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		log.info(rList.size());
		// 컴본갤
		if (rList.size() > 0) {
			String[] str = new String[rList.size()];
			for (int i = 0; i < rList.size(); i++) {
				String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
				str[i] = rList.get(i).getTitle().replaceAll(match, "");
			}
			c.assign("pList", str);
			c.eval("m_df <- pList %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
			c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit %>% count(noun, sort = TRUE)");
			c.eval("m_df <- filter(m_df,nchar(noun)>=2)");
			c.eval("m_df <- filter(m_df,n>=2)");

			REXP x = c.eval("m_df$noun");
			REXP y = c.eval("m_df$n");
			colNm = "AnalysisDcCom_" + DateUtil.getDateTime("yyyyMMddHH");

			ArrayList<DataDTO> pList = new ArrayList<DataDTO>();
			DataDTO pDTO = new DataDTO();
			String[] noun = x.asStrings();
			String[] count = y.asStrings();

			for (int i = 0; i < noun.length; i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setWord(noun[i]);
				pDTO.setCount(count[i]);
				pList.add(pDTO);
				pDTO = null;
			}
			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

		}
		// 여러 크롤링 하나하나 찾아서 넣기
		c.close();

		log.info(this.getClass().getName() + " AnalysisDcComData end!");
		return 0;
	}

}
