package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Imtrn;

@Component
public interface ImtrnService {
	
	public long save(Imtrn imtrn);
	public long update(Imtrn imtrn);
	
	public Imtrn findImtrnByXimtrnnum(String ximtrnnum);
	public List<Imtrn> getAllImtrn();

}
