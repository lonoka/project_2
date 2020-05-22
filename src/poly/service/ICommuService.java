package poly.service;

import java.util.List;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;

public interface ICommuService {
	// 크롤링 하기
	public int collectDcComData() throws Exception;

	// 크롤링 데이터 가져오기
	public List<CommuDTO> getData(String colNm) throws Exception;

	// 분석 데이터 가져오기
	public List<DataDTO> getAnalysisData(String colNm) throws Exception;

	// 크롤링 데이터 있는지 없는지 확인
	public int checkCrawlingData() throws Exception;

	// 분석 데이터 있는지 없는지 확인
	public int checkAnalysisData() throws Exception;

	// 데이터 분석
	public int AnalysisData() throws Exception;

}
