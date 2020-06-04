package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.UserDTO;
import poly.service.IUserService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

@Controller
public class UserController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "UserService")
	private IUserService userService;

	/*
	 * 회원가입 로직 처리
	 */
	@RequestMapping(value = "user/insertUserInfo", method = RequestMethod.POST)
	public String insertUserInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		log.info(this.getClass().getName() + "insertUserInfo Start");

		// 회원가입 결과 메시지 전달 변수
		String msg = "";

		// 웹에서 받는 정보를 저장할 변수
		UserDTO pDTO = null;

		try {
			/*
			 * 웹 에서 받는 정보를 String 변수에 저장 시작 무조건 웹으로 받는 정보는 DTO에 저장하기 위해 임시로 String에 저장
			 */
			String user_id = CmmUtil.nvl(request.getParameter("user_id"));
			String user_name = CmmUtil.nvl(request.getParameter("user_name"));
			String password = CmmUtil.nvl(request.getParameter("password"));
			String user_mail = CmmUtil.nvl(request.getParameter("user_mail"));
			String user_date = CmmUtil.nvl(request.getParameter("user_date"));
			String user_gen = CmmUtil.nvl(request.getParameter("user_gen"));
			/*
			 * 웹 에서 받는 정보를 String 변수에 저장 끝 무조건 웹으로 받는 정보는 DTO에 저장하기 위해 임시로 String에 저장
			 */

			/*
			 * 값을 받는 경우 반드시 로그를 찍어서 값이 들어오는지 확인
			 */
			log.info("user_id : " + user_id);
			log.info("user_name : " + user_name);
			log.info("password : " + password);
			log.info("user_mail : " + user_mail);
			log.info("user_date : " + user_date);
			log.info("user_gen : " + user_gen);

			/*
			 * 웹에서 받는 정보를 DTO에 저장하기 시작 무조건 웹으로 받는 정보는 DTO에 저장
			 */
			// 받는 정보를 저장할 변수를 메모리에 올리기
			pDTO = new UserDTO();
			pDTO.setUser_id(user_id);
			pDTO.setUser_name(user_name);

			// 비밀번호는 절대적으로 복호화되지 않도록 해시 알고리즘으로 암호화함
			pDTO.setPassword(EncryptUtil.encHashSHA256(password));

			// 민감 정보인 이메일은 AES128-CBC로 암호화함
			pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));
			/*
			 * 웹에서 받는 정보를 DTO에 저장하기 끝 무조건 웹으로 받는 정보는 DTO에 저장
			 */

			// 회원가입
			int res = userService.insertUserInfo(pDTO);

			if (res == 1) {
				msg = "회원가입되었습니다.";
			} else if (res == 2) {
				msg = "이미 가입된 회원입니다.";
			} else {
				msg = "오류로 인해 회원가입이 실패하였습니다.";
			}
		} catch (Exception e) {
			// 실패시 사용자에게 보여줄 메시지
			msg = "실패하였습니다. : " + e.toString();
			log.info(e.toString());
			e.printStackTrace();
		} finally {
			log.info(this.getClass().getName() + "insertUserInfo end");

			// 회원가입 여부 결과 메시지 전달
			model.addAttribute("msg", msg);
			model.addAttribute("url", "/index.do");

			// 변수 초기화(메모리 효율화 시키기 위해 사용)
			pDTO = null;
		}
		return "/redirect";
	}

	// ID 체크
	@RequestMapping(value = "checkId")
	@ResponseBody
	public  int checkId(HttpServletRequest request) throws Exception {

		String user_id = request.getParameter("user_id");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);

		return userService.ckeckId(pDTO);
	}

	// Email 체크
	@RequestMapping(value = "checkMail")
	@ResponseBody
	public  int checkMail(HttpServletRequest request) throws Exception {

		String user_mail = request.getParameter("user_mail");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		return userService.ckeckMail(pDTO);
	}

	// 로그인 버튼
	@RequestMapping(value = "Loginbtn", method = RequestMethod.POST)
	public String Loginbtn(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
		String userId = request.getParameter("userId");
		String userPassword = request.getParameter("userPassword");

		UserDTO uDTO = new UserDTO();
		uDTO.setUser_id(userId);
		uDTO.setPassword(EncryptUtil.encHashSHA256(userPassword));
		uDTO = userService.getLogin(uDTO);

		if (uDTO == null) {
			model.addAttribute("msg", "없는 아이디 또는 잘못된 비밀번호 입니다.");
			model.addAttribute("url", "/index.do");
		}  else if (uDTO.getUser_author().equals("1")) {
			model.addAttribute("msg", "관리자 로그인에 성공하셨습니다.");
			model.addAttribute("url", "/index.do");
			session.setAttribute("userAuthor", uDTO.getUser_author());
			session.setAttribute("userId", uDTO.getUser_id());
			session.setAttribute("userName", uDTO.getUser_name());
		} else {
			model.addAttribute("msg", "로그인에 성공하셨습니다.");
			model.addAttribute("url", "/index.do");
			session.setAttribute("userAuthor", uDTO.getUser_author());
			session.setAttribute("userId", uDTO.getUser_id());
			session.setAttribute("userName", uDTO.getUser_name());
		}

		return "/redirect";
	}

	// 로그아웃 버튼
	@RequestMapping(value = "Logoutbtn")
	public String Logoutbtn(HttpSession session, ModelMap model) throws Exception {
		session.invalidate();

		model.addAttribute("msg", "로그아웃 하셨습니다.");
		model.addAttribute("url", "/index.do");
		return "/redirect";
	}
}
