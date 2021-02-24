package com.asl.service;

import org.springframework.stereotype.Component;

import com.asl.model.pagination.PagingTable;

/**
 * @author Zubayer Ahamed
 * @since Feb 23, 2021
 */
@Component
public interface PaginationService {

	public PagingTable getPagingTable();
}
