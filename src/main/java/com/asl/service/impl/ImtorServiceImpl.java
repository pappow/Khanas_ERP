package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ImtorDetail;
import com.asl.entity.Imtorheader;
import com.asl.entity.Imtrn;
import com.asl.mapper.ImtorMapper;
import com.asl.service.ImtorService;

@Service
public class ImtorServiceImpl extends AbstractGenericService implements ImtorService {
	
	@Autowired
	private ImtorMapper imtorMapper;

	@Override
	public long save(Imtorheader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtrntor()))
			return 0;
		imtorHeader.setZid(sessionManager.getBusinessId());
		return imtorMapper.saveImtorHeader(imtorHeader);
	}

	@Override
	public long update(Imtorheader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtornum())) return 0;
		if(StringUtils.isBlank(imtorHeader.getZid())) imtorHeader.setZid(sessionManager.getBusinessId());
		return imtorMapper.updateImtorHeader(imtorHeader);
	}

	@Override
	public long saveDetail(ImtorDetail imtorDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long updateDetail(ImtorDetail imtorDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteDetail(ImtorDetail imtorDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Imtorheader findImtorHeaderByXtornum(String xtornum) {
		if (StringUtils.isBlank(xtornum))
			return null;

		return imtorMapper.findImtorHeaderByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public List<Imtorheader> getAllImtorHeader() {
		return imtorMapper.getAllImtorHeader(sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum))
			return null;
		return imtorMapper.findImtorDetailByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem) {
		if (StringUtils.isBlank(xtornum) || StringUtils.isBlank(xitem))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXitem(xtornum, xitem, sessionManager.getBusinessId());
	}

}
