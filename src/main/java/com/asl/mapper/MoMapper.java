package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Moheader;
import com.asl.entity.Modetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface MoMapper {
	
	public long saveMoHeader(Moheader moheader);
	public long updateMoHeader(Moheader moheader);

	public long saveMoDetail(Modetail modetail);
	public long updateMoDetail(Modetail modetail);

	public Moheader findMoHeaderByXbatch(String xbatch, String zid);
	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch, String zid);

	public List<Modetail> findModetailByXrow(int xrow, String zid);
	public List<Moheader> getAllMoheader(String zid);

}
