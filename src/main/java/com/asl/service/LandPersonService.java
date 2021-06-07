package com.asl.service;

import java.util.List;

import com.asl.entity.LandPerson;

public interface LandPersonService {
	
	public long save(LandPerson landperson);
	
	public long update(LandPerson landperson);

	public List<LandPerson> getAllLandPerson();

	public LandPerson findByLandPerson(String xperson);

}
