package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;

@Component
public interface AcService {

	// For Header Entity
	public long saveAcheader(Acheader acheader);

	public long updateAcheader(Acheader acheader);

	public List<Acheader> getAllAcheader();

	// For Detail Entity
	public long saveAcdetail(Acdetail acdetail);

	public long updateAcdetail(Acdetail acdetail);

	public List<Acdetail> getAllAcdetail();

}
