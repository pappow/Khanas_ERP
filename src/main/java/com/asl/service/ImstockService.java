package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;
import com.asl.entity.Imstock;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Component
public interface ImstockService {

	public List<Imstock> searchXitem(String xitem);
}
