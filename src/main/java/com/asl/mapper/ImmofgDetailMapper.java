package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ImmofgDetail;

@Mapper
public interface ImmofgDetailMapper {
	public long save(ImmofgDetail cabank);
	public long update(ImmofgDetail cabank);
	
	public ImmofgDetail findImmofgDetailByXtornumAndXrow(String xtornum, int xrow, String zid);
	public List<ImmofgDetail> getAllImmofgDetail(String zid);
}
