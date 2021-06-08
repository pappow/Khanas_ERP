package com.asl.service;

import java.util.List;

import com.asl.entity.LandInfo;

public interface LandInfoService {
	
public long save(LandInfo landinfo);
	
	public long update(LandInfo landinfo);

	public List<LandInfo> getAllLandInfo();

	public LandInfo findByLandInfo(String xland);

}
