package com.asl.service;

import org.springframework.stereotype.Component;

import com.asl.entity.ASLBusiness;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface BusinessService {

	public ASLBusiness findBById(String businessId);
}
