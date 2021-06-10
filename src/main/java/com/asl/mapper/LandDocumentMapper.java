package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;

@Mapper
public interface LandDocumentMapper {
	
	public long saveLandDocument(LandDocument LandDocument);
	
	public long updateLandDocument(LandDocument LandDocument);

	public List<LandDocument> getAllLandDocument(String zid);

	public LandDocument findByLandDocument(String xdoc, String zid);
	
	public long deleteDetail(LandDocument LandDocument);
	
	//Search
	public List<LandSurveyor> searchServeyorId(String xsurveyor, String zid);
	
}
