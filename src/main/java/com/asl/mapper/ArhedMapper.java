package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Arhed;
import com.asl.entity.PoordHeader;

@Mapper
public interface ArhedMapper {
	
	public long saveArhed(Arhed arhed);
	public long updateArhed(Arhed arhed);
	
	public Arhed findArhedByXvoucher(String xvoucher, String zid);
	public List<Arhed> getAllArhed(String zid);
	
	//Supplier Opening Entry
	public Arhed findObapByXcus(String xcus, String xtrn, String zid);
	public List<Arhed> getAllObaps(String xtrn, String zid);

}
