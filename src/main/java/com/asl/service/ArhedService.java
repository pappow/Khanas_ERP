package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Arhed;

@Component
public interface ArhedService {
	public long save(Arhed arhed);

	public long update(Arhed arhed);

	public Arhed findArhedByXvoucher(String xvoucher);

	public List<Arhed> getAllArheds();

	// Supplier Opening Entry
	public Arhed findObapByXcus(String xcus);

	public List<Arhed> getAllObaps();

	public List<Arhed> getAllArhedByXtrn(String xtrn);

	// Supplier Adjustment
	public Arhed findAdapByXcus(String xcus);

	public List<Arhed> getAllAdaps();

	// Customer Adjustment
	public Arhed findAdarByXcus(String xcus);

	public List<Arhed> getAllAdars();
}
