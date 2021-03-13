package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Bmbomdetail;
import com.asl.entity.Bmbomheader;
import com.asl.mapper.BmbomMapper;
import com.asl.service.BmbomService;

/**
 * @author Zubayer Ahamed
 * @since Mar 13, 2021
 */
@Service
public class BmbomServiceImpl extends AbstractGenericService implements BmbomService {

	@Autowired private BmbomMapper bmbomMapper;

	@Override
	public long saveBmbomheader(Bmbomheader bmbomheader) {
		if(bmbomheader == null || StringUtils.isBlank(bmbomheader.getXitem())) return 0;
		bmbomheader.setZid(sessionManager.getBusinessId());
		return bmbomMapper.saveBmBomHeader(bmbomheader);
	}

	@Override
	public long updateBmbomheader(Bmbomheader bmbomheader) {
		if(bmbomheader == null || StringUtils.isBlank(bmbomheader.getXitem())) return 0;
		bmbomheader.setZid(sessionManager.getBusinessId());
		return bmbomMapper.updateBmBomHeader(bmbomheader);
	}

	@Override
	public long saveBmbomdetail(Bmbomdetail bmbomdetail) {
		if(bmbomdetail == null || StringUtils.isBlank(bmbomdetail.getXbomkey())) return 0;
		bmbomdetail.setZid(sessionManager.getBusinessId());
		return bmbomMapper.saveBmBomDetail(bmbomdetail);
	}

	@Override
	public long updateBmbomdetail(Bmbomdetail bmbomdetail) {
		if(bmbomdetail == null || StringUtils.isBlank(bmbomdetail.getXbomkey())) return 0;
		return bmbomMapper.saveBmBomDetail(bmbomdetail);
	}

	@Override
	public Bmbomheader findBmbomheaderByXbomkey(String xbomkey) {
		if(StringUtils.isBlank(xbomkey)) return null;
		return bmbomMapper.findBmBomHeaderByXbomkey(xbomkey, sessionManager.getBusinessId());
	}

	@Override
	public Bmbomheader findBmBomHeaderByXitem(String xitem) {
		if(StringUtils.isBlank(xitem)) return null;
		return bmbomMapper.findBmBomHeaderByXitem(xitem, sessionManager.getBusinessId());
	}

	@Override
	public List<Bmbomdetail> findBmbomdetailByXbomkey(String xbomkey) {
		if(StringUtils.isBlank(xbomkey)) return Collections.emptyList();
		return bmbomMapper.findBmBomdetailByXbomkey(xbomkey, sessionManager.getBusinessId());
	}

	@Override
	public List<Bmbomheader> getAllBmbomheader() {
		return bmbomMapper.getAllBmBomheader(sessionManager.getBusinessId());
	}

	@Override
	public Bmbomdetail findBmbomdetailByXbomkeyAndXbomrow(String xbomkey, int xbomrow) {
		if(StringUtils.isBlank(xbomkey) || xbomrow == 0) return null;
		return bmbomMapper.findBmBomdetailByXbomkeyAndXbomrow(xbomkey, xbomrow, sessionManager.getBusinessId());
	}

	@Override
	public long archiveBmbomdetailByXbomkey(String xbomkey) {
		if(StringUtils.isBlank(xbomkey)) return 0;
		return bmbomMapper.archiveBmbomdetailByXbomkey(xbomkey, sessionManager.getBusinessId());
	}

	
}
