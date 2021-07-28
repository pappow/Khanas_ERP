package com.asl.service;

import java.util.List;

import com.asl.entity.Pdexperience;
import com.asl.entity.Pdmst;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
public interface PdmstService {

	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);
	
	public long delete(Pdmst pdmst);

	public List<Pdmst> getAll(Boolean zactive);
	
	public List<Pdmst> getAllHRPdmst();
	
	public Pdmst findAllPdmst(String xstaff);

	public Pdmst findByXstaff(String xstaff, Boolean zactive);

	public List<Pdmst> searchStaff(String hint);
	
	//for HRExperience
	public long savePdexperience(Pdexperience pdex);
	
	public long updatePdexperience(Pdexperience pdex);
	
	public long deletePdexperience(Pdexperience pdex);
	
	public List<Pdexperience> getAllPdexperience();
	
	public List<Pdexperience> findByPdexperience(String xstaff);
	
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow);
}
