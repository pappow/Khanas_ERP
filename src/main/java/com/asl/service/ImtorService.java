package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ImtorDetail;
import com.asl.entity.Imtorheader;
import com.asl.entity.Imtrn;
import com.asl.entity.PoordDetail;

@Component
public interface ImtorService {
	
	public long save(Imtorheader imtorHeader);
	public long update(Imtorheader imtorHeader);
	
	public long saveDetail(ImtorDetail imtorDetail);
	public long updateDetail(ImtorDetail imtorDetail);
	public long deleteDetail(ImtorDetail imtorDetail);
	
	public Imtorheader findImtorHeaderByXtornum(String xtornum);
	public List<Imtorheader> getAllImtorHeader();
	
	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow);
	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem);
	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum);

}
