package com.asl.service;

import org.springframework.stereotype.Component;

import com.asl.entity.Imtrn;

@Component
public interface ImtrnService {
	
	public long save(Imtrn imtrn);
	public long update(Imtrn imtrn);
	
	public Imtrn findImtrnByImtrnnum(String ximtrnnum);

}
