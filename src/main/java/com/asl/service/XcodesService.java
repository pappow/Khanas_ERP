package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Xcodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Component
public interface XcodesService {

	public long save(Xcodes xcodes);
	public long update(Xcodes xcodes);
	public List<Xcodes> getAllXcodes();
	public List<Xcodes> findByXtype(String xType);
	public List<Xcodes> findByXcode(String xCode);
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes);

	public Xcodes getSeilingRecord(String direction);
}
