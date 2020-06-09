package poly.service;

import poly.dto.UserDTO;

public interface IUserService {

	int insertUserInfo(UserDTO pDTO) throws Exception;

	int ckeckId(UserDTO pDTO) throws Exception;

	int ckeckMail(UserDTO pDTO) throws Exception;

	UserDTO getLogin(UserDTO uDTO) throws Exception;

	UserDTO getIdInfo(UserDTO pDTO) throws Exception;

	UserDTO getPwInfo(UserDTO pDTO) throws Exception;

	int updatePwInfo(UserDTO pDTO) throws Exception;

	int updateUserInfo(UserDTO pDTO) throws Exception;

	UserDTO getUserInfo(UserDTO pDTO) throws Exception;

}
