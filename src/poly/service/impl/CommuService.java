package poly.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
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
import poly.util.CmmUtil;
import poly.util.DateUtil;

@Service("CommuService")
public class CommuService implements ICommuService {

	@Resource(name = "CommuMapper")
	private ICommuMapper commuMapper;

	@Resource(name = "CommuService")
	private ICommuService commuService;

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 커뮤니티 크롤링
	 */
	// 컴본갤
	@Override
	public int collectDcComData() throws Exception {
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 1; i <= 20; i++) {
			String url = "https://gall.dcinside.com/board/lists/?id=pridepc_new3&list_num=30&page=";
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
				String link = "https://gall.dcinside.com" + postInfo.select("td.gall_tit a").eq(0).attr("href");

				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setTime(time);
				pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
				pDTO.setWriter(writer.replaceAll("'", "&#39;"));
				pDTO.setViews(views);
				pDTO.setLink(link);

				pList.add(pDTO);
			}
		}

		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// slr클럽
	@Override
	public int collectSlrData() throws Exception {
		Document odoc = Jsoup.connect("http://www.slrclub.com/bbs/zboard.php?id=free&page=1").get();
		String page = odoc.select("span#actpg").text();
		int res = 0;
		String tmp = "";
		Calendar cal = Calendar.getInstance();
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 0; i <= 19; i++) {
			String url = "http://www.slrclub.com/bbs/zboard.php?id=free&page=";
			url = url + Integer.toString(Integer.parseInt(page) - i);
			Document doc = null;
			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#bbs_list");

			Iterator<Element> postList = element.select("tbody > tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")
						&& !postInfo.select("td").eq(0).text().equals("")) {
					String title = postInfo.select("td.sbj > a").text();
					String writer = postInfo.select("td.list_name > span").text();
					String time = postInfo.select("td.list_date").text();

					if (time.contains(":")) {
						if (tmp.equals("")) {
							tmp = time.substring(0, 2);
						}
						if (tmp.equals("00") && time.substring(0, 2).equals("23")) {
							cal.add(Calendar.DATE, -1);
							tmp = time.substring(0, 2);
						} else {
							tmp = time.substring(0, 2);
						}
						int year = cal.get(cal.YEAR);
						int month = (cal.get(cal.MONTH) + 1);
						int date = cal.get(cal.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					int views = Integer.parseInt(postInfo.select("td.list_click").text());
					String link = "http://www.slrclub.com" + postInfo.select("td.sbj > a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("SLR클럽");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					pDTO = null;
				}
			}
		}

		String colNm = "Slr_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 뽐뿌
	@Override
	public int collectPpomData() throws Exception {
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 20; i++) {

			String url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=freeboard&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#revolution_main_table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("list_vspace")) {
					String title = postInfo.select("td").eq(2).select("a").text();
					String writer = "";
					if (postInfo.select("td").eq(1).select("a").hasText()) {
						writer = postInfo.select("td").eq(1).select("span.list_name").text();
					} else {
						writer = postInfo.select("td").eq(1).select("img").attr("alt");
					}
					String time = postInfo.select("td").eq(3).attr("title");
					int views = Integer.parseInt(postInfo.select("td").eq(5).text());
					String link = "http://www.ppomppu.co.kr/zboard/"
							+ postInfo.select("td").eq(2).select("a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("뽐뿌");
					pDTO.setTime("20" + time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					pDTO = null;
				}
			}
		}

		for (int i = 1; i <= 20; i++) {

			String url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=issue&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#revolution_main_table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("list_vspace")) {
					String title = postInfo.select("td").eq(2).select("a").text();
					String writer = "";
					if (postInfo.select("td").eq(1).select("a").hasText()) {
						writer = postInfo.select("td").eq(1).select("span.list_name").text();
					} else {
						writer = postInfo.select("td").eq(1).select("img").attr("alt");
					}
					String time = postInfo.select("td").eq(3).attr("title");
					int views = Integer.parseInt(postInfo.select("td").eq(5).text());
					String link = "http://www.ppomppu.co.kr/zboard/"
							+ postInfo.select("td").eq(2).select("a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("뽐뿌");
					pDTO.setTime("20" + time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					pDTO = null;
				}
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		String colNm = "Ppom_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 82쿡
	@Override
	public int collect82CookData() throws Exception {
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 1; i <= 20; i++) {
			String url = "https://www.82cook.com/entiz/enti.php?bn=15&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("form#bbs > table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("numbers")) {
					String title = postInfo.select("td.title a").text();
					String writer = postInfo.select("td.user_function").text();
					String time = postInfo.select("td.regdate").attr("title");
					int views = Integer.parseInt(StringReplace(postInfo.select("td").eq(4).text()));
					String link = "https://www.82cook.com/entiz/" + postInfo.select("td.title a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("82쿡");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}

		String colNm = "82Cook_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// MLBPARK
	@Override
	public int collectMPData() throws Exception {
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		String tmp1 = "";
		Calendar cal1 = Calendar.getInstance();
		String tmp2 = "";
		Calendar cal2 = Calendar.getInstance();

		for (int i = 1; i <= 20; i++) {

			String url = "http://mlbpark.donga.com/mp/b.php?b=kbotown&p=";
			url = url + Integer.toString((i - 1) * 3) + "1";

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table.tbl_type01 > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")
						&& !postInfo.select("span.date").text().contains("-")) {
					String title = CmmUtil.nvl(postInfo.select("span.word").text())
							+ postInfo.select("a").attr("title");
					String writer = postInfo.select("span.nick").text();
					String time = postInfo.select("span.date").text();
					int views = Integer.parseInt(StringReplace(postInfo.select("span.viewV").text()));
					String link = postInfo.select("a").attr("href");

					postInfo = null;

					if (time.contains(":")) {
						if (tmp1.equals("")) {
							tmp1 = time.substring(0, 2);
						}
						if (tmp1.equals("00") && time.substring(0, 2).equals("23")) {
							cal1.add(Calendar.DATE, -1);
							tmp1 = time.substring(0, 2);
						} else {
							tmp1 = time.substring(0, 2);
						}
						int year = cal1.get(cal1.YEAR);
						int month = (cal1.get(cal1.MONTH) + 1);
						int date = cal1.get(cal1.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("MLBPARK");
					pDTO.setTime(time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}
		for (int i = 1; i <= 20; i++) {

			String url = "http://mlbpark.donga.com/mp/b.php?b=bullpen&p=";
			url = url + Integer.toString((i - 1) * 3) + "1";

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table.tbl_type01 > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")) {
					String title = postInfo.select("a").attr("title");
					String writer = postInfo.select("span.nick").text();
					String time = postInfo.select("span.date").text();
					int views = Integer.parseInt(StringReplace(postInfo.select("span.viewV").text()));
					String link = postInfo.select("a").attr("href");

					postInfo = null;

					if (time.contains(":")) {
						if (tmp2.equals("")) {
							tmp2 = time.substring(0, 2);
						}
						if (tmp2.equals("00") && time.substring(0, 2).equals("23")) {
							cal2.add(Calendar.DATE, -1);
							tmp2 = time.substring(0, 2);
						} else {
							tmp2 = time.substring(0, 2);
						}
						int year = cal2.get(cal2.YEAR);
						int month = (cal2.get(cal2.MONTH) + 1);
						int date = cal2.get(cal2.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("MLBPARK");
					pDTO.setTime(time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " "));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		String colNm = "Mlb_" + DateUtil.getDateTime("yyyyMMddHH");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 크롤링 데이터 가져오기
	@Override
	public List<CommuDTO> getData(String colNm) throws Exception {
		List<CommuDTO> rList = commuMapper.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		return rList;
	}

	// 크롤링 데이터 가져오기
	@Override
	public List<DataDTO> getAnalysisData(String colNm) throws Exception {
		List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

		if (rList == null) {
			rList = new ArrayList<DataDTO>();
		}
		return rList;
	}

	// 크롤링 데이터 있는지 없는지 확인
	// 커뮤니티 전체
	@Override
	public int checkCrawlingData(List<String> sList) throws Exception {
		for (int i = 0; i < sList.size(); i++) {
			String colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> rList = commuMapper.getData(colNm);

			if (rList == null) {
				rList = new ArrayList<CommuDTO>();
			}
			if (rList.size() == 0) {
				switch (sList.get(i)) {
				case "DcCom_":
					commuService.collectDcComData();
					break;
				case "Slr_":
					commuService.collectSlrData();
					break;
				case "Ppom_":
					commuService.collectPpomData();
					break;
				case "82Cook_":
					commuService.collect82CookData();
					break;
				case "Mlb_":
					commuService.collectMPData();
					break;
				}
			}
			rList = null;
		}
		return 1;
	}

	// 분석 데이터 있는지 없는지 확인
	@Override
	public int checkAnalysisData(List<String> sList) throws Exception {

		for (int i = 0; i < sList.size(); i++) {
			String colNm = "Analysis" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

			if (rList == null) {
				rList = new ArrayList<DataDTO>();
			}
			if (rList.size() == 0) {
				commuService.AnalysisData(sList.get(i));
			}
		}
		return 1;
	}

	// 데이터 분석하기
	@Override
	public int AnalysisData(String str) throws Exception {

		String comu = "";
		switch (str) {
		case "DcCom_":
			comu = "컴퓨터 본체 갤러리";
			break;
		case "Slr_":
			comu = "SLR클럽";
			break;
		case "Ppom_":
			comu = "뽐뿌";
			break;
		case "82Cook_":
			comu = "82쿡";
			break;
		case "Mlb_":
			comu = "MLBPARK";
			break;
		}

		// R 연결 후 라이브러리 추가
		RConnection c = new RConnection();

		String colNm = str + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> rList = commuService.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		if (rList.size() > 0) {
			String tmp = "";
			String[] title = new String[rList.size()];
			String[] writer = new String[rList.size()];
			String[] time = new String[rList.size()];
			for (int i = 0; i < rList.size(); i++) {
				String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
				title[i] = rList.get(i).getTitle().replaceAll(match, "").toLowerCase();
				writer[i] = rList.get(i).getWriter();
				time[i] = rList.get(i).getTime().substring(0, 15) + "0:00";

			}
			c.assign("title", title);
			// 형태소 분석
			c.eval("m_df <- title %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
			c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit");
			c.eval("wordList <- m_df$noun");
			c.eval("m_df <- m_df %>% count(noun, sort = TRUE)");
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
			colNm = "Analysis" + str + DateUtil.getDateTime("yyyyMMddHH");

			ArrayList<DataDTO> pList = new ArrayList<DataDTO>();
			DataDTO pDTO = new DataDTO();
			String[] noun = x.asStrings();
			String[] count = y.asStrings();

			for (int i = 0; i < noun.length; i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(noun[i]);
				pDTO.setCount(Integer.parseInt(count[i]));
				pList.add(pDTO);
				pDTO = null;
			}
			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 긍정 부정 결과 몽고DB에 넣기

			colNm = "Opinion" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();
			x = c.eval("sum(posM)");
			y = c.eval("sum(negM)");

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("긍정");
			pDTO.setCount(Integer.parseInt(x.asString()));
			pList.add(pDTO);
			pDTO = null;

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("부정");
			pDTO.setCount(Integer.parseInt(y.asString()));
			pList.add(pDTO);
			pDTO = null;

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 글쓴이 분석 결과 몽고 DB에 넣기

			colNm = "Writer" + str + DateUtil.getDateTime("yyyyMMddHH");
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
				pDTO.setCommu_name(comu);
				pDTO.setWord(writer_name.get(i));
				pDTO.setCount(writer_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;
			// 글쓴 시간대별 분석 결과 몽고 DB에 넣기

			colNm = "Time" + str + DateUtil.getDateTime("yyyyMMddHH");
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
				pDTO.setCommu_name(comu);
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

		return 0;
	}

	@Override
	public void cTest() throws Exception {

		Map<String, String> header = new HashMap<String, String>();
		header.put("origin", "http://www.slrclub.com");
		header.put("Referer", "http://www.slrclub.com");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
		Map<String, String> data = new HashMap<String, String>();
		data.put("user_id", "lonoka");
		data.put("password", "scarlet14!");
		data.put("autologin", "1");
		data.put("group_no", "1");
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36";
		Connection.Response response = Jsoup.connect("https://www.slrclub.com/login/process.php").userAgent(userAgent)
				.headers(header).data(data).method(Connection.Method.POST).execute();

		Map<String, String> loginCookie = response.cookies();
		log.info("###############################################");
		log.info(loginCookie);
		log.info("###############################################");

		Map<String, String> sheader = new HashMap<String, String>();
		sheader.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		sheader.put("Accept-Encoding", "gzip, deflate");
		sheader.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

		Document doc = Jsoup.connect("http://www.slrclub.com/service/search/?keyword=%EC%A3%BC%EC%8B%9D&section=free")
				.userAgent(userAgent).headers(sheader).cookies(loginCookie).get();

		log.info(doc);
	}

	// 특수기호 제거 함수
	public static String StringReplace(String str) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str = str.replaceAll(match, "");
		return str;
	}
}
