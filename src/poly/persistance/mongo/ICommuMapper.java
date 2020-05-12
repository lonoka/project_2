package poly.persistance.mongo;

import java.util.List;

import poly.dto.CommuDTO;

public interface ICommuMapper {
	/**
	 * MongoDB 컬랙션 생성하기
	 * 
	 * @param colNm 생성하는 컬렉션 이름
	 */
	public boolean createCollection(String colNm) throws Exception;
	
	/**
	 * MongoDB 데이터 저장하기
	 * 
	 * @param pDTO 저장될 정보
	 */
	public int insertData(List<CommuDTO> pList, String colNm) throws Exception;
	
	/**
	 * MongoDB 멜론 데이터 가져오기
	 * 
	 * @param colNm 가져올 컬랙션 이름
	 */
	public List<CommuDTO> getData(String colNm) throws Exception;
}

