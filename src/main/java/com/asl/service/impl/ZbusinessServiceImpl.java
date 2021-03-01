package com.asl.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Zbusiness;
import com.asl.mapper.ZbusinessMapper;
import com.asl.service.ZbusinessService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class ZbusinessServiceImpl extends AbstractGenericService implements ZbusinessService{

	@Autowired private ZbusinessMapper businessMapper;

	@Override
	public Zbusiness findBById(String zid) {
		if(StringUtils.isBlank(zid)) return null;
		return businessMapper.findByZid(zid);
	}

}
