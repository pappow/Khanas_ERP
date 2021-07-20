package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;

@Mapper
public interface PogrnMapper {
	public long savePogrnHeader(PogrnHeader pogrnHeader);

	public long updatePogrnHeader(PogrnHeader pogrnHeader);

	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail);

	public long updatePogrnHeaderTotalAmtAndGrandTotalAmt(String xgrnnum, String zid);

	public long savePogrnDetail(PogrnDetail pogrnDetail);

	public long updatePogrnDetail(PogrnDetail pogrnDetail);

	public long deletePogrnDetail(PogrnDetail pogrnDetail);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum, String zid);

	public List<PogrnHeader> findPogrnHeaderByXpornum(String xpornum, String zid);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow, String zid);

	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum, String zid);

	public List<PogrnHeader> getAllPogrnHeader(String zid);

	public void procInventory(String zid, String user, String xgrnnum, String xpornum, String p_seq);

	public void procTransferPOtoAP(String zid, String user, String xgrnnum, String p_seq);

	public List<PogrnHeader> searchPoord(String xpornum, String zid);

	public long archiveDetailsByXgrnnum(String xgrnnum, String zuuserid, String zid);

	public long countOfPogrndetailByXgrnnum(String xgrnnum, String zid);
}
