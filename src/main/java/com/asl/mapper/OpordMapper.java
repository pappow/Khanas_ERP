package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface OpordMapper {

	public long saveOpordHeader(Opordheader opordheader);
	public long updateOpordHeader(Opordheader opordheader);

	public long saveOpordDetail(Oporddetail oporddetail);
	public long updateOpordDetail(Oporddetail oporddetail);

	public Opordheader findOpordHeaderByXordernum(String xordernum, String zid);
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow, String zid);

	public List<Oporddetail> findOporddetailByXordernum(String xordernum, String zid);
	public List<Opordheader> getAllOpordheader(String zid);

}
