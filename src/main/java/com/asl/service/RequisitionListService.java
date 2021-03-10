package com.asl.service;

import java.util.Date;
import java.util.List;

import com.asl.entity.Poorddetail;
import com.asl.model.BranchesRequisitions;

public interface RequisitionListService {

	public List<BranchesRequisitions> getAllBranchesRequisitions(Date xdate);
	public List<BranchesRequisitions> getAllBranchesRequisitionDetails(Date xdate);
	public List<Poorddetail> getDetailListByXpornumAndBranchZid(String xpornum, String branchzid);
}
