package poly.service;

import java.util.List;

import poly.dto.MovieDTO;

public interface IMovieRankService {
	//영화순위정보 가져오기
	List<MovieDTO> getMovieRank(MovieDTO pDTO) throws Exception;
}
