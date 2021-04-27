package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.CaitemMapper;
import com.asl.mapper.OpdoMapper;
import com.asl.mapper.OpordMapper;
import com.asl.mapper.PoordMapper;
import com.asl.service.OpdoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpdoServiceImpl extends AbstractGenericService implements OpdoService {

	@Autowired private OpdoMapper opdoMapper;
	@Autowired private OpordMapper opordMapper;
	@Autowired private PoordMapper poordMapper;
	@Autowired private CaitemMapper caitemMapper;

	@Override
	public long save(Opdoheader opdoHeader) {
		if (opdoHeader == null)
			return 0;
		opdoHeader.setZid(sessionManager.getBusinessId());
		opdoHeader.setZauserid(getAuditUser());
		return opdoMapper.saveOpdoHeader(opdoHeader);
	}

	@Override
	public long update(Opdoheader opdoHeader) {
		if (opdoHeader == null || StringUtils.isBlank(opdoHeader.getXdornum())) 
			return 0;
		if(StringUtils.isBlank(opdoHeader.getZid()))
			opdoHeader.setZid(sessionManager.getBusinessId());
		opdoHeader.setZuuserid(getAuditUser());
		return opdoMapper.updateOpdoHeader(opdoHeader);
	}

	@Override
	public long saveDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		opdoDetail.setZauserid(getAuditUser());
		long count = opdoMapper.saveOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		 
		return count;
	}

	@Override
	public long updateDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		opdoDetail.setZuuserid(getAuditUser());
		long count = opdoMapper.updateOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		return count;
	}

	@Override
	public long deleteDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null) return 0;
		long count = opdoMapper.deleteOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		return count;
	}

	@Override
	public Opdoheader findOpdoHeaderByXdornum(String xdornum) {
		if (StringUtils.isBlank(xdornum))
			return null;

		return opdoMapper.findOpdoHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> getAllOpdoHeader() {
		return opdoMapper.getAllOpdoHeader(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opdoheader> getAllRandomOpdoHeader() {
		return opdoMapper.getAllRandomOpdoHeader(sessionManager.getBusinessId());
	}

	@Override
	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow) {
		if(StringUtils.isBlank(xdornum) || xrow == 0) return null;
		return opdoMapper.findOpdoDetailByXdornumAndXrow(xdornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public Opdodetail findOpdoDetailByXdornumAndXitem(String xdornum, String xitem) {
		if(StringUtils.isBlank(xdornum) || StringUtils.isBlank(xitem)) return null;
		return opdoMapper.findOpdoDetailByXdornumAndXitem(xdornum, xitem, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum))
			return Collections.emptyList();
		return opdoMapper.findOpdoDetailByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public long updateOpdoHeaderTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmt(xdornum, sessionManager.getBusinessId());
	}
	

	@Override
	public long updateOpdoHeaderTotalAmtAndGrandTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmtAndGrandTotalAmt(xdornum, sessionManager.getBusinessId());
	}
	
	@Override
	public long updateOpdoHeaderGrandTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderGrandTotalAmt(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public void procConfirmDO(String xdornum, String p_seq) {
		opdoMapper.procConfirmDO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_seq);
	}

	@Override
	public void procIssuePricing(String xdocnum, String xwh, String p_seq) {
		opdoMapper.procIssuePricing(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdocnum, xwh, p_seq);
	}

	@Override
	public void procTransferOPtoAR(String xdornum, String p_screen, String p_seq) {
		opdoMapper.procTransferOPtoAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_screen, p_seq);
	}

	@Override
	public List<Opdoheader> findAllInvoiceOrder(String xtypetrn, String xtrn, String xstatus, Date date) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatus)) return Collections.emptyList();
		if(date == null) date = new Date();
		return opdoMapper.findAllInvoiceOrder(xtypetrn, xtrn, xstatus, sdf.format(date), sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findAllInvoiceOrderByChalan(String xtypetrn, String xtrn, String xchalanref) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xchalanref)) return Collections.emptyList();
		return opdoMapper.findAllInvoiceOrderByChalan(xtypetrn, xtrn, xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findAllOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opdoMapper.findAllOpdoHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xdornum) {
		return opdoMapper.searchOpdoHeader(xtypetrn, xdornum.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opdoheader> findOpdoXdornum(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return opdoMapper.findOpdoXdornum(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	
	
	@Override
	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xstatusord, String xdornum) {
		return opdoMapper.searchOpdoHeaderWithSatus(xtypetrn, xdornum.toUpperCase(), xstatusord, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long createSalesFromChalan(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return 0;

		Opordheader chalan = opordMapper.findOpordHeaderByXordernum(xordernum, sessionManager.getBusinessId());
		if(chalan == null) return 0;

		List<Opordheader> salesOrders = opordMapper.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum, sessionManager.getBusinessId());
		if(salesOrders == null || salesOrders.isEmpty()) return 0;

		// check delivery chalan exist
		Opdoheader existDeliveryChalan = opdoMapper.findPoordHeaderByXordernum(chalan.getXordernum(), sessionManager.getBusinessId());
		if(existDeliveryChalan != null) return 0;

		// create sales chalan first
		Opdoheader deliveryChalan = new Opdoheader();
		deliveryChalan.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		deliveryChalan.setXtrn(TransactionCodeType.CHALAN_NUMBER.getdefaultCode());
		deliveryChalan.setXdate(new Date());
		deliveryChalan.setXstatusord("Open");
		deliveryChalan.setZid(sessionManager.getBusinessId());
		deliveryChalan.setXordernum(chalan.getXordernum());
		long deliveryChalanCount = opdoMapper.saveOpdoHeader(deliveryChalan);
		if(deliveryChalanCount == 0) return 0;

		// Now fetch deliveryChalan again
		Opdoheader savedDeliveryChalan = opdoMapper.findPoordHeaderByXordernum(chalan.getXordernum(), sessionManager.getBusinessId());
		if(savedDeliveryChalan == null) return 0;

		// Create sales from sales order of chalan first
		int salesSavedCount = 0;
		for(Opordheader so : salesOrders) {
			Opdoheader sales = new Opdoheader();
			sales.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
			sales.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
			sales.setXdate(new Date());
			sales.setXstatusord("Open");
			sales.setXstatusjv("Open");
			sales.setXstatusar("Open");
			sales.setXdocnum(savedDeliveryChalan.getXdornum());
			sales.setXchalancreated(true);

			sales.setXordernum(so.getXordernum());
			sales.setRequisitionnumber(so.getXpornum());
			sales.setZid(sessionManager.getBusinessId());
			Cacus customer = poordMapper.findBranchCustomerByXcus(so.getXcus(), sessionManager.getBusinessId());
			if(customer != null) sales.setXcus(customer.getXcus());

			long count = opdoMapper.saveOpdoHeader(sales);
			if(count == 0) continue;
			salesSavedCount++;

			// find recently saved sales header
			Opdoheader savedSales = opdoMapper.findPoordHeaderByXordernumAndRequisitionnumber(so.getXordernum(), so.getXpornum(), sessionManager.getBusinessId());
			if(savedSales == null) continue;

			// total amount init for sales header
			BigDecimal totalAmt = BigDecimal.ZERO;

			// now prepare item details
			List<Oporddetail> salesOrdeItems = opordMapper.findOporddetailByXordernum(so.getXordernum(), sessionManager.getBusinessId());
			for(Oporddetail soItem : salesOrdeItems) {
				Caitem caitem = caitemMapper.findByXitem(soItem.getXitem(), sessionManager.getBusinessId());
				if(caitem == null) continue;

				Opdodetail item = new Opdodetail();
				item.setXdornum(savedSales.getXdornum());
				item.setXitem(soItem.getXitem());
				item.setXqtyord(soItem.getXqtyord() != null ? soItem.getXqtyord() : BigDecimal.ZERO);
				item.setXunitsel(soItem.getXunit());
				item.setZid(sessionManager.getBusinessId());
				item.setXcatitem(soItem.getXcatitem());
				item.setXgitem(soItem.getXgitem());
				item.setXrate(caitem.getXrate() != null ? caitem.getXrate() : BigDecimal.ZERO);
				long itemcount = opdoMapper.saveOpdoDetail(item);
				if(itemcount == 0) continue;

				// calculate totalAmt
				totalAmt = totalAmt.add(item.getXqtyord().multiply(item.getXrate()));

				// Add item for delivery chalan also
				Opdodetail chItem = opdoMapper.findOpdoDetailByXdornumAndXitem(savedDeliveryChalan.getXdornum(), soItem.getXitem(), sessionManager.getBusinessId());
				if(chItem != null) {
					chItem.setXqtyord(chItem.getXqtyord().add(item.getXqtyord()));
					opdoMapper.updateOpdoDetail(chItem);
				} else {
					item.setXdornum(deliveryChalan.getXdornum());
					opdoMapper.saveOpdoDetail(item);
				}

			}
			
			// now update sales header with total amount
			savedSales.setXtotamt(totalAmt);
			long count2 = opdoMapper.updateOpdoHeader(savedSales);
			if(count2 == 0) log.error("Can't update sales header with total amount");
		}

		if(salesSavedCount == salesOrders.size()) {
			chalan.setInvoicecreated(true);
			chalan.setXdornum(savedDeliveryChalan.getXdornum());
			opordMapper.updateOpordHeader(chalan);
		}

		return salesSavedCount == salesOrders.size() ? 1 : 0;
	}

}
