package poly.service;

import java.util.List;

import poly.dto.WeatherDTO;

public interface IWeatherService {
	
	int getWeatherInfoFromWEB() throws Exception;

	List<WeatherDTO> getWeatherInfo(WeatherDTO pDTO) throws Exception;
}
