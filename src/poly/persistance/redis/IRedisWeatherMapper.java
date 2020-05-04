package poly.persistance.redis;

import java.util.List;

import poly.dto.WeatherDTO;

public interface IRedisWeatherMapper {
	// 날씨정보 존재하는지 확인
	public boolean getExists(String key) throws Exception;

	// 날씨정보 가져오기
	public List<WeatherDTO> getWeatherInfo(String key) throws Exception;

	// 날씨정보 저장
	public int setWeatherInfo(String key, List<WeatherDTO> pList) throws Exception;

	// 날씨정보 저장 ttl설정
	public boolean setTimeOutHour(String key, int hours) throws Exception;
}
