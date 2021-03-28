package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;

@Mapper
public interface OpcrnMapper {
	public long saveOpcrnHeader(Opcrnheader opcrnheader);
	public long updateOpcrnHeader(Opcrnheader opcrnheader);
	
	public long saveOpcrnDetail(Opcrndetail opcrndetail);
	public long updateOpcrnDetail(Opcrndetail opcrndetail);
	public long deleteOpcrnDetail(Opcrndetail opcrndetail);

	public List<Opcrnheader> getAllOpcrnheader(String zid);
	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum, String zid);

	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum, String zid);
	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum, String zid);
	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow, String zid);;
	//public Pocrnheader findPocrnHeaderByXgrnnum(String xgrnnum, String zid);
	
	//Procedure Calls
}
