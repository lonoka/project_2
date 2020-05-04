package poly.persistance.mapper;

import java.util.List;

import config.Mapper;
import poly.dto.WeatherDTO;

@Mapper("WeatherMapper")
public interface IWeatherMapper {

	// 수집된 내용 DB에 등록
	int InsertWeatherInfo(WeatherDTO pDTO) throws Exception;

	// 수집된 데이터 조회
	List<WeatherDTO> getWeatherInfo(WeatherDTO pDTO) throws Exception;

}
