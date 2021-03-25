package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Mapper
public interface CacusMapper {

	public long save(Cacus cacus);
	public long update(Cacus cacus);

	public Cacus findByXcus(String xcus, String zid);
	public List<Cacus> findByXtype(String xtype, String zid);

	public List<Cacus> getAllCacus(String zid);

	public List<Cacus> searchCacus(String xtype, String xcus, String zid);
	public List<Cacus> searchXorg(String xorg, String zid);
}
