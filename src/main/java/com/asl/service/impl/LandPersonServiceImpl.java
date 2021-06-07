package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandPerson;
import com.asl.mapper.LandPersonMapper;
import com.asl.service.LandPersonService;


@Service
public class LandPersonServiceImpl extends AbstractGenericService implements LandPersonService {

	
	@Autowired
	private LandPersonMapper landpersonMapper;
	
	@Transactional
	@Override
	public long save(LandPerson landPerson) {
		if (landPerson == null)
			return 0;
		landPerson.setZid(sessionManager.getBusinessId());
		landPerson.setZauserid(getAuditUser());
		return landpersonMapper.saveLandPerson(landPerson);
	}

	@Transactional
	@Override
	public long update(LandPerson landPerson) {
		if (landPerson == null)
			return 0;
		landPerson.setZid(sessionManager.getBusinessId());
		landPerson.setZuuserid(getAuditUser());
		return landpersonMapper.updateLandPerson(landPerson);
	}

	@Override
	public List<LandPerson> getAllLandPerson() {
		return landpersonMapper.getAllLandPerson(sessionManager.getBusinessId());
	}

	@Override
	public LandPerson findByLandPerson(String xperson) {
		if (StringUtils.isBlank(xperson))
			return null;
		return landpersonMapper.findByLandPerson(xperson, sessionManager.getBusinessId());
	}

}
