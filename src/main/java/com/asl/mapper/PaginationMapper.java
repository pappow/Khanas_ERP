package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zubayer Ahamed
 * @since Feb 23, 2021
 */
@Mapper
public interface PaginationMapper {

	public List<Map<String, Object>> getResultMap(List<String> columns, String businessId);
}
