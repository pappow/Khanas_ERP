package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.mapper.OpordMapper;
import com.asl.model.BranchesRequisitions;
import com.asl.service.OpordService;


/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Service
public class OpordServiceImpl extends AbstractGenericService implements OpordService {

	@Autowired private OpordMapper opordMapper;

	@Override
	public long saveOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		opordheader.setZauserid(getAuditUser());
		return opordMapper.saveOpordHeader(opordheader);
	}

	@Override
	public long updateOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		opordheader.setZuuserid(getAuditUser());
		return opordMapper.updateOpordHeader(opordheader);
	}

	@Override
	public long saveOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		oporddetail.setZauserid(getAuditUser());
		return opordMapper.saveOpordDetail(oporddetail);
	}

	@Override
	public long updateOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		oporddetail.setZuuserid(getAuditUser());
		return opordMapper.updateOpordDetail(oporddetail);
	}

	@Override
	public long deleteOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		return opordMapper.deleteOpordDetail(oporddetail);
	}

	@Override
	public Opordheader findOpordHeaderByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opordMapper.findOpordHeaderByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow) {
		if(StringUtils.isBlank(xordernum) || xrow == 0) return null;
		return opordMapper.findOporddetailByXordernumAndXrow(xordernum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Oporddetail> findOporddetailByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opordMapper.findOporddetailByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> getAllOpordheader() {
		return opordMapper.getAllOpordheader(sessionManager.getBusinessId());
	}

	@Override
	public Opordheader findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(String xtypetrn, String xpornum, String xcus, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xpornum) || StringUtils.isBlank(xcus) || xdate == null) return null;
		return opordMapper.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(xtypetrn, xpornum, xcus, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public Opordheader findOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || xdate == null) return null;
		return opordMapper.findOpordHeaderByXtypetrnAndXtrnAndXdate(xtypetrn, xtrn, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(String xtypetrn, String xtrn, String xstatus) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatus)) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(xtypetrn, xtrn, xstatus, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || xdate == null) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(xtypetrn, xtrn, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public Oporddetail findOporddetailByXordernumAndXitem(String xordernum, String xitem) {
		if(StringUtils.isBlank(xordernum) || StringUtils.isBlank(xitem)) return null;
		return opordMapper.findOporddetailByXordernumAndXitem(xordernum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public List<BranchesRequisitions> getSalesOrderMatrxi(Date xdate) {
		return opordMapper.getSalesOrderMatrxi(sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllSalesOrder(String xtypetrn, String xtrn, String xstatus, Date date) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatus)) return Collections.emptyList();
		if(date == null) date = new Date();
		return opordMapper.findAllSalesOrder(xtypetrn, xtrn, xstatus, sdf.format(date), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllSalesOrderByChalan(String xtypetrn, String xtrn, String xchalanref) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xchalanref)) return Collections.emptyList();
		return opordMapper.findAllSalesOrderByChalan(xtypetrn, xtrn, xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> searchOpordheaderByXtypetrnAndXtrnAndXordernum(String xtypetrn, String xtrn, String xordernum, String xstatus) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xordernum)) return Collections.emptyList();
		return opordMapper.searchOpordheaderByXtypetrnAndXtrnAndXordernum(xtypetrn, xtrn, xordernum.toUpperCase(), xstatus, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opordheader> searchXpornum(String xpornum){
		return opordMapper.searchXpornum(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Caitem> findAvailableRoomsByDate(Date xcheckindate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findAvailableRoomsByDate(formater.format(xcheckindate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findBookedRoomsByDate(Date xcheckindate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findBookedRoomsByDate(formater.format(xcheckindate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Oporddetail> findBookedRoomsByXordernum(String xordernum) {
		return opordMapper.findBookedRoomsByXordernum(xordernum, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findAvailableHallsByDate(Date xfuncdate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findAvailableHallsByDate(formater.format(xfuncdate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findBookedHallsByXfuncdate(Date xfuncdate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findBookedHallsByXfuncdate(formater.format(xfuncdate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Oporddetail> findBookedHallsByXordernum(String xordernum) {
		return opordMapper.findBookedHallsByXordernum(xordernum, sessionManager.getBusinessId());
	}
	
}
