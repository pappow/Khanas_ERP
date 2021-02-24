package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		xcodes.setBusinessId(sessionManager.getBusinessId());
		return xcodesMapper.saveXcodes(xcodes);
	}

	@Override
	public long update(Xcodes xcodes) {
		if (xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) return 0;
		xcodes.setBusinessId(sessionManager.getBusinessId());
		return xcodesMapper.updateXcodes(xcodes);
	}

	@Override
	public List<Xcodes> getAllXcodes() {
		return xcodesMapper.getAllXcodes(sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> findByXtype(String xType) {
		if (StringUtils.isBlank(xType)) return Collections.emptyList();
		return xcodesMapper.findByXtype(xType, sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> findByXcode(String xCode) {
		if (StringUtils.isBlank(xCode)) return Collections.emptyList();
		return xcodesMapper.findByXcode(xCode, sessionManager.getBusinessId());
	}

	@Override
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;

		return xcodesMapper.findByXtypesAndXcodes(xType, xCodes, sessionManager.getBusinessId());
	}

}
