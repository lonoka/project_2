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
		String[] str = new String[rList.size()];
		String[] writer = new String[rList.size()];
		String[] time = new String[rList.size()];
		for (int i = 0; i < rList.size(); i++) {
			String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
			str[i] = rList.get(i).getTitle().replaceAll(match, "");
			writer[i] = rList.get(i).getWriter();
			time[i] = rList.get(i).getTime().substring(0,13);
		}
		c.assign("pList", str);
		c.assign("writer", writer);
		c.assign("time", time);
		c.eval("writer_count <- table(writer)");
		c.eval("writer_count <- data.frame(writer_count)");
		
		REXP x = c.eval("m_df$noun");
		REXP y = c.eval("m_df$n");
		return " ";

	}
}
