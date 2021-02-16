package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ListHead;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface ListMapper {

	public List<ListHead> getAllListHead();
}
