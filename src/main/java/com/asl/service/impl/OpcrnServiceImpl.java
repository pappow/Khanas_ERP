package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.mapper.OpcrnMapper;
import com.asl.service.OpcrnService;

@Service
public class OpcrnServiceImpl extends AbstractGenericService implements OpcrnService {

	@Autowired
	private OpcrnMapper opcrnMapper;
	
	@Override
	public long save(Opcrnheader opcrnheader) {
		if(opcrnheader == null)
			return 0;
		opcrnheader.setZid(sessionManager.getBusinessId());
		return opcrnMapper.saveOpcrnHeader(opcrnheader);
	}

	@Override
	public long update(Opcrnheader opcrnheader) {
		if (opcrnheader == null || StringUtils.isBlank(opcrnheader.getXcrnnum())) return 0;
		if(StringUtils.isBlank(opcrnheader.getZid()))
			opcrnheader.setZid(sessionManager.getBusinessId());
		return opcrnMapper.updateOpcrnHeader(opcrnheader);
	}

	@Override
	public long saveDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum()))
			return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		long count = opcrnMapper.saveOpcrnDetail(opcrndetail);

		return count;
	}

	@Override
	public long updateDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum())) return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		long count = opcrnMapper.updateOpcrnDetail(opcrndetail);
		return count;
	}

	@Override
	public long deleteDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null) return 0;
		long count = opcrnMapper.deleteOpcrnDetail(opcrndetail);		
		return count;
	}

	@Override
	public List<Opcrnheader> getAllOpcrnheader() {
		// TODO Auto-generated method stub
		return opcrnMapper.getAllOpcrnheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) 
			return Collections.emptyList();		
		return opcrnMapper.findOpcrnDetailByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) 
			return null;		
		return opcrnMapper.findOpcrnHeaderByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}
	
	@Override
	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum)) 
			return null;		
		return opcrnMapper.findOpcrnHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}


	@Override
	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow) {
		if(StringUtils.isBlank(xcrnnum) || xrow == 0) return null;
		return opcrnMapper.findOpcrnDetailByXcrnnumAndXrow(xcrnnum, xrow, sessionManager.getBusinessId());
	}

}
