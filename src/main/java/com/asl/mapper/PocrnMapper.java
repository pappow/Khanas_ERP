package com.asl.mapper;

import java.util.List;

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;

public interface PocrnMapper {
	public long savePocrnHeader(Pocrnheader pocrnheader);
	public long updatePocrnHeader(Pocrnheader pocrnheader);
	
	public long savePocrnDetail(Pocrndetail pocrndetail);
	public long updatePocrnDetail(Pocrndetail pocrndetail);

	public Pocrnheader findPocrnHeaderByXcrnnum(String xcrnnum, String zid);
	public Pocrndetail findPocrndetailByXrowAndXcrnnum(int xrow, String xcrnnum, String zid);

	public List<Pocrndetail> findPocrndetailByXrow(int xrow, String zid);
	public List<Pocrnheader> getAllPocrnheader(String zid);
}
