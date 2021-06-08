package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandInfo;
@Mapper
public interface LandInfoMapper {
	
	public long saveLandInfo(LandInfo landinfo);
	
	public long updateLandInfo(LandInfo landinfo);

	public List<LandInfo> getAllLandInfo(String zid);

	public LandInfo findByLandInfo(String xland, String zid);

}
