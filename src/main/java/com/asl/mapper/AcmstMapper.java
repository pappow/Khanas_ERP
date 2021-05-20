package com.asl.mapper;

import java.util.List;

import com.asl.entity.Acmst;

public interface AcmstMapper {
	public long saveAcmst(Acmst Acmst);
	public long updateAcmst(Acmst Acmst);
	
	public List<Acmst> getAllAcmst(String zid);

}
