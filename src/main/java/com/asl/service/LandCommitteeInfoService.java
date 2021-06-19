package com.asl.service;

import java.util.List;

import com.asl.entity.LandCommitteeInfo;

public interface LandCommitteeInfoService {

	public long save(LandCommitteeInfo landcommiteeinfo);

	public long update(LandCommitteeInfo landcommiteeinfo);

	public List<LandCommitteeInfo> getAllLandCommitteeInfo();

	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee);
}
