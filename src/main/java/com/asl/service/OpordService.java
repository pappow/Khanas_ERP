package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.model.BranchesRequisitions;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Component
public interface OpordService {

	public long saveOpordHeader(Opordheader opordheader);
	public long updateOpordHeader(Opordheader opordheader);

	public long saveOpordDetail(Oporddetail oporddetail);
	public long updateOpordDetail(Oporddetail oporddetail);

	public Opordheader findOpordHeaderByXordernum(String xordernum);
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow);

	public List<Oporddetail> findOporddetailByXordernum(String xordernum);
	public List<Opordheader> getAllOpordheader();

	public Opordheader findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(String xtypetrn, String xpornum, String xcus, Date xdate);
	public Opordheader findOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate);
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate);
	public Oporddetail findOporddetailByXordernumAndXitem(String xordernum, String xitem);

	public List<BranchesRequisitions> getSalesOrderMatrxi(Date xdate);
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);

	public List<Opordheader> findAllSalesOrder(String xtypetrn, String xtrn, String xstatus, Date xdate);
}
