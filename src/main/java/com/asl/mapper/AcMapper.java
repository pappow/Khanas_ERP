package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;

@Mapper
public interface AcMapper {

	// For Header Entity
	public long saveAcheader(Acheader acheader);

	public long updateAcheader(Acheader acheader);

	public List<Acheader> getAllAcheader(String zid);

	// For Detail Entity
	public long saveAcdetail(Acdetail acdetail);

	public long updateAcdetail(Acdetail acdetail);

	public List<Acdetail> getAllAcdetail(String zid);

}
