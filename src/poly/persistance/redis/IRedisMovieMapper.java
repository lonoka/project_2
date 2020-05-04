package poly.persistance.redis;

import java.util.List;

import poly.dto.MovieDTO;

public interface IRedisMovieMapper {

	// 순위정보 존재하는지 확인
	public boolean getExists(String key) throws Exception;

	// 순위정보 가져오기
	public List<MovieDTO> getMovieRank(String key) throws Exception;

	// 순위정보 저장
	public int setMovieRank(String key, List<MovieDTO> pList) throws Exception;

	// 순위정보 저장 ttl설정
	public boolean setTimeOutHour(String key, int hours) throws Exception;
}
