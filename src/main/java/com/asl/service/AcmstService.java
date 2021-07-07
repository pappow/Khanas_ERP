package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Acmst;

@Component
public interface AcmstService {

	public long save(Acmst acmst);

	public long update(Acmst acmst);

	public List<Acmst> getAllAcmst();

	public Acmst findByXacc(String xacc);

	public long delete(String xacc);

}
