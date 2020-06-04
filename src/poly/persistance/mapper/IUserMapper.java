package poly.persistance.mapper;

import config.Mapper;
import poly.dto.UserDTO;

@Mapper("UserMapper")
public interface IUserMapper {

	int insertUserInfo(UserDTO pDTO) throws Exception;

	int ckeckId(UserDTO pDTO) throws Exception;

	int ckeckMail(UserDTO pDTO) throws Exception;

	UserDTO getLogin(UserDTO uDTO) throws Exception;

}
