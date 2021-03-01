package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PoordHeader;

@Component
public interface PoordService {
	public long save(PoordHeader poordHeader);
	public long update(PoordHeader poordHeader);
	public List<PoordHeader> getAllPoordHeaders();
	
	public PoordHeader findPoordHeaderByXpornum(String xpornum);
}
