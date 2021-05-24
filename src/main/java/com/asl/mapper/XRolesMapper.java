package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xroles;
@Mapper
public interface XRolesMapper {
	public long saveXroles(Xroles xroles);
	
	public List<Xroles> getAllXroles(String zid);
	
}
