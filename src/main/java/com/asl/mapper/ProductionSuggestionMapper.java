package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.model.ProductionSuggestion;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Mapper
public interface ProductionSuggestionMapper {

	List<ProductionSuggestion> getProductionSuggestion();
	public void createSuggestion(String xordernum, String zid);
}
