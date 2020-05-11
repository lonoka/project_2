package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.MelonDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;
import poly.persistance.mongo.IMelonMapper;
import poly.util.CmmUtil;

@Component("MelonMapper")
public class MelonMapper implements IMelonMapper {

	@Autowired
	private MongoTemplate mongodb;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean createCollection(String colNm) throws Exception {
		log.info(this.getClass().getName() + "createCollection Start!");

		boolean res = false;

		// 기존 콜렉션 체크, 있는경우 삭제
		if (mongodb.collectionExists(colNm)) {
			mongodb.dropCollection(colNm);
		}

		// 컬렉션 및 인덱스 생성, MongoDB에서 데이터 가져오는 방식에 맞게 인덱스는 반드시 생성
		// 데이터 양이 많지 않으면 문제가 되지 않으나, 최소 10만건 이상 데이터 저장시 속도차이 10배 이상
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("collect_time", 1).append("rank", 1), "testIdx");

		res = true;

		log.info(this.getClass().getName() + "createCollection End!");

		return res;
	}

	@Override
	public int insertRank(List<MelonDTO> pList, String colNm) throws Exception {
		log.info(this.getClass().getName() + ".insertRank Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<MelonDTO>();
		}

		Iterator<MelonDTO> it = pList.iterator();

		while (it.hasNext()) {
			MelonDTO pDTO = (MelonDTO) it.next();

			if (pDTO == null) {
				pDTO = new MelonDTO();
			}

			mongodb.insert(pDTO, colNm);

		}

		res = 1;

		log.info(this.getClass().getName() + ".insertRank End!");

		return res;
	}

	@Override
	public List<MelonDTO> getRank(String colNm) throws Exception {
		log.info(this.getClass().getName() + "getRank start!");

		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<MelonDTO> rList = new ArrayList<MelonDTO>();

		MelonDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new MelonDTO();
			final DBObject current = cursor.next();

			String collect_time = CmmUtil.nvl((String) current.get("collect_time"));
			String rank = CmmUtil.nvl((String) current.get("rank"));
			String song = CmmUtil.nvl((String) current.get("song"));
			String singer = CmmUtil.nvl((String) current.get("singer"));
			String album = CmmUtil.nvl((String) current.get("album"));

			rDTO.setCollect_time(collect_time);
			rDTO.setRank(rank);
			rDTO.setSinger(singer);
			rDTO.setSong(song);
			rDTO.setAlbum(album);

			rList.add(rDTO);

			rDTO = null;
		}
		log.info(this.getClass().getName() + "getRank end!");

		return rList;
	}

	@Override
	public List<MelonSongDTO> getSongForSinger(String colNm, String singer) throws Exception {
		log.info(this.getClass().getName() + " getSongForSinger start!");

		// 데이터를 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("singer", singer);

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<MelonSongDTO> rList = new ArrayList<MelonSongDTO>();

		// 데이터를 rList에 저장하기 위한 DTO변수
		MelonSongDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new MelonSongDTO();

			final DBObject current = cursor.next();

			String rank = CmmUtil.nvl((String) current.get("rank"));
			String song = CmmUtil.nvl((String) current.get("song"));

			log.info("song : " + song);

			rDTO.setRank(rank);
			rDTO.setSong(song);

			rList.add(rDTO);

			rDTO = null;
		}

		log.info(this.getClass().getName() + " getSongForSinger end!");

		return rList;
	}

	@Override
	public List<MelonSingerDTO> getRankForSinger(String colNm) throws Exception {
		log.info(this.getClass().getName() + " getRankForSinger start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		List<DBObject> pipeline = Arrays.asList(
				// SQL의 Group by 와 같은 역할을 하는 group함수 호출
				new BasicDBObject().append("$group",
						new BasicDBObject().append("_id", new BasicDBObject().append("singer", "$singer"))
								// 그룹으로 묶인 함수를 통해 계산될 내용
								.append("COUNT(singer)", new BasicDBObject().append("$sum", 1))),
				// project는 결과를 보여줄 내용, sql의 select와 from절 사이 내용
				new BasicDBObject().append("$project",
						new BasicDBObject().append("singer", "$_id.singer").append("song_cnt", "$COUNT(singer)")
								.append("_id", 0)),
				// SQL의 order by 역할을 하는 sort 함수 호출
				// singer
				new BasicDBObject().append("$sort", new BasicDBObject().append("song_cnt", -1).append("singer", 1)));

		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();

		// 쿼리 실행하기
		Cursor cursor = rCol.aggregate(pipeline, options);

		// 컬랙션으로부터 가져온 데이터를 List형태로 저장하기 위한 변수 선언
		List<MelonSingerDTO> rList = new ArrayList<MelonSingerDTO>();

		// 데이터를 rList에 저장하기 위한 DTO변수
		MelonSingerDTO rDTO = null;

		int rank = 1; // 랭킹변수

		while (cursor.hasNext()) {
			rDTO = new MelonSingerDTO();

			final DBObject current = cursor.next();

			String singer = CmmUtil.nvl((String) current.get("singer"));
			int song_cnt = (int) current.get("song_cnt");

			rDTO.setRank(rank);
			rDTO.setSinger(singer);
			rDTO.setSong_cnt(song_cnt);

			rList.add(rDTO);

			rDTO = null;

			rank++;
		}

		log.info(this.getClass().getName() + " getRankForSinger end!");

		return rList;
	}

}
