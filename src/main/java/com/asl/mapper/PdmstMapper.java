package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface PdmstMapper {
	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);
	
	public long delete(Pdmst pdmst);

	public Pdmst findPdmstByXstaff(String xstaff, String zid, Boolean zactive);

	public List<Pdmst> getAllPdmst(String zid, Boolean zactive);
	
	public List<Pdmst> getAllHRPdmst(String zid);
	
	public Pdmst findAllPdmst(String xstaff, String zid);
	
	public List<Pdmst> searchStaff(String xstaff, String zid);
	
	//for HRQualification
	public long savePdeducation(Pdeducation pdqua);
	
	public long updatePdeducation(Pdeducation pdqua);
	
	public long deletePdeducation(Pdeducation pdqua);
	
	public List<Pdeducation> getAllPdeducation(String zid);
	
	public List<Pdeducation> findByPdeducation(String xstaff, String zid);
	
	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRExperience
	public long savePdexperience(Pdexperience pdex);
	
	public long updatePdexperience(Pdexperience pdex);
	
	public long deletePdexperience(Pdexperience pdex);
	
	public List<Pdexperience> getAllPdexperience(String zid);
	
	public List<Pdexperience> findByPdexperience(String xstaff, String zid);
	
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRPromotion
	public long savePdpromodt(Pdpromodt pdpr);
	
	public long updatePdpromodt(Pdpromodt pdpr);
	
	public long deletePdpromodt(Pdpromodt pdpr);
	
	public List<Pdpromodt> getAllPdpromodt(String zid);
	
	public List<Pdpromodt> findByPdpromodt(String xstaff, String zid);
	
	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow, String zid);
}
