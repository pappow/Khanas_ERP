package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Component
public interface HallBookingService {

	public List<String> allBookedHallsInDateRange(String xcatitem, String xstartdate, String xenddate);
}
