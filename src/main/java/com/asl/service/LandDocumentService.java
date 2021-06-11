package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;

@Component
public interface LandDocumentService {

	public long save(LandDocument landDocument);
	
	public long update(LandDocument landDocument);

	public List<LandDocument> getAllLandDocument();

	public LandDocument findByLandDocument(String xdoc);
	
	public long deleteDetail(LandDocument LandDocument);
	
	//Search
	public List<LandSurveyor> searchServeyorId(String xsurveyor);
}
