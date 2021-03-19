package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;

@Component
public interface OpdoService {
	
	public long save(Opdoheader opdoHeader);
	public long update(Opdoheader opdoHeader);
	public long updateOpdoHeaderTotalAmt(Opdodetail opdoDetail);
	
	public long saveDetail(Opdodetail opdoDetail);
	public long updateDetail(Opdodetail opdoDetail);
	public long deleteDetail(Opdodetail opdoDetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum);
	public List<Opdoheader> getAllOpdoHeader();

	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow);
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum);
}
