package com.asl.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.asl.entity.ConventionBookedDetails;
import com.asl.service.RoomBookingService;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2021
 */
@Service
public class RoomBookingServiceImpl extends AbstractGenericService implements RoomBookingService {

	@Override
	public List<String> allBookedRoomsInDateRange(String xcatitem, String xstartdate, String xenddate,
			String xordernum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConventionBookedDetails> allBookedRoomsInDateRange2(String xcatitem, String xstartdate,
			String xenddate) {
		// TODO Auto-generated method stub
		return null;
	}

}
