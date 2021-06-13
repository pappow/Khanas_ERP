package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandEducation;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
@Mapper
public interface LandInfoMapper {
	
	public long saveLandInfo(LandInfo landinfo);
	
	public long updateLandInfo(LandInfo landinfo);

	public List<LandInfo> getAllLandInfo(String zid);

	public LandInfo findByLandInfo(String xland, String zid);
	
	//for owner
	public long saveLandOwner(LandOwner landOwner);
	
	public long updateLandOwner(LandOwner landOwner);
	
	public long deleteLandOwner(LandOwner landOwner);
	
	public List<LandOwner> getAllLandOwner(String zid);
	
	public List<LandOwner> findByLandOwner(String xland, String zid);
	
	public LandOwner findLandOwnerByXlandAndXrow(String xland, int xrow, String zid);
	
	public List<LandPerson> searchPersonId(String xperson, String zid);
	



}
