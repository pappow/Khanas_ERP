package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;

@Mapper
public interface AcMapper {

	// For Header Entity
	public long saveAcheader(Acheader acheader);

	public long updateAcheader(Acheader acheader);

	public long deleteAcheader(String xvoucher, String zid);

	public List<Acheader> getAllAcheader(String zid);

	public Acheader findAcheaderByXvoucher(String xvoucher, String zid);

	// For Detail Entity
	public long saveAcdetail(Acdetail acdetail);

	public long updateAcdetail(Acdetail acdetail);

	public long deleteAcdetail(int xrow, String xvoucher, String zid);

	public List<Acdetail> getAllAcdetail(String zid);

	public Acdetail findAcdetailByXrowAndXvoucher(int xrow, String xvoucher, String zid);

	public List<Acdetail> findAcdetailsByXvoucher(String xvoucher, String zid);

	public long updateAcheaderXstatusjv(String xvoucher, String zid);

	// Proc
	public void procAcVoucherPost(String zid, String user, Integer xyear, Integer xper, String xfvoucher, String xtvoucher);

	public void procAcVoucherUnPost(String zid, String user, Integer xyear, Integer xper, String xfvoucher, String xtvoucher);
}
