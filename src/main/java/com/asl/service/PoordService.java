package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Poorddetail;
import com.asl.entity.Poordheader;

@Component
public interface PoordService {
	public long save(Poordheader poordHeader);
	public long update(Poordheader poordHeader);
	public long updatePoordHeaderTotalAmt(Poorddetail poordDetail);

	public long saveDetail(Poorddetail poordDetail);
	public long updateDetail(Poorddetail poordDetail);

	public long deleteDetail(Poorddetail poordDetail);
	public long archiveAllPoordDetailByXpornum(String xpornum);
	public long countOfRequisitionDetailsByXpornum(String xpornum);

	public Poordheader findPoordHeaderByXpornum(String xpornum);
	public Poordheader findBranchPoordHeaderByXpornumForCentral(String xpornum);

	public Poorddetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow);
	public List<Poorddetail> findPoorddetailByXpornum(String xpornum);

	public List<Poordheader> getAllPoordHeaders();
	public List<Poordheader> getPoordHeadersByXtype(String xtype);

	public Poorddetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem);
}
