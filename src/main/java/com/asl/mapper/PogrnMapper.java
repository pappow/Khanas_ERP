package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Pogrndetail;
import com.asl.entity.Pogrnheader;

@Mapper
public interface PogrnMapper {
	public long savePogrnHeader(Pogrnheader pogrnHeader);
	public long updatePogrnHeader(Pogrnheader pogrnHeader);
	public long updatePogrnHeaderTotalAmt(Pogrndetail pogrnDetail);

	public long savePogrnDetail(Pogrndetail pogrnDetail);
	public long updatePogrnDetail(Pogrndetail pogrnDetail);
	public long deletePogrnDetail(Pogrndetail pogrnDetail);

	public Pogrnheader findPogrnHeaderByXgrnnum(String xgrnnum, String zid);
	public Pogrnheader findPogrnHeaderByXpornum(String xpornum, String zid);

	public Pogrndetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow, String zid);
	public List<Pogrndetail> findPogrnDetailByXgrnnum(String xgrnnum, String zid);

	public List<Pogrnheader> getAllPogrnHeader(String zid);
}
