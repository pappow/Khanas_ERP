package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Immofgdetail;

@Mapper
public interface ImmofgDetailMapper {
	public long save(Immofgdetail cabank);
	public long update(Immofgdetail cabank);
	
	public Immofgdetail findImmofgDetailByXtornumAndXrow(String xtornum, int xrow, String zid);
	public List<Immofgdetail> getAllImmofgDetail(String zid);
}
