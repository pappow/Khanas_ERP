package com.asl.service.impl;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.mapper.OpreqMapper;
import com.asl.service.OpreqService;


/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */

@Service
public class OpreqServiceImpl extends AbstractGenericService implements OpreqService {

	@Autowired private OpreqMapper opreqMapper;

	@Override
	public long saveOpreqheader(Opreqheader opreqheader) {
		if(opreqheader == null) return 0;
		opreqheader.setZid(sessionManager.getBusinessId());
		opreqheader.setZauserid(getAuditUser());
		opreqheader.setCentralzid(getBusinessId());
		return opreqMapper.saveOpreqheader(opreqheader);
	}

	@Override
	public long updateOpreqheader(Opreqheader opreqheader) {
		if(opreqheader == null) return 0;
		opreqheader.setZid(sessionManager.getBusinessId());
		opreqheader.setZuuserid(getAuditUser());
		long count = opreqMapper.updateOpreqheader(opreqheader);
		return count;
	}

	@Override
	public long deleteOpreqheader(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return 0;
		return opreqMapper.deleteOpreqheader(xdoreqnum, sessionManager.getBusinessId());
	}
	
	@Override
	public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return null;
		return opreqMapper.findOpreqHeaderByXdoreqnum(xdoreqnum, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opreqheader> getAllOpreqheader() {
		return opreqMapper.getAllOpreqheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opreqheader> getAllStatusOpenOpreqheader(){
		return opreqMapper.getAllStatusOpenOpreqheader(sessionManager.getBusinessId());
	}
	
	@Override
	public long saveOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		opreqdetail.setZid(sessionManager.getBusinessId());
		opreqdetail.setZauserid(getAuditUser());
		return opreqMapper.saveOpreqdetail(opreqdetail);
		
	}

	@Override
	public long updateOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		opreqdetail.setZid(sessionManager.getBusinessId());
		opreqdetail.setZuuserid(getAuditUser());
		return opreqMapper.updateOpreqdetail(opreqdetail);
	
	}

	@Override
	public long deleteOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		opreqdetail.setZid(sessionManager.getBusinessId());
		return opreqMapper.deleteOpreqdetail(opreqdetail);
	
	}

	@Override
	public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum){
		if(StringUtils.isBlank(xdoreqnum)) return null;
		return opreqMapper.findOpreqDetailByXdoreqnum(xdoreqnum, sessionManager.getBusinessId());
	}

	@Override
	public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow) {
		if(StringUtils.isBlank(xdoreqnum) || xrow == 0) return null;
		return opreqMapper.findOpreqdetailByXordernumAndXrow(xdoreqnum, xrow, sessionManager.getBusinessId());
	}
	
	@Override
	public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem) {
		if(StringUtils.isBlank(xdoreqnum) || StringUtils.isBlank(xitem)) return null;
		return opreqMapper.findOpreqdetailByXdoreqnumAndXitem(xdoreqnum, xitem, sessionManager.getBusinessId());
	}
	
	
}
