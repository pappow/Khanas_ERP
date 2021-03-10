package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Poorddetail;
import com.asl.entity.Poordheader;

@Mapper
public interface PoordMapper {

	public long savePoordHeader(Poordheader poordHeader);
	public long updatePoordHeader(Poordheader poordHeader);
	public long updatePoordHeaderTotalAmt(Poorddetail poordDetail);

	public long savePoordDetail(Poorddetail poordDetail);
	public long updatePoordDetail(Poorddetail poordDetail);
	public long deletePoordDetail(Poorddetail poordDetail);
	public long archiveAllPoordDetailByXpornum(String xpornum, String zid);
	public long countOfRequisitionDetailsByXpornum(String xpornum, String zid);

	public Poordheader findPoordHeaderByXpornum(String xpornum, String zid);
	public Poordheader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid);

	public Poorddetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow, String zid);
	public List<Poorddetail> findPoorddetailByXpornum(String xpornum, String zid, String centralzid);

	public List<Poordheader> getAllPoordHeader(String zid);
	public List<Poordheader> getPoordHeadersByXtype(String xtype, String zid);

	public Poorddetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem, String zid);

}
