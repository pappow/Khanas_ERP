package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Arhed;

@Mapper
public interface ArhedMapper {

	public long saveArhed(Arhed arhed);

	public long updateArhed(Arhed arhed);

	public Arhed findArhedByXvoucher(String xvoucher, String zid);

	public List<Arhed> getAllArhed(String zid);

	// Supplier Opening Entry
	public Arhed findObapByXcus(String xcus, String xtrn, String zid);

	public List<Arhed> getAllObaps(String xtrn, String zid);

	public List<Arhed> getAllArhedByXtrn(String xtrn, String zid);

	// Supplier Adjustment
	public Arhed findAdapByXcus(String xcus, String xtrn, String zid);

	public List<Arhed> getAllAdaps(String xtrn, String zid);

	// Customer Adjustment
	public Arhed findAdarByXcus(String xcus, String xtrn, String zid);

	public List<Arhed> getAllAdars(String xtrn, String zid);

	// Search Field
	public List<Arhed> findByXstaff(String xstaff, String zid);

	public long deleteVoucher(String xvoucher, String zid);
}
