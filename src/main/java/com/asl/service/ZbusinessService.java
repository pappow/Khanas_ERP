package com.asl.service;

import org.springframework.stereotype.Component;

import com.asl.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface ZbusinessService {

	public long save(Zbusiness zbusiness);
	public long update(Zbusiness zbusiness);
	public Zbusiness findBById(String zid);
}
