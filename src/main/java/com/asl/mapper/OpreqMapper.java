package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opreqheader;
import com.asl.entity.Opreqdetail;

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Mapper
public interface OpreqMapper {
	//opreqheader
	public long saveOpreqheader(Opreqheader opreqheader);

	public long updateOpreqheader(Opreqheader opreqheader);

	public long deleteOpreqheader(String xdoreqnum, String zid);
	
	public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum, String zid);
	
	public List<Opreqheader> getAllOpreqheader(String zid);
	
	public List<Opreqheader> getAllStatusOpenOpreqheader(String zid);
	
	//opreqdetail
	public long saveOpreqdetail(Opreqdetail opreqdetail);

	public long updateOpreqdetail(Opreqdetail opreqdetail);

	public long deleteOpreqdetail(Opreqdetail opreqdetail);
	
	public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum, String zid);
	
	public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow, String zid);
	
	public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem, String zid);
	
}
