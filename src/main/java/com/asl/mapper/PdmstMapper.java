package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Pdmst;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface PdmstMapper {
	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);

	public Pdmst findPdmstByXstaff(String xstaff, String zid, Boolean zactive);

	public List<Pdmst> getAllPdmst(String zid, Boolean zactive);
	
	public List<Pdmst> getAllHRPdmst(String zid, Boolean zactive);
	
	public List<Pdmst> searchStaff(String xstaff, String zid);
}
