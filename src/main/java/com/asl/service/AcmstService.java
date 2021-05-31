package com.asl.service;

import java.util.List;

import com.asl.entity.Acmst;

public interface AcmstService {
	
	public long save(Acmst Acmst);
	public long update(Acmst Acmst);
	
	public List<Acmst> getAllAcmst();


}
