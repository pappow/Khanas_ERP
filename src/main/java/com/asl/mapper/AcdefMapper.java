package com.asl.mapper;

import java.util.List;

import com.asl.entity.Acdef;

public interface AcdefMapper {
	public long saveAcdef(Acdef acdef);
	public long updateAcdef(Acdef acdef);
	
	public List<Acdef> getAllAcdef(String zid);

}
