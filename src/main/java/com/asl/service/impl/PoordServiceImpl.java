package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.PogrnMapper;
import com.asl.mapper.PoordMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.model.report.RM0301;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.OpordService;
import com.asl.service.PoordService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {

	@Autowired private PoordMapper poordMapper;
	@Autowired private PogrnMapper pogrnMapper;
	@Autowired private OpordService opordService;
	@Autowired private CaitemService caitemService;
	@Autowired private CacusService cacusService;

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
	public List<PoordHeader> getPoordHeadersByXtypetrn(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return poordMapper.getPoordHeadersByXtypetrn(xtypetrn, sessionManager.getBusinessId());
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

	@Transactional
	@Override
	public Map<String, Object> createPurchaseOrderToGRN(ResponseHelper responseHelper, String xpornum) throws ServiceException {
		PoordHeader poordHeader = findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find purchase order : " + xpornum);
			return responseHelper.getResponse();
		}
		if("Open".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Purchase order not confirmed");
			return responseHelper.getResponse();
		}

		// check purchase order has item details
		List<PoordDetail> poordDetailList = findPoorddetailByXpornum(xpornum);
		if(poordDetailList == null || poordDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item");
			return responseHelper.getResponse();
		}
		// check is there is any available item to make GRN
		boolean itemavailable = false;
		for(PoordDetail p : poordDetailList) {
			if(p.getXqtygrn() == null) p.setXqtygrn(BigDecimal.ZERO);
			if(!p.getXqtygrn().equals(p.getXqtyord())) itemavailable = true;
		}
		if(!itemavailable) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item available to make GRN");
			return responseHelper.getResponse();
		}


		// Create a GRN header first
		PogrnHeader pogrnHeader = new PogrnHeader();
		pogrnHeader.setXpornum(xpornum);
		pogrnHeader.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrnHeader.setXtrn(TransactionCodeType.GRN_NUMBER.getdefaultCode());
		pogrnHeader.setXdate(new Date());
		pogrnHeader.setXstatusgrn("Open");
		pogrnHeader.setXtotamt(poordHeader.getXtotamt());
		pogrnHeader.setXwh(poordHeader.getXwh());
		pogrnHeader.setXcus(poordHeader.getXcus());
		pogrnHeader.setZid(sessionManager.getBusinessId());
		pogrnHeader.setZauserid(getAuditUser());

		long count = pogrnMapper.savePogrnHeader(pogrnHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		// Create grn details from purchase details
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			if(poorddetail.getXqtygrn() == null) poorddetail.setXqtygrn(BigDecimal.ZERO);
			if(poorddetail.getXqtyord() == null) poorddetail.setXqtyord(BigDecimal.ZERO);

			PogrnDetail detail = new PogrnDetail();
			detail.setXgrnnum(pogrnHeader.getXgrnnum());
			detail.setXitem(poorddetail.getXitem());
			detail.setXdocrow(poorddetail.getXrow());
			detail.setXqtygrn(poorddetail.getXqtyord().subtract(poorddetail.getXqtygrn()));
			detail.setXrate(poorddetail.getXrate());
			detail.setXunitpur(poorddetail.getXunitpur());
			detail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			detail.setZid(sessionManager.getBusinessId());
			detail.setZauserid(getAuditUser());
			
			// if item has no qty, then it don't need to save
			if(BigDecimal.ZERO.equals(detail.getXqtygrn())) continue;

			long dcount = pogrnMapper.savePogrnDetail(detail);
			if(dcount == 0) throw new ServiceException("Can't save detail");

			poorddetail.setXqtygrn(poorddetail.getXqtygrn().add(detail.getXqtygrn()));
		}

		// now update poorddetails with grn qty
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			long dcount = updateDetail(poorddetail);
			if(dcount == 0) throw new ServiceException("Can't update purchase detail");
		}

		// now update poordheader status
		poordHeader.setXstatuspor("Full Received");
		long phcount = update(poordHeader);
		if(phcount == 0) throw new ServiceException("Can't update purchase order status");

		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/procurements/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

	@Override
	public List<PoordDetail> searchPurchaseOrderAvailableItem(String xpornum, String xitem){
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return Collections.emptyList();
		return poordMapper.searchPurchaseOrderAvailableItem(xpornum, xitem.toUpperCase(), sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> confirmRequisitionsOfBranch(ResponseHelper responseHelper, String xpornum, String branchzid) throws ServiceException {
		// Change requisition order status
		PoordHeader ph = findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
		if(ph == null) {
			responseHelper.setErrorStatusAndMessage("Can't find any requisition in the system");
			return responseHelper.getResponse();
		}

		// find all order requisition details first
		List<PoordDetail> poordDetailsList = findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
		if(poordDetailsList == null || poordDetailsList.isEmpty()) {  // if no detail exist
			responseHelper.setErrorStatusAndMessage("Requisition has no item added");
			return responseHelper.getResponse();
		}

		// Create sales order header
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.SALES_ORDER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_ORDER.getdefaultCode());
		oh.setXpornum(ph.getXpornum());
		oh.setXdate(new Date());
		oh.setXstatus("Open");
		oh.setXstatusord("Open");
		oh.setXnote(ph.getXnote());
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXvatait("No Vat");

		// Tag with branch customer    xgcus   xcuszid
		Cacus cacus = cacusService.findCacusByXcuszid(ph.getZid());
		if(cacus == null) {
			responseHelper.setErrorStatusAndMessage("There is no customer found for this branch");
			return responseHelper.getResponse();
		}
		oh.setXcus(cacus.getXcus());

		long ohCount = opordService.saveOpordHeader(oh);
		if(ohCount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't crete sales order");
			return responseHelper.getResponse();
		}

		// if header saved successfully, then find it again from db to get xordernum
		// find oh by  xpornum, xdate, xtypetrn and xcus 
		Opordheader savedoh = opordService.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(oh.getXtypetrn(), oh.getXpornum(), oh.getXcus(), oh.getXdate());
		if(savedoh == null) {
			responseHelper.setErrorStatusAndMessage("Can't found any sales order");
			return responseHelper.getResponse();
		}

		// if detail data exist
		List<Oporddetail> detailsList = new ArrayList<>();
		for(PoordDetail pd : poordDetailsList) {
			// create all sales details from requisition details
			Caitem c = caitemService.findByXitem(pd.getXitem());
			if(c == null) {
				responseHelper.setErrorStatusAndMessage("Item "+ pd.getXitem() +" not found");
				return responseHelper.getResponse();
			}

			Oporddetail od = new Oporddetail();
			od.setXordernum(savedoh.getXordernum());
			od.setXitem(pd.getXitem());
			od.setXunit(pd.getXunitpur());
			od.setXqtyord(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			od.setXrate(pd.getXrate() == null ? BigDecimal.ZERO : pd.getXrate());
			od.setXdesc(c.getXdesc());
			od.setXcatitem(c.getXcatitem());
			od.setXgitem(c.getXgitem());
			od.setXlineamt(od.getXqtyord().multiply(od.getXrate()));

			detailsList.add(od);
		}

		// now save all details
		long countOD = opordService.saveBatchOpordDetail(detailsList);
		if(countOD == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create sales order detail");
			return responseHelper.getResponse();
		}

		// Update status and order reference
		ph.setXstatuspor("Confirmed");
		ph.setXordernum(savedoh.getXordernum());
		long count = update(ph);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// reload page
		responseHelper.setSuccessStatusAndMessage("Requisition confirmed successfully");
		responseHelper.setReloadSectionIdWithUrl("branchesorderrequisitiontable", "/purchasing/bqls/query?date=" + sdf.format(ph.getXdate()));
		return responseHelper.getResponse();
	}

}
