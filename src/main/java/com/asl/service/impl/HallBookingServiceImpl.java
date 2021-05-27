package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ConventionBookedDetails;
import com.asl.mapper.OpordMapper;
import com.asl.service.HallBookingService;

/**
 * @author Zubayer Ahamed
 * @since May 26, 2021
 */
@Service
public class HallBookingServiceImpl extends AbstractGenericService implements HallBookingService {

	@Autowired private OpordMapper opordMapper;

	@Override
	public List<String> allBookedHallsInDateRange(String xcatitem, String xstartdate, String xenddate, String xordernum) {
		if(StringUtils.isBlank(xcatitem) || StringUtils.isBlank(xstartdate) || StringUtils.isBlank(xenddate)) return Collections.emptyList();
		return opordMapper.allBookedHallsInDateRange(xcatitem, xstartdate, xenddate, xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<ConventionBookedDetails> allBookedHallsInDateRange2(String xcatitem, String xstartdate, String xenddate) {
		if(StringUtils.isBlank(xcatitem) || StringUtils.isBlank(xstartdate) || StringUtils.isBlank(xenddate)) return Collections.emptyList();
		return opordMapper.allBookedHallsInDateRange2(xcatitem, xstartdate, xenddate, sessionManager.getBusinessId());
	}

	
}
