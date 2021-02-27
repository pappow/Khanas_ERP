package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xtrn;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Mapper
public interface XtrnMapper {

	public long save(Xtrn xtrn);
	public long update(Xtrn xtrn);

	public List<Xtrn> getAllXtrn(String zid);
	public List<Xtrn> findByXtypetrn(String xtypetrn, String zid);
	public List<Xtrn> findByXtrn(String xtrn, String zid);
	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn, String zid);

}
