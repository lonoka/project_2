package poly.service;

import java.util.List;

import poly.dto.CommuDTO;

public interface ICommuService {
	public int collectDcComData() throws Exception;
	
	public List<CommuDTO> getData() throws Exception;
}
