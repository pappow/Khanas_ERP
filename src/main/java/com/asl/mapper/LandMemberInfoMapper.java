package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandMemberInfo;
import com.asl.entity.LandPerson;

@Mapper
public interface LandMemberInfoMapper {

	public long saveLandMemberInfo(LandMemberInfo landMemberInfo);
	
	public long updateLandMemberInfo(LandMemberInfo landMemberInfo);

	public List<LandMemberInfo> getAllLandMemberInfo(String zid);

	public LandMemberInfo findByLandMemberInfo(String xmember, String zid);
	
	public List<LandPerson> searchPersonId(String xperson, String zid);
}
