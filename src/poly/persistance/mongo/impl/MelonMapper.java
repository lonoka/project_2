package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.MelonDTO;
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

}
