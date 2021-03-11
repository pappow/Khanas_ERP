package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opdoheader;
import com.asl.entity.Opdodetail;

@Mapper
public interface OpdoMapper {
	public long saveOpdoHeader(Opdoheader OpdoHeader);
	public long updateOpdoHeader(Opdoheader OpdoHeader);

	public long saveOpDoDetail(Opdodetail opdodetail);
	public long updateOpDoDetail(Opdodetail opdodetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum, String zid);
	public Opdodetail findOpDoDetailByXrowAndXdornum(int xrow, String xdornum, String zid);

	public List<Opdodetail> findOpDoDetailByXrow(int xrow, String zid);
	public List<Opdoheader> getAllOpdoHeader(String zid);
}
