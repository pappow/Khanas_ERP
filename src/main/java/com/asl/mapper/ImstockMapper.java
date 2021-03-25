package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imstock;

@Mapper
public interface ImstockMapper {
	
	public List<Imstock> searchXitem(String xitem, String zid);
}
