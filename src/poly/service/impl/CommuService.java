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
				int views = Integer.parseInt(postInfo.select("td.gall_count").text());
				String link = "https://gall.dcinside.com"+postInfo.select("td.gall_tit a").eq(0).attr("href");

				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setTime(time);
				pDTO.setTitle(title);
				pDTO.setWriter(writer);
				pDTO.setViews(views);
				pDTO.setLink(link);

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
		log.info("rList size : " + rList.size());

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
		
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> rList = commuService.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		log.info(rList.size());
		// 컴본갤
		if (rList.size() > 0) {
			String[] title = new String[rList.size()];
			String[] writer = new String[rList.size()];
			String[] time = new String[rList.size()];
			for (int i = 0; i < rList.size(); i++) {
				String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
				title[i] = rList.get(i).getTitle().replaceAll(match, "").toLowerCase();
				writer[i] = rList.get(i).getWriter();
				time[i] = rList.get(i).getTime().substring(0, 15) + "0";
			}
			c.assign("title", title);
			// 형태소 분석
			c.eval("m_df <- title %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
			c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit %>% count(noun, sort = TRUE)");
			c.eval("wordList <- m_df$noun");
			c.eval("m_df <- filter(m_df,nchar(noun)>=2)");
			c.eval("m_df <- filter(m_df,n>=2)");
			// 긍정 부정 분석
			c.eval("wordList = unlist(wordList)");
			c.eval("posM = match(wordList, positive)");
			c.eval("posM = !is.na(posM)");
			c.eval("negM = match(wordList, negative)");
			c.eval("negM = !is.na(negM)");

			// 형태소 분석 결과 몽고DB에 넣기
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
				pDTO.setCount(Integer.parseInt(count[i]));
				pList.add(pDTO);
				pDTO = null;
			}
			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 긍정 부정 결과 몽고DB에 넣기

			colNm = "OpinionDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();
			x = c.eval("sum(posM)");
			y = c.eval("sum(negM)");

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name("컴퓨터 본체 갤러리");
			pDTO.setWord("긍정");
			pDTO.setCount(Integer.parseInt(x.asString()));
			pList.add(pDTO);
			pDTO = null;

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name("컴퓨터 본체 갤러리");
			pDTO.setWord("부정");
			pDTO.setCount(Integer.parseInt(y.asString()));
			pList.add(pDTO);
			pDTO = null;

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 글쓴이 분석 결과 몽고 DB에 넣기

			colNm = "WriterDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> writer_name = new ArrayList<String>();
			List<Integer> writer_count = new ArrayList<Integer>();

			for (int i = 0; i < writer.length; i++) {
				if (!writer_name.contains(writer[i])) {
					writer_name.add(writer[i]);
					int cnt = 0;
					for (int j = i; j < writer.length; j++) {
						if (writer[i].equals(writer[j])) {
							cnt++;
						}
					}
					writer_count.add(cnt);
				}
			}
			for (int i = 0; i < writer_name.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setWord(writer_name.get(i));
				pDTO.setCount(writer_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;
			// 글쓴 시간대별 분석 결과 몽고 DB에 넣기

			colNm = "TimeDcCom_" + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> time_section = new ArrayList<String>();
			List<Integer> time_count = new ArrayList<Integer>();

			for (int i = 0; i < time.length; i++) {
				if (!time_section.contains(time[i])) {
					time_section.add(time[i]);
					int cnt = 0;
					for (int j = i; j < time.length; j++) {
						if (time[i].equals(time[j])) {
							cnt++;
						}
					}
					time_count.add(cnt);
				}
			}
			for (int i = 0; i < time_section.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setWord(time_section.get(i));
				pDTO.setCount(time_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

		}
		// 여러 크롤링 하나하나 찾아서 넣기
		c.close();

		log.info(this.getClass().getName() + " AnalysisDcComData end!");
		return 0;
	}

}
