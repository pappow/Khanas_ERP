package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandEducation;
import com.asl.entity.LandPerson;

@Mapper
public interface LandCommitteeMembersMapper {
	
	public long saveLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);

	public long updateLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);
	
	public long deleteLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);

	public List<LandCommitteeMembers> getAllLandCommitteeMembers(String zid);

	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson, String zid);

	// search
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee, String zid);

	public List<LandPerson> searchPersonId(String xperson, String zid);

}
