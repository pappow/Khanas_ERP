package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;

@Component
public interface PoordService {
	public long save(PoordHeader poordHeader);

	public long update(PoordHeader poordHeader);

	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail);

	public long saveDetail(PoordDetail poordDetail);

	public long updateDetail(PoordDetail poordDetail);

	public long deleteDetail(PoordDetail poordDetail);

	public long archiveAllPoordDetailByXpornum(String xpornum);

	public long countOfRequisitionDetailsByXpornum(String xpornum);

	public PoordHeader findPoordHeaderByXpornum(String xpornum);

	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid);

	public PoordDetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow);

	public List<PoordDetail> findPoorddetailByXpornum(String xpornum);

	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid);

	public List<PoordHeader> getAllPoordHeaders();

	public List<PoordHeader> getPoordHeadersByXtype(String xtype);

	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem);
}
