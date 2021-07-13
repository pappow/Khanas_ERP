package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.mapper.PoordMapper;
import com.asl.model.ServiceException;
import com.asl.model.report.RM0301;
import com.asl.service.PoordService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {

	@Autowired private PoordMapper poordMapper;

	@Override
	@Transactional
	public long save(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXtrn())) return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		poordHeader.setZauserid(getAuditUser());
		return poordMapper.savePoordHeader(poordHeader);
	}

	@Override
	@Transactional
	public long update(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) return 0;
		if(StringUtils.isBlank(poordHeader.getZid())) poordHeader.setZid(sessionManager.getBusinessId());
		poordHeader.setZuuserid(getAuditUser());
		return poordMapper.updatePoordHeader(poordHeader);
	}

	@Override
	public List<PoordHeader> getAllPoordHeaders() {
		return poordMapper.getAllPoordHeader(sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findPoordHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findPoordHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
	}

	@Override
	@Transactional
	public long saveDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		poordDetail.setZauserid(getAuditUser());
		long count = poordMapper.savePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long saveDetail(List<PoordDetail> poordDetails) throws ServiceException {
		if(poordDetails == null || poordDetails.isEmpty()) return 0;
		long f_count = 0;
		for(PoordDetail pd : poordDetails) {
			if(StringUtils.isBlank(pd.getXpornum())) throw new ServiceException("Requesition reference empty");
			pd.setZid(sessionManager.getBusinessId());
			pd.setZauserid(getAuditUser());
			f_count += poordMapper.savePoordDetailWithRow(pd);
		}
		return f_count;
	}

	@Override
	@Transactional
	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail) {
		if(poordDetail == null) return 0;
		return poordMapper.updatePoordHeaderTotalAmt(poordDetail);
	}

	@Override
	public long updateDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		poordDetail.setZuuserid(getAuditUser());
		long count = poordMapper.updatePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long deleteDetail(PoordDetail poordDetail) {
		if(poordDetail == null) return 0;
		long count = poordMapper.deletePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long deleteDetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.deleteDetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long archiveAllPoordDetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.archiveAllPoordDetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long countOfRequisitionDetailsByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.countOfRequisitionDetailsByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public PoordDetail findPoorddetailByXpornumAndXrow(String xpornum, int xrow) {
		if(StringUtils.isBlank(xpornum) || xrow == 0) return null;
		return poordMapper.findPoorddetailByXpornumAndXrow(xpornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<PoordDetail> findPoorddetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return Collections.emptyList();

		String centralZid = null;
		if(Boolean.TRUE.equals(sessionManager.getZbusiness().getCentral())) {
			centralZid = sessionManager.getBusinessId();
		} else {
			centralZid = sessionManager.getZbusiness().getCentralzid();
		}

		return poordMapper.findPoorddetailByXpornum(xpornum, sessionManager.getBusinessId(), centralZid);
	}

	@Override
	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(branchzid)) return Collections.emptyList();
		return poordMapper.findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
	}

	@Override
	public List<PoordHeader> getPoordHeadersByXtype(String xtype) {
		if(StringUtils.isBlank(xtype)) return Collections.emptyList();
		return poordMapper.getPoordHeadersByXtype(xtype, sessionManager.getBusinessId());
	}

	@Override
	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return null;
		return poordMapper.findPoorddetailByXpornumAndXitem(xpornum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid) {
		if(StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchCustomerByRequsitionNumber(xpornum, branchid, sessionManager.getBusinessId());
	}

	
	@Override
	public List<PoordHeader> searchXpornum(String xpornum){
		return poordMapper.searchXpornum(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RM0301> getRM0301(String fdate, String tdate, String xcus, String xstatuspor, String xitem) {
		if(StringUtils.isBlank(fdate) || StringUtils.isBlank(tdate)) return Collections.emptyList();
		return poordMapper.getRM0301(fdate, tdate, xcus, xstatuspor, xitem, sessionManager.getBusinessId());
	}

	@Override
	@Transactional
	public long deletePoordheaderByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.deletePoordheaderByXpornum(xpornum, sessionManager.getBusinessId());
	}
}
