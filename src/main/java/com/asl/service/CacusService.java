package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Component
public interface CacusService {

	public long save(Cacus cacus);
	public long update(Cacus cacus);

	public Cacus findByXcus(String xcus);
	public List<Cacus> findByXtype(String xtype);

	public List<Cacus> getAllCacus();

	public List<Cacus> searchCacus(String xtype, String xcus);
}
