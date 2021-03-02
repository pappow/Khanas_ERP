package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Caitem;

@Component
public interface CaitemService {
	
	public long save(Caitem caitem);
	public long update(Caitem caitem);
	public List<Caitem> getAllCaitems();
	//public List<Xcodes> findByXtype(String xType);
	//public List<Xcodes> findByXcode(String xCode);
	public List<Caitem> findByXcatitem(String xcatitem);
	public Caitem findByXitem(String xitem);
	//public Xcodes findByXtypesAndXcodes(String xType, String xCodes);
	public List<Caitem> searchCaitem(String hint);

}