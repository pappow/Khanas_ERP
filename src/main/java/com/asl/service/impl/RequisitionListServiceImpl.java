package com.asl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.RequisitionList;
import com.asl.mapper.RequisitionListMapper;
import com.asl.service.RequisitionListService;

@Service
public class RequisitionListServiceImpl extends AbstractGenericService implements RequisitionListService{

	@Autowired private RequisitionListMapper requisitionListMapper;

	@Override
	public List<RequisitionList> getAllBranchesRequisitionList() {
		return requisitionListMapper.getAllBranchesRequisitionList();
	}

}
