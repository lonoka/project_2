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

import poly.dto.MailDTO;
import poly.dto.UserDTO;
import poly.service.IMailService;
import poly.service.IUserService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

@Controller
public class UserController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "MailService")
	private IMailService mailService;

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
	public int checkId(HttpServletRequest request) throws Exception {

		String user_id = request.getParameter("user_id");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);

		return userService.ckeckId(pDTO);
	}

	// Email 체크
	@RequestMapping(value = "checkMail")
	@ResponseBody
	public int checkMail(HttpServletRequest request) throws Exception {

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
		} else if (uDTO.getUser_author().equals("1")) {
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
		log.info(uDTO.getUser_id());
		log.info(uDTO.getUser_mail());
		log.info(uDTO.getUser_name());
		session.setAttribute("uDTO",uDTO);

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

	// ID찾기
	@RequestMapping(value = "findID", method = RequestMethod.POST)
	public String findId(HttpServletRequest request, ModelMap model) throws Exception {
		String user_name = request.getParameter("user_name");// userName 은 DTO와 같게 지정 parameter값은 jsp의 name값이랑 같게 지정
		String user_mail = request.getParameter("user_mail");

		UserDTO pDTO = new UserDTO(); // 보내는 통

		pDTO.setUser_name(user_name);
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		pDTO = userService.getIdInfo(pDTO);

		if (pDTO == null) {
			model.addAttribute("msg", "가입된 아이디가 없습니다.");
			model.addAttribute("url", "/index.do");
		} else {
			model.addAttribute("msg", "가입된 아이디는 " + pDTO.getUser_id() + " 입니다.");
			model.addAttribute("url", "/index.do");
		}

		return "/redirect";
	}

	// PW찾기 버튼 기능/
	@RequestMapping(value = "findPW", method = RequestMethod.POST)
	public String findPW(HttpServletRequest request, ModelMap model) throws Exception {
		String user_name = request.getParameter("user_name");
		String user_id = request.getParameter("user_id");
		String user_mail = request.getParameter("user_mail");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);
		pDTO.setUser_name(user_name);
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		pDTO = userService.getPwInfo(pDTO);

		if (pDTO == null) {
			model.addAttribute("msg", "회원정보가 존재하지 않습니다.");
			model.addAttribute("url", "/userLogin.do");
			return "/redirect";
		} else {

			// 10글자 짜리 비밀번호가 NewPw에 들어감
			String NewPw = getNewPw();
			pDTO.setUser_id(user_id);
			pDTO.setPassword(EncryptUtil.encHashSHA256(NewPw));
			int result = 0;
			try {
				result = userService.updatePwInfo(pDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String title = "새로운 비밀번호";
			String contents = "새로운 비밀번호는 " + NewPw + "입니다.";

			// 메일 발송할 정보를 넣을 DTO객체 생성
			MailDTO mDTO = new MailDTO();

			mDTO.setToMail(user_mail); // 받는 사람 정보 DTO에 저장
			mDTO.setTitle(title); // 제목을 DTO에 저장
			mDTO.setContents(contents); // 내용을 DTO에 저장

			// 메일 발송하기
			int res = mailService.doSendMail(mDTO);

			if (res == 1) {
				// 발송 성공 로그 찍기
				log.info(this.getClass().getName() + "mail.sendMail success!!!");
			} else {
				// 발송 실패 로그 찍기
				log.info(this.getClass().getName() + "mail.sendMail fail!!!");
			}

			model.addAttribute("msg", "새로운 비밀번호가 이메일로 발송되었습니다..");
			model.addAttribute("url", "/index.do");
			return "/redirect";
		}

	}

	// 고객문의 메일 발송
	@RequestMapping(value = "contactSend", method = RequestMethod.POST)
	public String contactSend(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		// 로그 찍기
		log.info(this.getClass().getName() + "mail.contactSend start!");

		String contactName = CmmUtil.nvl(request.getParameter("contactName"));
		String contactTel = CmmUtil.nvl(request.getParameter("contactTel"));
		String contactEmail = CmmUtil.nvl(request.getParameter("contactEmail"));
		String contactMessage = CmmUtil.nvl(request.getParameter("contactMessage"));

		// 메일 발송할 정보를 넣을 DTO객체 생성
		MailDTO pDTO = new MailDTO();

		pDTO.setContactName(contactName);
		pDTO.setContactTel(contactTel);
		pDTO.setContactEmail(contactEmail);
		pDTO.setContactMessage(contactMessage);

		// 메일 발송하기
		int res = mailService.doSendContact(pDTO);

		if (res == 1) {
			// 발송 성공 로그 찍기
			log.info(this.getClass().getName() + "mail.contactSend success!!!");
			model.addAttribute("msg", "문의메일이 발송됬습니다.");
			model.addAttribute("url", "/index.do");
		} else {
			// 발송 실패 로그 찍기
			log.info(this.getClass().getName() + "mail.contactSend fail!!!");
			model.addAttribute("msg", "일시적 오류로 문의메일 발송에 실패했습니다. 나중에 다시 시도해주세요.");
			model.addAttribute("url", "/index.do");
		}
		// 로그 찍기
		log.info(this.getClass().getName() + "mail.contactSend end!");

		return "/redirect";
	}

	public String getNewPw() throws Exception {
		// 비밀번호 배열을 생성
		char[] charSet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		// 비밀번호를 받기 위한 문자열 버퍼 생성
		// 비밀번호를 담는 틀 생성
		StringBuffer newKey = new StringBuffer();

		// 10번 반복
		for (int i = 0; i < 10; i++) {
			// 비밀번호 배열 길이*랜덤으로 생성된 숫자
			// random() 난수가 복잡하지 않기 떄문에 숫자를 더 복잡하게 해줌
			int idx = (int) (charSet.length * Math.random());
			// 문자열에다가 한글자씩 담는것
			newKey.append(charSet[idx]);
		}

		// 스트링 버퍼를 스트링형태로 바꿔서 반환해줌
		return newKey.toString();
	}
}
