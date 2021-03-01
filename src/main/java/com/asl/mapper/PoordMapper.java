package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.PoordHeader;

@Mapper
public interface PoordMapper {
	public long savePoordHeader(PoordHeader poordHeader);
	public long updatePoordHeader(PoordHeader poordHeader);

	public List<PoordHeader> getAllPoordHeader(String zid);
	public PoordHeader findPoordHeaderByXpornum(String xpornum, String zid);

}
