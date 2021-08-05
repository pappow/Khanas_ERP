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
	
	public List<Arhed> getAllArhedByXtrn(String xtrn);
	
	public List<Arhed> getAllArhedByXtype(String xtype);

	// Supplier Opening Entry
	public Arhed findObapByXcus(String xcus);
	
	public List<Arhed> getAllObaps();
	
	// Customer Opening Entry
	public Arhed findObarByXcus(String xcus);

	public List<Arhed> getAllObars();
	

	// Supplier Adjustment
	public Arhed findAdapByXcus(String xcus);
	
	public List<Arhed> getAllAdaps();

	// Customer Adjustment
	public Arhed findAdarByXcus(String xcus);
	
	public List<Arhed> getAllAdars();

	// Search Field
	public List<Arhed> findByXstaff(String xstaff);

	public long deleteVoucher(String xvoucher);

	public List<Arhed> getAllArhedByXtrnarhedAndXtype(String xtrnarhed, String xtype);
}
