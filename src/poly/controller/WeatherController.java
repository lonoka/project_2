package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.MovieDTO;
import poly.dto.WeatherDTO;
import poly.service.IWeatherService;
import poly.util.CmmUtil;
import poly.util.DateUtil;

@Controller
public class WeatherController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "WeatherService")
	private IWeatherService weatherService;

	// 첫 화면
	@RequestMapping(value = "weather/index")
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info(this.getClass().getName() + ".index start!");
		return "/weather/index";
	}

	// 영화순위 가져오기
	@RequestMapping(value = "weather/info")
	@ResponseBody
	public List<WeatherDTO> weather(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info(this.getClass().getName() + ".weather start!");

		List<WeatherDTO> rList = null;

		String send_msg = CmmUtil.nvl(request.getParameter("send_msg"));

		if (((send_msg.indexOf("날씨") > -1) || (send_msg.indexOf("날시") > -1) || (send_msg.indexOf("널씨") > -1)
				|| (send_msg.indexOf("널시") > -1))) {
			WeatherDTO pDTO = new WeatherDTO();
			pDTO.setWeather_check_time(DateUtil.getDateTime("yyyyMMdd"));
			rList = weatherService.getWeatherInfo(pDTO);

			if (rList == null) {
				rList = new ArrayList<WeatherDTO>();
			}
		}

		log.info(this.getClass().getName() + ".weather end!");
		return rList;
	}
}
