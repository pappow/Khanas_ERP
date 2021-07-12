package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Acdef;
import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;

@Component
public interface AcService {

	// For Header Entity
	public long saveAcheader(Acheader acheader);

	public long updateAcheader(Acheader acheader);

	public long deleteAcheader(String xvoucher);

	public List<Acheader> getAllAcheader();

	public Acheader findAcheaderByXvoucher(String xvoucher);

	// For Detail Entity
	public long saveAcdetail(Acdetail acdetail);

	public long updateAcdetail(Acdetail acdetail);

	public long deleteAcdetail(int xrow, String xvoucher);

	public List<Acdetail> getAllAcdetail();

	public Acdetail findAcdetailByXrowAndXvoucher(int xrow, String xvoucher);

	public List<Acdetail> findAcdetailsByXvoucher(String xvoucher);

	public long updateAcheaderXstatusjv(String xvoucher);

	public void procAcVoucherPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher);

	public void procAcVoucherUnPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher);

	public Acheader setXyearAndXper(Acheader acheader, Acdef acdef);

}
