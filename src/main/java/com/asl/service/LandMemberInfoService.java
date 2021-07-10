package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;
import com.asl.entity.ImtorHeader;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Component
public interface LandMemberInfoService {

	public long save(Cacus cacus);

	public long update(Cacus cacus);

	public long delete(Cacus cacus);
	
	public Cacus findByXcus(String xcus);

	public List<Cacus> getAllCacus();

	public List<Cacus> searchCacus(String xtype, String xcus);

	public Cacus findByXphone(String xphone);

	public List<Cacus> searchXorg(String xorg);

	public List<Cacus> searchXgcus(String xgcus);

	public Cacus findCacusByXcuszid(String xcuszid);

	public long deleteCacus(String xcus);
	
	public List<Cacus> getAllCacusByPrefix(String xtypetrn);
	
	public List<Cacus> getAllLandMembers();
}
