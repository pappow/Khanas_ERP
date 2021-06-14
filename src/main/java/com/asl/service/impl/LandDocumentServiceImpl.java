package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;
import com.asl.mapper.LandDocumentMapper;
import com.asl.service.LandDocumentService;

@Service
public class LandDocumentServiceImpl extends AbstractGenericService implements LandDocumentService{
	
	@Autowired
	private LandDocumentMapper landDocumentMapper;
	
	@Transactional
	@Override
	public long save(LandDocument landDocument) {
		if (landDocument == null)
			return 0;
		landDocument.setZid(sessionManager.getBusinessId());
		landDocument.setZauserid(getAuditUser());
		return landDocumentMapper.saveLandDocument(landDocument);
	}
	
	@Transactional
	@Override
	public long update(LandDocument landDocument) {
		if (landDocument == null)
			return 0;
		landDocument.setZid(sessionManager.getBusinessId());
		landDocument.setZuuserid(getAuditUser());
		return landDocumentMapper.updateLandDocument(landDocument);
	}
	
	@Override
	public List<LandDocument> getAllLandDocument() {
		return landDocumentMapper.getAllLandDocument(sessionManager.getBusinessId());
	}
	
	@Override
	public LandDocument findByLandDocument(String xdoc) {
		if (StringUtils.isBlank(xdoc)) return null;
		return landDocumentMapper.findByLandDocument(xdoc, sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandDocument> findByLandPersonDocument(String xperson) {
		if (StringUtils.isBlank(xperson)) return null;
		return landDocumentMapper.findByLandPersonDocument(xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public long deleteDetail(LandDocument landDocument){
		if(landDocument==null) return 0;
		long count = landDocumentMapper.deleteDetail(landDocument);
		return count;
	}
	
	@Override
	public LandDocument findLandPersonDocumentByXpersonAndXrow(String xperson, int xrow) {
		if(StringUtils.isBlank(xperson) || xrow == 0) return null;
		return landDocumentMapper.findLandPersonDocumentByXpersonAndXrow(xperson,xrow,sessionManager.getBusinessId());
		
	}
	
	@Override
	public List<LandSurveyor> searchServeyorId(String xsurveyor){
		if(StringUtils.isBlank(xsurveyor)) return Collections.emptyList();
		return landDocumentMapper.searchServeyorId(xsurveyor.toUpperCase(), sessionManager.getBusinessId());
	}
	
}
