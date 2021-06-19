package com.asl.service;

import java.util.List;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;

public interface LandInfoService {
	
public long save(LandInfo landinfo);
	
	public long update(LandInfo landinfo);

	public List<LandInfo> getAllLandInfo();

	public LandInfo findByLandInfo(String xland);
	
	//for owner
	public long save(LandOwner landOwner);
	
	public long update(LandOwner landOwner);
	
	public long deleteLandOwner(LandOwner landOwner);
	
	public List<LandOwner> getAllLandOwner(String zid);
	
	public List<LandOwner> findByLandOwner(String xland);
	
	public LandOwner findLandOwnerByXlandAndXrow(String xland, int xrow);
	
	public LandOwner findByXlandAndXperson(String xland, String xperson);
		
	//for dag details
	
	public long save(LandDagDetails landDagDetails);
	
	public long update(LandDagDetails landDagDetails);
	
	public long deleteLandDagDetails(LandDagDetails landDagDetails);
	
	public List<LandDagDetails> getAllLandDagDetails(String zid);
	
	public List<LandDagDetails> findByLandDagDetails(String xland);
	
	public LandDagDetails findlandDagDetailsByXlandAndXrow(String xland, int xrow);
		

}
