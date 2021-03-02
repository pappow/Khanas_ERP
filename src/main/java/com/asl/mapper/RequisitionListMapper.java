package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.RequisitionList;

@Mapper
public interface RequisitionListMapper {

	public List<RequisitionList> getAllBranchesRequisitionList();
}
