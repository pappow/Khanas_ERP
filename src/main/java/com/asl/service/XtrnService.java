package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Xtrn;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Component
public interface XtrnService {

	public long save(Xtrn xtrn);
	public long update(Xtrn xtrn);

	public List<Xtrn> getAllXtrn();
	public List<Xtrn> findByXtypetrn(String xtypetrn);
	public List<Xtrn> findByXtrn(String xtrn);
	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn);

}
