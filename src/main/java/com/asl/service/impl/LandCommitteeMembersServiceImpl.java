package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandPerson;
import com.asl.mapper.LandCommitteeMembersMapper;
import com.asl.service.LandCommitteeMembersService;

@Service
public class LandCommitteeMembersServiceImpl extends AbstractGenericService implements LandCommitteeMembersService{

	@Autowired
	private LandCommitteeMembersMapper landcommitteemembersMapper;

	@Transactional
	@Override
	public long save(LandCommitteeMembers landCommiteemembers) {
		if (landCommiteemembers == null)
			return 0;
		landCommiteemembers.setZid(sessionManager.getBusinessId());
		landCommiteemembers.setZauserid(getAuditUser());
		return landcommitteemembersMapper.saveLandCommitteeMembers(landCommiteemembers);
	}

	@Transactional
	@Override
	public long update(LandCommitteeMembers landCommiteemembers) {
		if (landCommiteemembers == null)
			return 0;
		landCommiteemembers.setZid(sessionManager.getBusinessId());
		landCommiteemembers.setZauserid(getAuditUser());
		return landcommitteemembersMapper.updateLandCommitteeMembers(landCommiteemembers);
	}

	@Override
	public List<LandCommitteeMembers> getAllLandCommitteeMembers() {
		return landcommitteemembersMapper.getAllLandCommitteeMembers(sessionManager.getBusinessId());
	}

	@Override
	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson) {
		if (StringUtils.isBlank(xcommittee) || StringUtils.isBlank(xperson))
			return null;
		return landcommitteemembersMapper.findByXcommitteeAndXperson(xcommittee, xperson, sessionManager.getBusinessId());
	}

	@Override
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee) {
		if(StringUtils.isBlank(xcommittee)) return Collections.emptyList();
		return landcommitteemembersMapper.searchCommitteeId(xcommittee.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<LandPerson> searchPersonId(String xperson) {
		if(StringUtils.isBlank(xperson)) return Collections.emptyList();
		return landcommitteemembersMapper.searchPersonId(xperson.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public long delete(LandCommitteeMembers landcommiteemembers) {
		// TODO Auto-generated method stub
		return 0;
	}


}
