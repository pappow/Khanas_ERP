package com.asl.service;

import java.util.List;

import com.asl.entity.LandMemberInfo;
import com.asl.entity.LandPerson;

public interface LandMemberInfoService {

	public long save(LandMemberInfo landMemberInfo);
	
	public long update(LandMemberInfo landMemberInfo);

	public long delete(LandMemberInfo landMemberInfo);
	
	public List<LandMemberInfo> getAllLandMemberInfo();

	public LandMemberInfo findByLandMemberInfo(String xmember);
	
	//search
	public List<LandMemberInfo> searchMemberId(String xmember);
}
