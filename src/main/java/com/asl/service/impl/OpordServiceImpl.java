package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return opordMapper.saveOpordHeader(opordheader);
	}

	@Override
	public long updateOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		return opordMapper.updateOpordHeader(opordheader);
	}

	@Override
	public long saveOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		return opordMapper.saveOpordDetail(oporddetail);
	}

	@Override
	public long updateOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		return opordMapper.updateOpordDetail(oporddetail);
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
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || xdate == null) return null;
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

	
}
