package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.MoHeader;
import com.asl.entity.MoDetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface MoMapper {
	
	public long saveMoHeader(MoHeader moheader);
	public long updateMoHeader(MoHeader moheader);

	public long saveMoDetail(MoDetail modetail);
	public long updateMoDetail(MoDetail modetail);

	public MoHeader findMoHeaderByXbatch(String xbatch, String zid);
	public MoDetail findModetailByXrowAndXbatch(int xrow, String xbatch, String zid);

	public List<MoDetail> findModetailByXrow(int xrow, String zid);
	public List<MoHeader> getAllMoheader(String zid);

}
