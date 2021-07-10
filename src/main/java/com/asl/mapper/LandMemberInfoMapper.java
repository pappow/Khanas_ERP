package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;
import com.asl.entity.LandComEvent;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Mapper
public interface LandMemberInfoMapper {

	public long save(Cacus cacus);

	public long update(Cacus cacus);
	
	public long delete(Cacus cacus);

	public Cacus findByXcus(String xcus, String zid);

	public Cacus findByXphone(String xphone, String zid);

	public List<Cacus> findByXtype(String xtype, String zid);

	public List<Cacus> getAllCacus(String zid);
	
	public List<Cacus> searchCacus(String xtype, String xcus, String zid);

	public List<Cacus> searchXorg(String xorg, String zid);

	public List<Cacus> searchXgcus(String xgcus, String zid);

	public Cacus findCacusByXcuszid(String xcuszid, String zid);

	public long deleteCacus(String xcus, String zid);
	
	public List<Cacus> getAllCacusByPrefix(String xtypetrn, String zid);
	
	public List<Cacus> getAllLandMembers(String zid);
}
