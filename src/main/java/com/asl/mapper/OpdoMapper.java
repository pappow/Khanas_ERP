package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;

@Mapper
public interface OpdoMapper {
	public long saveOpDoHeader(Opdoheader opdoheader);
	public long updateOpDoHeader(Opdoheader opdoheader);

	public long saveOpDoDetail(Opdodetail opdodetail);
	public long updateOpDoDetail(Opdodetail opdodetail);

	public Opdoheader findOpDoHeaderByXdornum(String xdornum, String zid);
	public Opdodetail findOpDoDetailByXrowAndXdornum(int xrow, String xdornum, String zid);

	public List<Opdodetail> findOpDoDetailByXrow(int xrow, String zid);
	public List<Opdoheader> getAllOpDoHeader(String zid);
}
