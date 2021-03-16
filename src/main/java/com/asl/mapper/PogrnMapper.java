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

	public long savePogrnDetail(PogrnDetail pogrnDetail);
	public long updatePogrnDetail(PogrnDetail pogrnDetail);
	public long deletePogrnDetail(PogrnDetail pogrnDetail);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum, String zid);
	public PogrnHeader findPogrnHeaderByXpornum(String xpornum, String zid);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow, String zid);
	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum, String zid);

	public List<PogrnHeader> getAllPogrnHeader(String zid);
	
	public void procInventory(String zid, String user, String xgrnnum, String xpornum);
	public void procArhed(String zid, String user, String xgrnnum);
}
