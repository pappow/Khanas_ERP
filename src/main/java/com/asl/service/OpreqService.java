package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;


@Component
public interface OpreqService {

		//opreqheader
		public long saveOpreqheader(Opreqheader opreqheader);

		public long updateOpreqheader(Opreqheader opreqheader);

		public long deleteOpreqheader(String xdoreqnum);
		
		public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum);
		
		public List<Opreqheader> getAllOpreqheader();
		
		public List<Opreqheader> getAllStatusOpenOpreqheader();
		
		
		//opreqdetail
		public long saveOpreqdetail(Opreqdetail opreqdetail);

		public long updateOpreqdetail(Opreqdetail opreqdetail);

		public long deleteOpreqdetail(Opreqdetail opreqdetail);
		
		public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum);
		
		public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow);
		
		public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem);
		
		public long updateOpreqHeaderTotalAmtAndGrandTotalAmt(String xdoreqnum);
		
}
