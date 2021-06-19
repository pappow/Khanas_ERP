package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandCommitteeInfo;

@Mapper
public interface LandCommitteeInfoMapper {
	
	public long saveLandCommitteeInfo(LandCommitteeInfo landcommiteeinfo);

	public long updateLandCommitteeInfo(LandCommitteeInfo landcommiteeinfo);

	public List<LandCommitteeInfo> getAllLandCommitteeInfo(String zid);

	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee, String zid);
}
