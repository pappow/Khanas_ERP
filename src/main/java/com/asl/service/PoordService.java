package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

@Component
public interface PoordService {
	public long save(PoordHeader poordHeader);

	public long update(PoordHeader poordHeader);

	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail);

	public long saveDetail(PoordDetail poordDetail);

	public long saveDetail(List<PoordDetail> poordDetails) throws ServiceException;

	public long updateDetail(PoordDetail poordDetail);

	public long deleteDetail(PoordDetail poordDetail);

	public long deleteDetailByXpornum(String xpornum);

	public long archiveAllPoordDetailByXpornum(String xpornum);

	public long countOfRequisitionDetailsByXpornum(String xpornum);

	public PoordHeader findPoordHeaderByXpornum(String xpornum);

	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid);

	public PoordDetail findPoorddetailByXpornumAndXrow(String xpornum, int xrow);

	public List<PoordDetail> findPoorddetailByXpornum(String xpornum);

	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid);

	public List<PoordHeader> getAllPoordHeaders();

	public List<PoordHeader> getPoordHeadersByXtypetrn(String xtypetrn);

	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem);

	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid);

	public Cacus findBranchCustomerByRequsitionNumber(String xpornum);

	public List<PoordHeader> searchXpornum(String xpornum);

	// REPORT
	public List<com.asl.model.report.RM0301> getRM0301(String fdate, String tdate, String xcus, String xstatuspor, String xitem);

	public long deletePoordheaderByXpornum(String xpornum);

	public Map<String, Object> createPurchaseOrderToGRN(ResponseHelper responseHelper, String xpornum) throws ServiceException;

	public List<PoordDetail> searchPurchaseOrderAvailableItem(String xpornum, String xitem);
}
