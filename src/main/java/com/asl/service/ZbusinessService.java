package com.asl.service;

import org.springframework.stereotype.Component;

import com.asl.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface ZbusinessService {

	public Zbusiness findBById(String zid);
}
