package com.asl.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ASLBusiness;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface BusinessMapper {

	ASLBusiness findByZid(String zid);
}
