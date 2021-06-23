package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xcodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Mapper
public interface XcodesMapper {

	public long saveXcodes(Xcodes xcodes);

	public long updateXcodes(Xcodes xcodes);

	public List<Xcodes> getAllXcodes(String zid, Boolean zactive);

	public List<Xcodes> findByXtype(String xtype, String zid, Boolean zactive);

	public List<Xcodes> findByXcode(String xcode, String zid, Boolean zactive);

	public Xcodes findByXtypesAndXcodes(String xtype, String xcode, String zid, Boolean zactive);

	public Xcodes getSeilingRecord(String direction, String zid);

	public long deleteXcodes(String xcode, String xtype, String zid);
}
