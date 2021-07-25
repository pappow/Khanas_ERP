package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;

@Mapper
public interface PoordMapper {

	public long savePoordHeader(PoordHeader poordHeader);

	public long updatePoordHeader(PoordHeader poordHeader);

	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail);

	public long savePoordDetail(PoordDetail poordDetail);

	public long savePoordDetailWithRow(PoordDetail poordDetail);

	public long updatePoordDetail(PoordDetail poordDetail);

	public long deletePoordDetail(PoordDetail poordDetail);

	public long deleteDetailByXpornum(String xpornum, String zid);

	public long archiveAllPoordDetailByXpornum(String xpornum, String zid);

	public long countOfRequisitionDetailsByXpornum(String xpornum, String zid);

	public PoordHeader findPoordHeaderByXpornum(String xpornum, String zid);

	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid);

	public PoordDetail findPoorddetailByXpornumAndXrow(String xpornum, int xrow, String zid);

	public List<PoordDetail> findPoorddetailByXpornum(String xpornum, String zid, String centralzid);

	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid);

	public List<PoordHeader> getAllPoordHeader(String zid);

	public List<PoordHeader> getPoordHeadersByXtypetrn(String xtypetrn, String zid);

	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem, String zid);
	
	public List<PoordHeader> searchXpornum(String xpornum, String zid);

	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid, String zid);

	public Cacus findBranchCustomerByXcus(String xcus, String zid);

	public List<com.asl.model.report.RM0301> getRM0301(String fdate, String tdate, String xcus, String xstatuspor, String xitem, String zid);

	public long deletePoordheaderByXpornum(String xpornum, String zid);

	public List<PoordDetail> searchPurchaseOrderAvailableItem(String xpornum, String xitem, String zid);
}
