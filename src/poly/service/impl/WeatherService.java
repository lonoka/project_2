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

import poly.dto.MovieDTO;
import poly.dto.WeatherDTO;
import poly.persistance.mapper.IWeatherMapper;
import poly.persistance.redis.IRedisWeatherMapper;
import poly.service.IWeatherService;
import poly.util.CmmUtil;
import poly.util.DateUtil;

@Service("WeatherService")
public class WeatherService implements IWeatherService {
	// 로그사용
	private Logger log = Logger.getLogger(this.getClass());

	// mysql 접속 위한 맵퍼
	@Resource(name = "WeatherMapper")
	private IWeatherMapper mysqlWeatherMapper;

	@Resource(name = "WeatherService")
	private IWeatherService weatherService;

	@Resource(name = "RedisWeatherMapper")
	private IRedisWeatherMapper redisWeatherMapper;

	@Override
	public List<WeatherDTO> getWeatherInfo(WeatherDTO pDTO) throws Exception {
		log.info(this.getClass().getName() + ".getWeatherInfo start!");

		List<WeatherDTO> rList = null;

		String key = "WEATHER_INFO_" + DateUtil.getDateTime("yyyyMMdd");

		if (redisWeatherMapper.getExists(key)) {
			rList = redisWeatherMapper.getWeatherInfo(key);

			if (rList == null) {
				rList = new ArrayList<WeatherDTO>();
			}

			redisWeatherMapper.setTimeOutHour(key, 1);
		} else {
			rList = mysqlWeatherMapper.getWeatherInfo(pDTO);

			if (rList == null) {
				rList = new ArrayList<WeatherDTO>();
			}

			if (rList.size() == 0) {
				weatherService.getWeatherInfoFromWEB();

				rList = mysqlWeatherMapper.getWeatherInfo(pDTO);

				if (rList == null) {
					rList = new ArrayList<WeatherDTO>();
				}
			}

			if (rList.size() > 0) {
				redisWeatherMapper.setWeatherInfo(key, rList);
				redisWeatherMapper.setTimeOutHour(key, 1);
			}
		}
		log.info(this.getClass().getName() + ".getWeatherInfo end!");
		return rList;
	}

	// 크롤링 하고 정보 넣기
	@Override
	public int getWeatherInfoFromWEB() throws Exception {
		log.info(this.getClass().getName() + ".getWeatherInfoFromWEB start!");
		int res = 0;
		String url = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EB%82%A0%EC%94%A8";
		Document doc = null;
		doc = Jsoup.connect(url).get();
		Elements element = doc.select("div._weeklyWeather");

		Iterator<Element> day_info = element.select("span.day_info").iterator();
		Iterator<Element> morning_rain = element.select("span.morning span.rain_rate span.num").iterator();
		Iterator<Element> afternoon_rain = element.select("span.afternoon span.rain_rate span.num").iterator();
		Iterator<Element> high_temp = element.select("dl dd span:first-child").iterator();
		Iterator<Element> low_temp = element.select("dl dd span:nth-child(3)").iterator();

		WeatherDTO pDTO = null;

		while (day_info.hasNext()) {
			pDTO = new WeatherDTO();
			pDTO.setWeather_check_time(DateUtil.getDateTime("yyyyMMdd"));
			pDTO.setDay_info(CmmUtil.nvl(day_info.next().text()).trim());
			pDTO.setMorning_rain(CmmUtil.nvl(morning_rain.next().text()).trim());
			pDTO.setAfternoon_rain(CmmUtil.nvl(afternoon_rain.next().text()).trim());
			pDTO.setHigh_temp(CmmUtil.nvl(high_temp.next().text()).trim());
			pDTO.setLow_temp(CmmUtil.nvl(low_temp.next().text()).trim());
			pDTO.setReg_id("admin");
			res += mysqlWeatherMapper.InsertWeatherInfo(pDTO);
		}
		log.info(this.getClass().getName() + ".getWeatherInfoFromWEB end!");

		return res;
	}

}
