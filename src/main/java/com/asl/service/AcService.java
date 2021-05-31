package com.asl.service;

import java.util.List;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;

public interface AcService {
	
	//For Header Entity
	public long save(Acheader acheader);
	public long update(Acheader acheader);
	
	public List<Acheader> getAllAcheader();
	
	//For Detail Entity
	public long save(Acdetail acdetail);
	public long update(Acdetail acdetail);
	
	public List<Acdetail> getAllAcdetail();
	
}
