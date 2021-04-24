package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;

@Mapper
public interface PocrnMapper {
	public long savePocrnHeader(Pocrnheader pocrnheader);
	public long updatePocrnHeader(Pocrnheader pocrnheader);
	
	public long savePocrnDetail(Pocrndetail pocrndetail);
	public long updatePocrnDetail(Pocrndetail pocrndetail);
	public long deletePocrnDetail(Pocrndetail pocrndetail);

	public List<Pocrnheader> getAllPocrnheader(String zid);
	public List<Pocrndetail> findPocrnDetailByXcrnnum(String xcrnnum, String zid);

	public Pocrnheader findPocrnHeaderByXcrnnum(String xcrnnum, String zid);
	public Pocrnheader findPocrnHeaderByXgrnnum(String xgrnnum, String zid);
	public Pocrndetail findPocrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow, String zid);
	
	//Procedures
	public void procConfirmCRN(String zid, String user, String xcrnnum, String p_seq);	
	public void procIssuePricing(String zid, String user, String xtrnnum, String xwh, String p_seq);
	public void procTransferPRtoAP(String zid, String user, String xcrnnum, String p_seq);
	
	public List<Pocrnheader> findPocrnXstatuscrn(String xstatuscrn, String zid);
}
