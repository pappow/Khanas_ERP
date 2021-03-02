package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;

@Component
public interface PoordService {
	public long save(PoordHeader poordHeader);
	public long update(PoordHeader poordHeader);
	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail);

	public long saveDetail(PoordDetail poordDetail);
	public long updateDetail(PoordDetail poordDetail);

	public long deleteDetail(PoordDetail poordDetail);

	public PoordHeader findPoordHeaderByXpornum(String xpornum);

	public PoordDetail findPoorddetailByXportNumAndXrow(String xpornum, int xrow);
	public List<PoordDetail> findPoorddetailByXpornum(String xpornum);

	public List<PoordHeader> getAllPoordHeaders();
}
