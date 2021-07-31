package com.asl.service;

import java.util.List;

import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
import com.asl.entity.Pdtransdt;

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
	
	//for HRQualification
	public long savePdeducation(Pdeducation pdqua);
	
	public long updatePdeducation(Pdeducation pdqua);
	
	public long deletePdeducation(Pdeducation pdqua);
	
	public List<Pdeducation> getAllPdeducation();
	
	public List<Pdeducation> findByPdeducation(String xstaff);
	
	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow);
	
	//for HRExperience
	public long savePdexperience(Pdexperience pdex);
	
	public long updatePdexperience(Pdexperience pdex);
	
	public long deletePdexperience(Pdexperience pdex);
	
	public List<Pdexperience> getAllPdexperience();
	
	public List<Pdexperience> findByPdexperience(String xstaff);
	
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow);
	
	//for HRPromotion
	public long savePdpromodt(Pdpromodt pdpr);
	
	public long updatePdpromodt(Pdpromodt pdpr);
	
	public long deletePdpromodt(Pdpromodt pdpr);
	
	public List<Pdpromodt> getAllPdpromodt();
	
	public List<Pdpromodt> findByPdpromodt(String xstaff);
	
	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow);
	
	//for HRTransfer
	public long savePdtransdt(Pdtransdt pdtr);
	
	public long updatePdtransdt(Pdtransdt pdtr);
	
	public long deletePdtransdt(Pdtransdt pdtr);
	
	public List<Pdtransdt> getAllPdtransdt(String zid);
	
	public List<Pdtransdt> findByPdtransdt(String xstaff);
	
	public Pdtransdt findPdtransdtByXstaffAndXrow(String xstaff, int xrow);
}
