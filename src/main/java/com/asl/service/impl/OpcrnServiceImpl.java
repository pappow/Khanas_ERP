package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.mapper.OpcrnMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpcrnServiceImpl extends AbstractGenericService implements OpcrnService {

	@Autowired private OpcrnMapper opcrnMapper;
	@Autowired private OpdoService opdoService;

	@Transactional
	@Override
	public long save(Opcrnheader opcrnheader) {
		if(opcrnheader == null) return 0;
		opcrnheader.setZid(sessionManager.getBusinessId());
		opcrnheader.setZauserid(getAuditUser());
		return opcrnMapper.saveOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long update(Opcrnheader opcrnheader) {
		if (opcrnheader == null || StringUtils.isBlank(opcrnheader.getXcrnnum())) return 0;
		opcrnheader.setZid(sessionManager.getBusinessId());
		opcrnheader.setZuuserid(getAuditUser());
		return opcrnMapper.updateOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long deleteOpcrnHeader(Opcrnheader opcrnheader) {
		if(opcrnheader == null) return 0;
		return opcrnMapper.deleteOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long saveDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum())) return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		opcrndetail.setZauserid(getAuditUser());
		return opcrnMapper.saveOpcrnDetail(opcrndetail);
	}

	@Transactional
	@Override
	public long saveDetail(List<Opcrndetail> opcrndetail) throws ServiceException {
		if(opcrndetail == null || opcrndetail.isEmpty()) return 0;
		long totalCount = 0;
		for(Opcrndetail opcrnDetail : opcrndetail) {
			opcrnDetail.setZid(sessionManager.getBusinessId());
			opcrnDetail.setZauserid(getAuditUser());
			long count = opcrnMapper.saveOpcrnDetail(opcrnDetail);
			if(count == 0) throw new ServiceException("Can't save return details");
			totalCount += count;
		}
		return totalCount;
	}

	@Transactional
	@Override
	public long updateDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum())) return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		opcrndetail.setZuuserid(getAuditUser());
		return opcrnMapper.updateOpcrnDetail(opcrndetail);
	}

	@Transactional
	@Override
	public long deleteDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null) return 0;
		return opcrnMapper.deleteOpcrnDetail(opcrndetail);
	}

	@Override
	public List<Opcrnheader> getAllOpcrnheader() {
		return opcrnMapper.getAllOpcrnheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return Collections.emptyList();
		return opcrnMapper.findOpcrnDetailByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return null;
		return opcrnMapper.findOpcrnHeaderByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return null;
		return opcrnMapper.findOpcrnHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow) {
		if(StringUtils.isBlank(xcrnnum) || xrow == 0) return null;
		return opcrnMapper.findOpcrndetailByXcrnnumAndXrow(xcrnnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public void procConfirmCRN(String xcrnnum, String p_seq) {
		opcrnMapper.procConfirmCRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, p_seq);
	}

	@Override
	public void procTransferOPtoAR(String xdocnum, String p_screen, String p_seq) {
		opcrnMapper.procTransferOPtoAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdocnum, p_screen, p_seq);
	}

	@Transactional
	@Override
	public Map<String, Object> save(ResponseHelper responseHelper, Opcrnheader opcrnHeader, Opdoheader odh) {
		// get available invoice details
		List<Opdodetail> availableInvoiceDetails = new ArrayList<>();
		List<Opdodetail> invoiceDetails = opdoService.findOpdoDetailByXdornum(odh.getXdornum());
		for(Opdodetail d : invoiceDetails) {
			if(!d.getXqtyord().equals(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn())) {
				availableInvoiceDetails.add(d);
			}
		}
		if(availableInvoiceDetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invoice has no available item to return");
			return responseHelper.getResponse();
		}

		long count = save(opcrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create sales return");
			return responseHelper.getResponse();
		}

		// create remaining details for crn
		List<Opcrndetail> details = new ArrayList<>();
		for(Opdodetail d : availableInvoiceDetails) {
			Opcrndetail p = new Opcrndetail();
			p.setXcrnnum(opcrnHeader.getXcrnnum());
			p.setXunit(d.getXunitsel());
			p.setXitem(d.getXitem());
			p.setXqtyord(d.getXqtyord().subtract(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn()));
			p.setXrate(d.getXrate());
			p.setXlineamt(p.getXqtyord().multiply(p.getXrate()));
			p.setXdocrow(d.getXrow());
			details.add(p);

			// update grn xqtycrn
			d.setXqtycrn(p.getXqtyord().add(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn()));
		}

		// now save pocrndetail first
		try {
			long countd = saveDetail(details);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't create sales return details");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// now update grn details
		for(Opdodetail d : availableInvoiceDetails) {
			long countd = opdoService.updateDetail(d);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update invoice details");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Sales return created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + opcrnHeader.getXcrnnum());
		return responseHelper.getResponse();
	}

	

}
