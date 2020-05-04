package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.service.IMovieRankService;
import poly.service.IWeatherService;

@Controller
public class MainController {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "WeatherService")
	private IWeatherService weatherService;
	
	// TTS페이지
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		weatherService.getWeatherInfoFromWEB();
		return "/index";

	}

	// TTS페이지
	@RequestMapping(value = "TTS")
	public String TTS(HttpServletRequest request, Model model, HttpSession session) {

		return "/TTS";

	}
}
