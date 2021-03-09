package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.CaBank;

@Mapper
public interface CaBankMapper {
	public long save(CaBank cabank);
	public long update(CaBank cabank);
	
	public CaBank findCaBankByXbank(String xbank, String zid);
	public List<CaBank> getAllCaBank(String zid);
}
