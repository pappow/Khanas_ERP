package com.asl.service;

import java.util.List;

import com.asl.entity.LandComEvent;

public interface LandComEventService {
	
public long saveLandComEvent(LandComEvent landComEvent);
	
	public long updateLandComEvent(LandComEvent landComEvent);
	
	public List<LandComEvent> getAllLandComEvent();
	
	public List<LandComEvent> getAllLandOtherEvent();
	
	public LandComEvent findAllLandComEvent(String xevent);
	
	

}
