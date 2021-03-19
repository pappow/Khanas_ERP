package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opdoheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.Opdodetail;

@Mapper
public interface OpdoMapper {
	public long saveOpdoHeader(Opdoheader opdoheader);
	public long updateOpdoHeader(Opdoheader opdoheader);
	public long updateOpdoHeaderTotalAmt(Opdodetail opdodetail);
	public long updateOpdoHeaderGrandTotalAmt(Opdoheader opdoheader);	

	public long saveOpdoDetail(Opdodetail opdodetail);
	public long updateOpdoDetail(Opdodetail opdodetail);
	public long deleteOpdoDetail(Opdodetail opdodetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum, String zid);
	public List<Opdoheader> getAllOpdoHeader(String zid);

	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow, String zid);
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum, String zid);
	
	//Procedure Calls
	public void procConfirmDO(String zid, String user, String xdornum, String p_seq);
	public void procIssuePricing(String zid, String user, String xdocnum, String xwh, String p_seq);
	public void procTransferOPtoAR(String zid, String user, String xdocnum, String p_screen, String p_seq);
}
