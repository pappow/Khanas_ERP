package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.mapper.ImtorMapper;
import com.asl.model.ServiceException;
import com.asl.service.ImtorService;

@Service
public class ImtorServiceImpl extends AbstractGenericService implements ImtorService {

	@Autowired
	private ImtorMapper imtorMapper;

	@Override
	public long save(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtrn()))
			return 0;
		imtorHeader.setZid(sessionManager.getBusinessId());
		imtorHeader.setZauserid(getAuditUser());
		if(StringUtils.isNotBlank(imtorHeader.getXtornum()) && StringUtils.isNotBlank(imtorHeader.getXchalanref()))
			return imtorMapper.saveImtorHeaderWithChalan(imtorHeader);
		
		return imtorMapper.saveImtorHeader(imtorHeader);
	}

	@Override
	public long update(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtornum())) return 0;
		if(StringUtils.isBlank(imtorHeader.getZid())) imtorHeader.setZid(sessionManager.getBusinessId());
		imtorHeader.setZuuserid(getAuditUser());
		return imtorMapper.updateImtorHeader(imtorHeader);
	}

	@Override
	public long delete(ImtorHeader imtorHeader) {
		if(imtorHeader == null) return 0;
		long count = imtorMapper.deleteImtorHeader(imtorHeader);
		return count;
	}
	
	@Override
	public long saveDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		imtorDetail.setZauserid(getAuditUser());
		long count = imtorMapper.saveImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	@Transactional
	public long saveDetail(List<ImtorDetail> imtorDetails) throws ServiceException {
		if(imtorDetails == null || imtorDetails.isEmpty()) return 0;
		long totalCount = 0;
		for(ImtorDetail id : imtorDetails) {
			id.setZid(sessionManager.getBusinessId());
			id.setZauserid(getAuditUser());
			long count = imtorMapper.saveImtorDetail(id);
			if(count == 0) {
				throw new ServiceException("All details not saved");
			}
			totalCount += count;
		}
		return totalCount;
	}

	@Override
	public long updateDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		imtorDetail.setZuuserid(getAuditUser());
		long count = imtorMapper.updateImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public long deleteDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		long count = imtorMapper.deleteImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public ImtorHeader findImtorHeaderByXtornum(String xtornum) {
		if (StringUtils.isBlank(xtornum)) return null;
		return imtorMapper.findImtorHeaderByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public ImtorHeader findImtorHeaderByXchalanref(String xchalanref) {
		if (StringUtils.isBlank(xchalanref)) return null;
		return imtorMapper.findImtorHeaderByXchalanref(xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> getAllImtorHeader() {
		return imtorMapper.getAllImtorHeader(sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum))
			return null;
		String centralZid = null;
		if(Boolean.TRUE.equals(sessionManager.getZbusiness().getCentral())) {
			centralZid = sessionManager.getBusinessId();
		} else {
			centralZid = sessionManager.getZbusiness().getCentralzid();
		}
		return imtorMapper.findImtorDetailByXtornum(xtornum, sessionManager.getBusinessId(), centralZid);
	}
	
	@Override
	public List<ImtorDetail> findImtorDetailByXtornumAndXchalanref(String xtornum, String xchalanref) {
		if(StringUtils.isBlank(xchalanref))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXchalanref(xtornum, xchalanref, sessionManager.getBusinessId());
	}
	
	

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow) {
		if (StringUtils.isBlank(xtornum) || xrow == 0)
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXrow(xtornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem) {
		if (StringUtils.isBlank(xtornum) || StringUtils.isBlank(xitem))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXitem(xtornum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public long updateImtorHeaderTotalAmt(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		return imtorMapper.updateImtorHeaderTotalAmt(imtorDetail);
	}

	@Override
	public void procConfirmTO(String xtornum, String p_action, String p_seq) {
		imtorMapper.procConfirmTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtornum, p_action, p_seq);
	}

	@Override
	public long deleteImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum)) return 0;
		return imtorMapper.deleteImtorDetailByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> getAllImtorHeaderbyPrefix(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return imtorMapper.getAllImtorHeaderbyPrefix(xtypetrn, sessionManager.getBusinessId());
	}



	
}
