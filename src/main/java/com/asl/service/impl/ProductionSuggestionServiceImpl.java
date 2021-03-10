package com.asl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.mapper.ProductionSuggestionMapper;
import com.asl.model.ProductionSuggestion;
import com.asl.service.ProductionSuggestionService;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Service
public class ProductionSuggestionServiceImpl extends AbstractGenericService implements ProductionSuggestionService{

	@Autowired private ProductionSuggestionMapper productionSuggestionMapper;

	@Override
	public List<ProductionSuggestion> getProductionSuggestion() {
		return productionSuggestionMapper.getProductionSuggestion();
	}

	@Override
	public void createSuggestion(String xordernum) {
		productionSuggestionMapper.createSuggestion(xordernum, sessionManager.getBusinessId());
	}
}
