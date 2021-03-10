package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Pogrndetail;
import com.asl.entity.Pogrnheader;
import com.asl.mapper.PogrnMapper;
import com.asl.service.PogrnService;

@Service
public class PogrnServiceImpl extends AbstractGenericService implements PogrnService {
	
	@Autowired
	private PogrnMapper pogrnMapper;

	@Override
	public long save(Pogrnheader pogrnHeader) {
		//if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()) || StringUtils.isBlank(pogrnHeader.getXtrnpor()))
		if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))
			return 0;
		pogrnHeader.setZid(sessionManager.getBusinessId());
		return pogrnMapper.savePogrnHeader(pogrnHeader);
	}

	@Override
	public long update(Pogrnheader pogrnHeader) {
		if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXgrnnum()))
			return 0;
		pogrnHeader.setZid(sessionManager.getBusinessId());
		return pogrnMapper.updatePogrnHeader(pogrnHeader);
	}

	@Override
	public long updatePogrnHeaderTotalAmt(Pogrndetail pogrnDetail) {
		if(pogrnDetail == null) return 0;
		return pogrnMapper.updatePogrnHeaderTotalAmt(pogrnDetail);
	}

	@Override
	public long saveDetail(Pogrndetail pogrnDetail) {
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) return 0;
		pogrnDetail.setZid(sessionManager.getBusinessId());
		long count = pogrnMapper.savePogrnDetail(pogrnDetail);
		/*
		if(count != 0) {
			count = updatePogrnHeaderTotalAmt(pogrnDetail);
		}
		*/
		return count;
	}

	@Override
	public long updateDetail(Pogrndetail pogrnDetail) {
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) return 0;
		pogrnDetail.setZid(sessionManager.getBusinessId());
		long count = pogrnMapper.updatePogrnDetail(pogrnDetail);
		/*
		if(count != 0) {
			count = updatePogrnHeaderTotalAmt(pogrnDetail);
		}
		*/
		return count;
	}

	@Override
	public long deleteDetail(Pogrndetail pogrnDetail) {
		if(pogrnDetail == null) return 0;
		long count = pogrnMapper.deletePogrnDetail(pogrnDetail);
		
		if(count != 0) {
			// Commmented due to the reason of xlineamt column not present in PogrnDetail. We need this to calculate xtotamt.
			//count = updatePogrnHeaderTotalAmt(pogrnDetail);
		}
		
		return count;
	}

	@Override
	public Pogrnheader findPogrnHeaderByXgrnnum(String xgrnnum) {
		if (StringUtils.isBlank(xgrnnum))
			return null;

		return pogrnMapper.findPogrnHeaderByXgrnnum(xgrnnum, sessionManager.getBusinessId());
	}
	
	@Override
	public Pogrnheader findPogrnHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum))
			return null;

		return pogrnMapper.findPogrnHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public Pogrndetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow) {
		if(StringUtils.isBlank(xgrnnum) || xrow == 0) return null;
		return pogrnMapper.findPogrnDetailByXgrnnumAndXrow(xgrnnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Pogrndetail> findPogrnDetailByXgrnnum(String xgrnnum) {
		if(StringUtils.isBlank(xgrnnum)) return Collections.emptyList();
		return pogrnMapper.findPogrnDetailByXgrnnum(xgrnnum, sessionManager.getBusinessId());
	}

	@Override
	public List<Pogrnheader> getAllPogrnHeaders() {
		return pogrnMapper.getAllPogrnHeader(sessionManager.getBusinessId());
	}
	
	

}
