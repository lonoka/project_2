package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Controller
public class RController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "CommuService")
	private ICommuService commuService;

	@RequestMapping(value = "rtest")
	@ResponseBody
	public String rtest(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		RConnection c = new RConnection();
		String a = "이것은";
		String b = "정말";
		String d = "귀찮타";
		c.assign("pList", a);
		REXP x = c.eval("pList");
		System.out.println(x.asString());
		return x.asString();

	}

	@RequestMapping(value = "rtest2")
	@ResponseBody
	public String rtest2(HttpServletRequest request, Model model, HttpSession session) throws Exception {

		log.info(this.getClass().getName() + " rtest2 start!");

		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<CommuDTO> rList = commuService.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		log.info(rList.size());

		RConnection c = new RConnection();
		String[] title = new String[rList.size()];
		for (int i = 0; i < rList.size(); i++) {
			String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
			title[i] = rList.get(i).getTitle().replaceAll(match, "");
		}
		
		c.assign("title", title);
		c.eval("negative <- readLines('c:\\\\word\\\\negative.txt', encoding = 'UTF-8')");
		c.eval("positive <- readLines('c:\\\\word\\\\positive.txt', encoding = 'UTF-8')");
		c.eval("m_df <- title %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
		c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit %>% count(noun, sort = TRUE)");
		c.eval("wordList <- m_df$noun");
		c.eval("m_df <- filter(m_df,nchar(noun)>=2)");
		c.eval("m_df <- filter(m_df,n>=2)");
		c.eval("wordList = unlist(wordList)");
		c.eval("posM = match(wordList, positive)");
		c.eval("posM = !is.na(posM)");
		c.eval("negM = match(wordList, negative)");
		c.eval("negM = !is.na(negM)");
		
		REXP y = c.eval("sum(posM)");
		log.info(y.asString());
		REXP z = c.eval("sum(negM)");
		log.info(z.asString());
		return " ";

	}
}
