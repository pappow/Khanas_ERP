package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Pogrndetail;
import com.asl.entity.Pogrnheader;

@Component
public interface PogrnService {
	
	public long save(Pogrnheader pogrnHeader);
	public long update(Pogrnheader pogrnHeader);
	public long updatePogrnHeaderTotalAmt(Pogrndetail pogrnDetail);

	public long saveDetail(Pogrndetail pogrnDetail);
	public long updateDetail(Pogrndetail pogrnDetail);

	public long deleteDetail(Pogrndetail pogrnDetail);

	public Pogrnheader findPogrnHeaderByXgrnnum(String xgrnnum);
	public Pogrnheader findPogrnHeaderByXpornum(String xpornum);

	public Pogrndetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow);
	public List<Pogrndetail> findPogrnDetailByXgrnnum(String xgrnnum);

	public List<Pogrnheader> getAllPogrnHeaders();

}
