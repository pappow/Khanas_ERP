package com.asl.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ZbusinessMapper {

	public long save(Zbusiness zbusiness);
	public long update(Zbusiness zbusiness);

	Zbusiness findByZid(String zid);
}
