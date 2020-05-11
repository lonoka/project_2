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

import poly.dto.MelonDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;
import poly.persistance.mongo.IMelonMapper;
import poly.service.IMelonService;
import poly.util.DateUtil;

@Service("MelonService")
public class MelonService implements IMelonService {

	@Resource(name = "MelonMapper")
	private IMelonMapper melonMapper;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int collectMelonRank() throws Exception {
		log.info(this.getClass().getName() + "collectMelonRank Start!");

		int res = 0;
		List<MelonDTO> pList = new ArrayList<MelonDTO>();
		String url = "https://www.melon.com/chart/index.htm";
		Document doc = null;

		doc = Jsoup.connect(url).get();

		Elements element = doc.select("div.service_list_song");

		Iterator<Element> rank50List = element.select("tr.lst50").iterator();

		while (rank50List.hasNext()) {
			Element songInfo = rank50List.next();
			String rank = songInfo.select("span.rank").text();
			String song = songInfo.select("div.ellipsis a").eq(0).text();
			String singer = songInfo.select("div.ellipsis a").eq(1).text();
			String album = songInfo.select("div.ellipsis a").eq(3).text();

			songInfo = null;

			MelonDTO pDTO = new MelonDTO();

			pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddhhmmss"));
			pDTO.setRank(rank);
			pDTO.setSinger(singer);
			pDTO.setSong(song);
			pDTO.setAlbum(album);

			pList.add(pDTO);
		}
		String colNm = "MelonTOP100_" + DateUtil.getDateTime("yyyyMMdd");
		melonMapper.createCollection(colNm);
		melonMapper.insertRank(pList, colNm);

		log.info(this.getClass().getName() + "collectMelonRank end");

		return res;
	}

	@Override
	public List<MelonDTO> getRank() throws Exception {
		log.info(this.getClass().getName() + "getRank start");

		String colNm = "MelonTOP100_" + DateUtil.getDateTime("yyyyMMdd");

		List<MelonDTO> rList = melonMapper.getRank(colNm);

		if (rList == null) {
			rList = new ArrayList<MelonDTO>();
		}

		log.info(this.getClass().getName() + "getRank end");
		return rList;
	}

	@Override
	public List<MelonSongDTO> getSongForSinger() throws Exception {
		log.info(this.getClass().getName() + " getSongForSinger start");

		String colNm = "MelonTOP100_20200507";
		String singer = "방탄소년단";

		List<MelonSongDTO> rList = melonMapper.getSongForSinger(colNm, singer);

		if (rList == null) {
			rList = new ArrayList<MelonSongDTO>();
		}

		log.info(this.getClass().getName() + " getSongForSinger end");

		return rList;
	}

	@Override
	public List<MelonSingerDTO> getRankForSinger() throws Exception {
		log.info(this.getClass().getName() + " getRankForSinger start");
		// 오늘의 랭킹 수집하기
		this.collectMelonRank();

		// 조회할 컬렉션 이름
		String colNm = "MelonTOP100_" + DateUtil.getDateTime("yyyyMMdd");

		// 가수별 랭킹 가져오기
		List<MelonSingerDTO> rList = melonMapper.getRankForSinger(colNm);

		if (rList == null) {
			rList = new ArrayList<MelonSingerDTO>();
		}

		log.info(this.getClass().getName() + " getRankForSinger end");

		return rList;

	}

}
