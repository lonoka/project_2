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
import org.springframework.stereotype.Service;

import poly.dto.CommuDTO;
import poly.dto.MelonDTO;
import poly.persistance.mongo.ICommuMapper;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Service("CommuService")
public class CommuService implements ICommuService {

	@Resource(name="CommuMapper")
	private ICommuMapper commuMapper;

	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public int collectDcComData() throws Exception {
		log.info(this.getClass().getName() + " collectDcComData Start!");
		
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for(int i = 1; i<=10;i++) {
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

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddhhmmss"));
				pDTO.setCommu_name("DC inside 컴본갤");
				pDTO.setTime(time);
				pDTO.setTitle(title);
				pDTO.setWriter(writer);

				pList.add(pDTO);
			}
		}
		
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddhh");
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);
		
		log.info(this.getClass().getName() + " collectDcComData end!");
		return res;
	}

	@Override
	public List<CommuDTO> getData() throws Exception {
		log.info(this.getClass().getName() + " getData start!");
		
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddhh");

		List<CommuDTO> rList = commuMapper.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		
		log.info(this.getClass().getName() + " getData end!");
		
		return rList;
	}

}
