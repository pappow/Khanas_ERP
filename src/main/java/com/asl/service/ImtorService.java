package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;

@Component
public interface ImtorService {

	public long save(ImtorHeader imtorHeader);

	public long update(ImtorHeader imtorHeader);

	public long saveDetail(ImtorDetail imtorDetail);

	public long updateDetail(ImtorDetail imtorDetail);

	public long deleteDetail(ImtorDetail imtorDetail);

	public ImtorHeader findImtorHeaderByXtornum(String xtornum);

	public ImtorHeader findImtorHeaderByXchalanref(String xchalanref);

	public List<ImtorHeader> getAllImtorHeader();

	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow);

	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem);

	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum);

	public List<ImtorDetail> findImtorDetailByXtornumAndXchalanref(String xtornum, String xchalanref);

	public long updateImtorHeaderTotalAmt(ImtorDetail imtorDetail);

	// Procedure calls
	public void procConfirmTO(String xtornum, String p_action, String p_seq);

	public long deleteImtorDetailByXtornum(String xtornum);

}
