package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Xtrn;
import com.asl.mapper.XtrnMapper;
import com.asl.service.XtrnService;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Service
public class XtrnServiceImpl extends AbstractGenericService implements XtrnService {

	@Autowired private XtrnMapper xtrnMapper;

	@Override
	public long save(Xtrn xtrn) {
		if(xtrn == null || StringUtils.isBlank(xtrn.getXtypetrn()) || StringUtils.isBlank(xtrn.getXtrn())) return 0;
		xtrn.setZid(sessionManager.getBusinessId());
		return xtrnMapper.save(xtrn);
	}

	@Override
	public long update(Xtrn xtrn) {
		if(xtrn == null || StringUtils.isBlank(xtrn.getXtypetrn()) || StringUtils.isBlank(xtrn.getXtrn())) return 0;
		xtrn.setZid(sessionManager.getBusinessId());
		return xtrnMapper.update(xtrn);
	}

	@Override
	public List<Xtrn> getAllXtrn() {
		return xtrnMapper.getAllXtrn(sessionManager.getBusinessId());
	}

	@Override
	public List<Xtrn> findByXtypetrn(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return xtrnMapper.findByXtypetrn(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Xtrn> findByXtrn(String xtrn) {
		if(StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return xtrnMapper.findByXtrn(xtrn, sessionManager.getBusinessId());
	}

	@Override
	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return null;
		return xtrnMapper.findByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

}
