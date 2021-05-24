package com.asl.mapper;

import java.util.List;

import com.asl.entity.Acgroup;

public interface AcgroupMapper {
	
	public long saveAcgroup(Acgroup acgroup);
	public long updateAcgroup(Acgroup acgroup);
	
	public List<Acgroup> getAllAcgroup(String zid);

}
