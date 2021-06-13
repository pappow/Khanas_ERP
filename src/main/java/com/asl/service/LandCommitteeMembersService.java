package com.asl.service;

import java.util.List;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandPerson;

public interface LandCommitteeMembersService {

	
	public long save(LandCommitteeMembers landcommiteemembers);

	public long update(LandCommitteeMembers landcommiteemembers);
	
	public long delete(LandCommitteeMembers landcommiteemembers);

	public List<LandCommitteeMembers> getAllLandCommitteeMembers();

	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson);

	// search
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee);

	public List<LandPerson> searchPersonId(String xperson);
}
