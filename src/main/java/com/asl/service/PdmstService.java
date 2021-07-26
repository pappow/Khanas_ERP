package com.asl.service;

import java.util.List;

import com.asl.entity.Pdmst;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
public interface PdmstService {

	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);

	public List<Pdmst> getAll(Boolean zactive);
	
	public List<Pdmst> getAllHRPdmst(Boolean zactive);

	public Pdmst findByXstaff(String xstaff, Boolean zactive);

	public List<Pdmst> searchStaff(String hint);
}
