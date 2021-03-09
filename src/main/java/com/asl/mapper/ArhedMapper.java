package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Arhed;

@Mapper
public interface ArhedMapper {
	
	public long saveArhed(Arhed arhed);
	public long updateArhed(Arhed arhed);
	
	public Arhed findArhedByXvoucher(String xvoucher, String zid);
	public List<Arhed> getAllArhed(String zid);

}
