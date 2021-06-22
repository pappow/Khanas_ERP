package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Xtrn;
import com.asl.mapper.XtrnMapper;
import com.asl.model.ServiceException;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Slf4j
@Service
public class XtrnServiceImpl extends AbstractGenericService implements XtrnService {

	@Autowired private XtrnMapper xtrnMapper;

	@Transactional
	@Override
	public long save(Xtrn xtrn) throws ServiceException {
		if(xtrn == null || StringUtils.isBlank(xtrn.getXtypetrn()) || StringUtils.isBlank(xtrn.getXtrn())) return 0;
		xtrn.setZid(sessionManager.getBusinessId());
		xtrn.setZauserid(getAuditUser());
		try {
			return xtrnMapper.save(xtrn);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public long update(Xtrn xtrn) throws ServiceException {
		if(xtrn == null || StringUtils.isBlank(xtrn.getXtypetrn()) || StringUtils.isBlank(xtrn.getXtrn())) return 0;
		xtrn.setZid(sessionManager.getBusinessId());
		xtrn.setZuuserid(getAuditUser());
		try {
			return xtrnMapper.update(xtrn);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<Xtrn> getAllXtrn() {
		return getAllXtrn(null);
	}

	@Override
	public List<Xtrn> getAllXtrn(Boolean zactive) {
		return xtrnMapper.getAllXtrn(sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Xtrn> findByXtypetrn(String xtypetrn) {
		return findByXtypetrn(xtypetrn, null);
	}

	@Override
	public List<Xtrn> findByXtypetrn(String xtypetrn, Boolean zactive) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return xtrnMapper.findByXtypetrn(xtypetrn, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Xtrn> findByXtrn(String xtrn) {
		return findByXtrn(xtrn, null);
	}

	@Override
	public List<Xtrn> findByXtrn(String xtrn, Boolean zactive) {
		if(StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return xtrnMapper.findByXtrn(xtrn, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		return findByXtypetrnAndXtrn(xtypetrn, xtrn, null);
	}

	@Override
	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn, Boolean zactive) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return null;
		return xtrnMapper.findByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public String generateAndGetXtrnNumber(String xtypetrn, String xtrn, int leftpad) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return "";
		if(leftpad < 6) leftpad = 6;
		return xtrnMapper.generateXtrn(xtypetrn, xtrn, leftpad, sessionManager.getBusinessId());
	}
}
