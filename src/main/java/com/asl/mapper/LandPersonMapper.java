package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandPerson;

@Mapper
public interface LandPersonMapper {
	
	public long saveLandPerson(LandPerson landperson);
	
	public long updateLandPerson(LandPerson landperson);

	public List<LandPerson> getAllLandPerson(String zid);

	public LandPerson findByLandPerson(String xperson, String zid);

}
