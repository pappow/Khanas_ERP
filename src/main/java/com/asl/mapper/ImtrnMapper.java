package com.asl.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imtrn;

@Mapper
public interface ImtrnMapper {
	
	public long saveImtrn(Imtrn imtrn);
	public long updateImtrn(Imtrn imtrn);
	
	public Imtrn findImtrnByImtrnnum(String imtrnnum, String zid);
	
}
