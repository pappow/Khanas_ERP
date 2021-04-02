package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;

@Component
public interface PogrnService {

	public long save(PogrnHeader pogrnHeader);

	public long update(PogrnHeader pogrnHeader);

	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail);
	
	public long updatePogrnHeaderTotalAmtAndGrandTotalAmt(String xgrnnum);

	public long saveDetail(PogrnDetail pogrnDetail);

	public long updateDetail(PogrnDetail pogrnDetail);

	public long deleteDetail(PogrnDetail pogrnDetail);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum);

	public PogrnHeader findPogrnHeaderByXpornum(String xpornum);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow);

	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum);

	public List<PogrnHeader> getAllPogrnHeaders();

	// Procedure Call
	public void procInventory(String xgrnnum, String xpornum, String p_seq);

	public void procTransferPOtoAP(String xgrnnum, String p_seq);

	// Search
	public List<PogrnHeader> searchPoord(String xpornum);
	
	//public List<PogrnHeader> search(String xwh,String sup,String status,String fromDate,String toDate);
}
