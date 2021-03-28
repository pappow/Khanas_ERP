package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;

@Component
public interface PocrnService {

	public long save(Pocrnheader pocrnheader);

	public long update(Pocrnheader pocrnheader);

	public long saveDetail(Pocrndetail pocrndetail);

	public long updateDetail(Pocrndetail pocrndetail);

	public long deleteDetail(Pocrndetail pocrndetail);

	public List<Pocrnheader> getAllPocrnheader();

	public List<Pocrndetail> findPocrnDetailByXcrnnum(String xcrnnum);

	public Pocrnheader findPocrnHeaderByXcrnnum(String xcrnnum);

	public Pocrnheader findPocrnHeaderByXgrnnum(String xgrnnum);

	public Pocrndetail findPocrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow);

	// Procedure Calls
	public void procConfirmCRN(String xcrnnum, String p_seq);

	public void procIssuePricing(String xtrnnum, String xwh, String p_seq);

	public void procTransferPRtoAP(String xcrnnum, String p_seq);

}
