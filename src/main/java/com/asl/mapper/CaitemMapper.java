package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caitem;

@Mapper
public interface CaitemMapper {
	
	public long saveCaitem(Caitem caitem);
	public long updateCaitem(Caitem caitem);

	public List<Caitem> getAllCaitems(String zid);
	public List<Caitem> findByXcatitem(String xcatitem, String zid);
	public Caitem findByXitem(String xitem, String zid);

	public List<Caitem> searchCaitem(String xitem, String zid);
}
