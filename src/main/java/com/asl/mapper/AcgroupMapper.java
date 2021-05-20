package com.asl.mapper;

import java.util.List;

import com.asl.entity.Acdetail;

public interface AcgroupMapper {
	
	public long saveAcdetail(Acdetail acdetail);
	public long updateAcdetail(Acdetail acdetail);
	
	public List<Acdetail> getAllAcdetail(String zid);

}
