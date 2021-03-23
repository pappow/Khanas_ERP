package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;

@Component
public interface OpdoService {
	
	public long save(Opdoheader opdoHeader);
	public long update(Opdoheader opdoHeader);
	public long updateOpdoHeaderTotalAmt(Opdodetail opdoDetail);
	public long updateOpdoHeaderTotalAmtAndGrandTotalAmt(Opdodetail opdodetail);
	public long updateOpdoHeaderGrandTotalAmt(Opdoheader opdoheader);
	
	public long saveDetail(Opdodetail opdoDetail);
	public long updateDetail(Opdodetail opdoDetail);
	public long deleteDetail(Opdodetail opdoDetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum);
	public List<Opdoheader> getAllOpdoHeader();

	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow);
	public Opdodetail findOpdoDetailByXdornumAndXitem(String xdornum, String xitem);
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum);
	

	public List<Opdoheader> findAllInvoiceOrder(String xtypetrn, String xtrn, String xstatus, Date date);
	public List<Opdoheader> findAllInvoiceOrderByChalan(String xtypetrn, String xtrn, String xchalanref);
	public List<Opdoheader> findAllOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);

	//Procedure Calls
	public void procConfirmDO(String xdornum, String p_seq);
	public void procIssuePricing(String xdocnum, String xwh, String p_seq);
	public void procTransferOPtoAR(String xdocnum, String p_screen, String p_seq);
}
