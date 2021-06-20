package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Xcodes;
import com.asl.mapper.XcodesMapper;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Service
public class XcodesServiceImpl extends AbstractGenericService implements XcodesService {

	@Autowired
	private XcodesMapper xcodesMapper;

	@Override
	public long save(Xcodes xcodes) {
		if (xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) return 0;
		xcodes.setZid(getBusinessId());
		xcodes.setZauserid(getAuditUser());
		return xcodesMapper.saveXcodes(xcodes);
	}

	@Override
	public long update(Xcodes xcodes) {
		if (xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) return 0;
		xcodes.setZid(getBusinessId());
		xcodes.setZuuserid(getAuditUser());
		return xcodesMapper.updateXcodes(xcodes);
	}

	@Override
	public List<Xcodes> getAllXcodes() {
		return getAllXcodes(null);
	}

	@Override
	public List<Xcodes> getAllXcodes(Boolean zactive) {
		return xcodesMapper.getAllXcodes(getBusinessId(), zactive);
	}

	@Override
	public List<Xcodes> findByXtype(String xType) {
		return findByXtype(xType, null);
	}

	@Override
	public List<Xcodes> findByXtype(String xType, Boolean zactive) {
		if (StringUtils.isBlank(xType)) return Collections.emptyList();
		return xcodesMapper.findByXtype(xType, getBusinessId(), zactive);
	}


	@Override
	public List<Xcodes> findByXcode(String xCode) {
		return findByXcode(xCode, null);
	}

	@Override
	public List<Xcodes> findByXcode(String xCode, Boolean zactive) {
		if (StringUtils.isBlank(xCode)) return Collections.emptyList();
		return xcodesMapper.findByXcode(xCode, getBusinessId(), zactive);
	}

	@Override
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes) {
		return findByXtypesAndXcodes(xType, xCodes, null);
	}

	@Override
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes, Boolean zactive) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;
		return xcodesMapper.findByXtypesAndXcodes(xType, xCodes, getBusinessId(), zactive);
	}

	@Override
	public Xcodes getSeilingRecord(String direction) {
		return xcodesMapper.getSeilingRecord(direction, getBusinessId());
	}

	@Transactional
	@Override
	public long deleteXcodes(String xcodes, String xtype) {
		if(StringUtils.isBlank(xcodes) || StringUtils.isBlank(xtype)) return 0;
		return xcodesMapper.deleteXcodes(xcodes, xtype, sessionManager.getBusinessId());
	}

	
}
