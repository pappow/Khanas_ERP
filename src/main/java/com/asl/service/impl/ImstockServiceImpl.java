package com.asl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Imstock;
import com.asl.mapper.ImstockMapper;
import com.asl.service.ImstockService;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Service
public class ImstockServiceImpl extends AbstractGenericService implements ImstockService {

	@Autowired
	private ImstockMapper imstockMapper;

	@Override
	public List<Imstock> searchXitem(String xitem) {
		return imstockMapper.searchXitem(xitem.toUpperCase(), sessionManager.getBusinessId());
	}
}
