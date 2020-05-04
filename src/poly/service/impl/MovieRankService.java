package poly.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.dto.MovieDTO;
import poly.persistance.mapper.IMovieMapper;
import poly.persistance.redis.IRedisMovieMapper;
import poly.service.IMovieRankService;
import poly.service.IMovieService;
import poly.util.DateUtil;

@Service("MovieRankService")
public class MovieRankService implements IMovieRankService {

	@Resource(name="MovieService")
	private IMovieService movieService;
	
	@Resource(name="MovieMapper")
	private IMovieMapper mysqlMovieMapper;
	
	@Resource(name="RedisMovieMapper")
	private IRedisMovieMapper redisMovieMapper;
	
	//로그사용
	private Logger log = Logger.getLogger(this.getClass());
	
	
	@Override
	public List<MovieDTO> getMovieRank(MovieDTO pDTO) throws Exception {
		log.info(this.getClass().getName()+".getMovieRank start!");
		
		List<MovieDTO> rList = null;
		
		String key = "CGV_RANK_"+DateUtil.getDateTime("yyyyMMdd");
		
		if(redisMovieMapper.getExists(key)) {
			rList = redisMovieMapper.getMovieRank(key);
			
			if(rList==null) {
				rList = new ArrayList<MovieDTO>();
			}
			
			redisMovieMapper.setTimeOutHour(key, 1);
		} else {
			rList = mysqlMovieMapper.getMovieInfo(pDTO);
			
			if(rList==null) {
				rList = new ArrayList<MovieDTO>();
			}
			
			if(rList.size()==0) {
				movieService.getMovieInfoFromWEB();
				
				rList = mysqlMovieMapper.getMovieInfo(pDTO);
				
				if(rList==null) {
					rList = new ArrayList<MovieDTO>();
				}
			}
			
			if(rList.size()>0) {
				redisMovieMapper.setMovieRank(key, rList);
				redisMovieMapper.setTimeOutHour(key, 1);
			}
		}
		log.info(this.getClass().getName()+".getMovieRank end!");
		return rList;
	}

}
