package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandComEvent;



@Mapper
public interface LandComEventMapper {
	
	public long saveLandComEvent(LandComEvent landComEvent);
	
	public long updateLandComEvent(LandComEvent landComEvent);
	
	public List<LandComEvent> getAllLandComEvent(String zid);
	
	public List<LandComEvent> getAllLandOtherEvent(String zid);
	
	public LandComEvent findAllLandComEvent(String xevent, String zid);
 	
	

}
