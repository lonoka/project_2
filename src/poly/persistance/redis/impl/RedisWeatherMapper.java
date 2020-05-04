package poly.persistance.redis.impl;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import poly.dto.MovieDTO;
import poly.dto.WeatherDTO;
import poly.persistance.redis.IRedisWeatherMapper;

@Component("RedisWeatherMapper")
public class RedisWeatherMapper implements IRedisWeatherMapper {
	
	@Autowired
	public RedisTemplate<String, Object> redisDB;
	

	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public boolean getExists(String key) throws Exception {
		log.info(this.getClass().getName() + ".getExists Start!");
		return redisDB.hasKey(key);
	}

	@Override
	public List<WeatherDTO> getWeatherInfo(String key) throws Exception {
		log.info(this.getClass().getName() + ".getWeatherInfo Start!");

		List<WeatherDTO> rList = null;

		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(WeatherDTO.class));

		if (redisDB.hasKey(key)) {
			rList = (List) redisDB.opsForList().range(key, 0, -1);
		}

		log.info(this.getClass().getName() + ".getWeatherInfo End!");

		return rList;
	}

	@Override
	public int setWeatherInfo(String key, List<WeatherDTO> pList) throws Exception {
		log.info(this.getClass().getName() + ".setWeatherInfo Start!");

		int res = 0;

		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(MovieDTO.class));

		if (this.getExists(key)) {
			redisDB.delete(key);
		}

		Iterator<WeatherDTO> it = pList.iterator();

		while (it.hasNext()) {
			WeatherDTO pDTO = (WeatherDTO) it.next();
			redisDB.opsForList().rightPush(key, pDTO);
			pDTO = null;
		}
		res = 1;
		
		log.info(this.getClass().getName() + ".setWeatherInfo End!");
		
		return res;
	}

	@Override
	public boolean setTimeOutHour(String key, int hours) throws Exception {
		log.info(this.getClass().getName() + ".setTimeOutHour Start!");
		return redisDB.expire(key, hours, TimeUnit.HOURS);
	}

}
