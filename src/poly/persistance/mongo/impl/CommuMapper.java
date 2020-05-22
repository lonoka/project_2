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

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.dto.MelonDTO;
import poly.persistance.mongo.ICommuMapper;
import poly.util.CmmUtil;

@Component("CommuMapper")
public class CommuMapper implements ICommuMapper {

	@Autowired
	private MongoTemplate mongodb;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean createCollection(String colNm) throws Exception {
		log.info(this.getClass().getName() + " createCollection Start!");
		boolean res = false;

		// 기존 콜렉션 체크, 있는경우 삭제
		if (mongodb.collectionExists(colNm)) {
			mongodb.dropCollection(colNm);
		}

		// 컬렉션 및 인덱스 생성, MongoDB에서 데이터 가져오는 방식에 맞게 인덱스는 반드시 생성
		// 데이터 양이 많지 않으면 문제가 되지 않으나, 최소 10만건 이상 데이터 저장시 속도차이 10배 이상
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("collect_time", 1).append("commu_name", 1),
				"commuIdx");

		res = true;

		log.info(this.getClass().getName() + " createCollection end!");

		return res;
	}

	@Override
	public int insertData(List<CommuDTO> pList, String colNm) throws Exception {
		log.info(this.getClass().getName() + " insertData Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<CommuDTO>();
		}

		Iterator<CommuDTO> it = pList.iterator();

		while (it.hasNext()) {
			CommuDTO pDTO = (CommuDTO) it.next();

			if (pDTO == null) {
				pDTO = new CommuDTO();
			}

			mongodb.insert(pDTO, colNm);

		}

		res = 1;

		log.info(this.getClass().getName() + " insertData end!");

		return res;
	}

	@Override
	public List<CommuDTO> getData(String colNm) throws Exception {
		log.info(this.getClass().getName() + " getData Start!");
		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<CommuDTO> rList = new ArrayList<CommuDTO>();

		CommuDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new CommuDTO();
			final DBObject current = cursor.next();

			String collect_time = CmmUtil.nvl((String) current.get("collect_time"));
			String commu_name = CmmUtil.nvl((String) current.get("commu_name"));
			String time = CmmUtil.nvl((String) current.get("time"));
			String title = CmmUtil.nvl((String) current.get("title"));
			String writer = CmmUtil.nvl((String) current.get("writer"));

			rDTO.setCollect_time(collect_time);
			rDTO.setCommu_name(commu_name);
			rDTO.setTime(time);
			rDTO.setTitle(title);
			rDTO.setWriter(writer);

			rList.add(rDTO);

			rDTO = null;

		}

		log.info(this.getClass().getName() + " getData end!");

		return rList;
	}

	@Override
	public int insertAnalysisData(ArrayList<DataDTO> pList, String colNm) {
		log.info(this.getClass().getName() + " insertAnalysisData Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<DataDTO>();
		}

		Iterator<DataDTO> it = pList.iterator();

		while (it.hasNext()) {
			DataDTO pDTO = (DataDTO) it.next();

			if (pDTO == null) {
				pDTO = new DataDTO();
			}

			mongodb.insert(pDTO, colNm);

		}

		res = 1;

		log.info(this.getClass().getName() + " insertAnalysisData end!");

		return res;
		
	}

	@Override
	public List<DataDTO> getAnalysisData(String colNm) throws Exception {
		log.info(this.getClass().getName() + " getAnalysisData Start!");
		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<DataDTO> rList = new ArrayList<DataDTO>();

		DataDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new DataDTO();
			final DBObject current = cursor.next();

			String analysis_time = CmmUtil.nvl((String) current.get("analysis_time"));
			String commu_name = CmmUtil.nvl((String) current.get("commu_name"));
			String word = CmmUtil.nvl((String) current.get("word"));
			String count = CmmUtil.nvl((String) current.get("count"));

			rDTO.setAnalysis_time(analysis_time);
			rDTO.setCommu_name(commu_name);
			rDTO.setWord(word);
			rDTO.setCount(count);

			rList.add(rDTO);

			rDTO = null;

		}

		log.info(this.getClass().getName() + " getAnalysisData end!");

		return rList;
	}

}