package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Vatait;

@Component
public interface VataitService {
	public long save(Vatait vatait);
	public long update(Vatait vatait);
	public long delete(Vatait vatait);
	
	public Vatait findVataitByXvatait(String xvatait);
	public List<Vatait> getAllVatait();
}
