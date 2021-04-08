package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.mapper.PogrnMapper;
import com.asl.service.PogrnService;

@Service
public class PogrnServiceImpl extends AbstractGenericService implements PogrnService {
	
	@Autowired
	private PogrnMapper pogrnMapper;

	@Override
	public long save(PogrnHeader pogrnHeader) {
		//if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()) || StringUtils.isBlank(pogrnHeader.getXtrnpor()))
		if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))
			return 0;
		pogrnHeader.setZid(sessionManager.getBusinessId());
		pogrnHeader.setZauserid(getAuditUser());
		return pogrnMapper.savePogrnHeader(pogrnHeader);
	}

	@Override
	public long update(PogrnHeader pogrnHeader) {
		if (pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXgrnnum()))
			return 0;
		pogrnHeader.setZid(sessionManager.getBusinessId());
		pogrnHeader.setZuuserid(getAuditUser());
		return pogrnMapper.updatePogrnHeader(pogrnHeader);
	}

	@Override
	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail) {
		if(pogrnDetail == null) return 0;
		return pogrnMapper.updatePogrnHeaderTotalAmt(pogrnDetail);
	}
	
	@Override
	public long updatePogrnHeaderTotalAmtAndGrandTotalAmt(String xgrnnum) {
		if(StringUtils.isBlank(xgrnnum)) return 0;
		return pogrnMapper.updatePogrnHeaderTotalAmtAndGrandTotalAmt(xgrnnum, sessionManager.getBusinessId());
	}

	@Override
	public long saveDetail(PogrnDetail pogrnDetail) {
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) return 0;
		pogrnDetail.setZid(sessionManager.getBusinessId());
		pogrnDetail.setZauserid(getAuditUser());
		long count = pogrnMapper.savePogrnDetail(pogrnDetail);		
		if(count != 0) { count = updatePogrnHeaderTotalAmtAndGrandTotalAmt(pogrnDetail.getXgrnnum());};
		return count;
	}

	@Override
	public long updateDetail(PogrnDetail pogrnDetail) {
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) return 0;
		pogrnDetail.setZid(sessionManager.getBusinessId());
		pogrnDetail.setZuuserid(getAuditUser());
		long count = pogrnMapper.updatePogrnDetail(pogrnDetail);
		if(count != 0) { count = updatePogrnHeaderTotalAmtAndGrandTotalAmt(pogrnDetail.getXgrnnum());};		
		return count;
	}

	@Override
	public long deleteDetail(PogrnDetail pogrnDetail) {
		if(pogrnDetail == null) return 0;
		long count = pogrnMapper.deletePogrnDetail(pogrnDetail);
		
		if(count != 0) { count = updatePogrnHeaderTotalAmtAndGrandTotalAmt(pogrnDetail.getXgrnnum());};
		
		return count;
	}

	@Override
	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum) {
		if (StringUtils.isBlank(xgrnnum))
			return null;

		return pogrnMapper.findPogrnHeaderByXgrnnum(xgrnnum, sessionManager.getBusinessId());
	}
	
	@Override
	public PogrnHeader findPogrnHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum))
			return null;

		return pogrnMapper.findPogrnHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow) {
		if(StringUtils.isBlank(xgrnnum) || xrow == 0) return null;
		return pogrnMapper.findPogrnDetailByXgrnnumAndXrow(xgrnnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum) {
		if(StringUtils.isBlank(xgrnnum)) return Collections.emptyList();
		return pogrnMapper.findPogrnDetailByXgrnnum(xgrnnum, sessionManager.getBusinessId());
	}

	@Override
	public List<PogrnHeader> getAllPogrnHeaders() {
		return pogrnMapper.getAllPogrnHeader(sessionManager.getBusinessId());
	}

	@Override
	public void procInventory(String xgrnnum, String xpornum, String p_seq) {
		pogrnMapper.procInventory(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xgrnnum, xpornum, p_seq);
	}
	
	@Override
	public void procTransferPOtoAP(String xgrnnum, String p_seq) {
		pogrnMapper.procTransferPOtoAP(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xgrnnum, p_seq);
	}
	
	@Override
	public List<PogrnHeader> searchPoord(String xpornum){
		return pogrnMapper.searchPoord(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	/*
	 * @Override public List<PogrnHeader> search(String xwh, String sup, String
	 * status, String fromDate, String toDate) { // TODO Auto-generated method stub
	 * return pogrnMapper.search(xwh, sup, status, fromDate, toDate); }
	 */


}
