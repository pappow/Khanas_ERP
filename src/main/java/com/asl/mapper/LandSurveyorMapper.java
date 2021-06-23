package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandSurvey;
import com.asl.entity.LandSurveyor;

@Mapper
public interface LandSurveyorMapper {
	
	public long saveLandSurveyor(LandSurveyor landsurveyor);
	
	public long updateLandSurveyor(LandSurveyor landsurveyor);
	
	public long deleteLandSurveyor(LandSurveyor landsurveyor);

	public List<LandSurveyor> getAllLandSurveyor(String zid);

	public LandSurveyor findByLandSurveyor(String xsurveyor, String zid);

}
