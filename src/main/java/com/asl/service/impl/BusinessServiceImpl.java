package com.asl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ASLBusiness;
import com.asl.mapper.BusinessMapper;
import com.asl.service.BusinessService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class BusinessServiceImpl extends AbstractGenericService implements BusinessService{

	@Autowired private BusinessMapper businessMapper;

	@Override
	public ASLBusiness findBById(String businessId) {
		if(businessId == null) return null;
		return businessMapper.findByZid(businessId);
	}

}
