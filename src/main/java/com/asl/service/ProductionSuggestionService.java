package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.model.ProductionSuggestion;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Component
public interface ProductionSuggestionService {

	public void createSuggestion(String xordernum);
	List<ProductionSuggestion> getProductionSuggestion(String chalan, Date xdate);
	long deleteSuggestion(String chalan, Date xdate);
}
