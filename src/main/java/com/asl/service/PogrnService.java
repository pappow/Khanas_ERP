package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;

@Component
public interface PogrnService {
	
	public long save(PogrnHeader pogrnHeader);
	public long update(PogrnHeader pogrnHeader);
	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail);

	public long saveDetail(PogrnDetail pogrnDetail);
	public long updateDetail(PogrnDetail pogrnDetail);

	public long deleteDetail(PogrnDetail pogrnDetail);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum);
	public PogrnHeader findPogrnHeaderByXpornum(String xpornum);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow);
	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum);

	public List<PogrnHeader> getAllPogrnHeaders();

}