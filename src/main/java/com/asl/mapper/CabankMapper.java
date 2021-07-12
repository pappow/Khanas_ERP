package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cabank;

@Mapper
public interface CabankMapper {

	public long save(Cabank cabank);

	public long update(Cabank cabank);

	public long delete(String xbank, String zid);

	public Cabank findCaBankByXbank(String xbank, String zid);

	public List<Cabank> getAllCaBank(String zid);
}
