package com.asl.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.asl.model.BranchesRequisitions;
import com.asl.service.CacusService;
import com.asl.service.OpdoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpdoServiceImpl extends AbstractGenericService implements OpdoService {

	@Autowired private OpdoMapper opdoMapper;
	@Autowired private OpordMapper opordMapper;
	@Autowired private CaitemMapper caitemMapper;
	@Autowired private CacusService cacusService;

	@Transactional
	@Override
	public long save(Opdoheader opdoHeader) {
		if (opdoHeader == null)
			return 0;
		opdoHeader.setZid(sessionManager.getBusinessId());
		opdoHeader.setZauserid(getAuditUser());
		opdoHeader.setCentralzid(getBusinessId());
		return opdoMapper.saveOpdoHeader(opdoHeader);
	}

	@Override
	@Transactional
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
		if(StringUtils.isBlank(xdornum)) return Collections.emptyList();
		return opdoMapper.findOpdoDetailByXdornum(xdornum, sessionManager.getBusinessId(), getBusinessId());
	}

	@Override
	public long updateOpdoHeaderTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmt(xdornum, sessionManager.getBusinessId());
	}
	

	@Transactional
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

		Opordheader productionChalan = opordMapper.findOpordHeaderByXordernum(xordernum, sessionManager.getBusinessId());
		if(productionChalan == null) return 0;

		List<Opordheader> salesOrdersOfProductionChalan = opordMapper.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum, sessionManager.getBusinessId());
		if(salesOrdersOfProductionChalan == null || salesOrdersOfProductionChalan.isEmpty()) return 0;

		// check delivery chalan exist (it referes that already delivery chalan created)
		Opdoheader existDeliveryChalan = opdoMapper.findPoordHeaderByXordernum(productionChalan.getXordernum(), sessionManager.getBusinessId());
		if(existDeliveryChalan != null) return 0;

		// create delivery chalan first
		Opdoheader deliveryChalan = new Opdoheader();
		deliveryChalan.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		deliveryChalan.setXtrn(TransactionCodeType.CHALAN_NUMBER.getdefaultCode());
		deliveryChalan.setXdate(new Date());
		deliveryChalan.setXstatusord("Open");
		deliveryChalan.setXstatusar("Open");
		deliveryChalan.setXstatusjv("Open");
		deliveryChalan.setZid(sessionManager.getBusinessId());
		deliveryChalan.setXordernum(productionChalan.getXordernum());  // production chalan reference 
		long deliveryChalanCount = opdoMapper.saveOpdoHeader(deliveryChalan);
		if(deliveryChalanCount == 0) return 0;

		// Now fetch deliveryChalan again which is saved now by usning production chalan reference
//		Opdoheader savedDeliveryChalan = opdoMapper.findPoordHeaderByXordernum(productionChalan.getXordernum(), sessionManager.getBusinessId());
//		if(savedDeliveryChalan == null) return 0;

		// Create sales from sales order of production chalan first
		int salesSavedCount = 0;
		for(Opordheader salesOrder : salesOrdersOfProductionChalan) {
			Opdoheader sales = new Opdoheader();
			sales.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
			sales.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
			sales.setXdate(new Date());
			sales.setXstatusord("Open");
			sales.setXstatusjv("Open");
			sales.setXstatusar("Open");
			sales.setXdocnum(deliveryChalan.getXdornum());   // assign this sales to delivery chalan
			sales.setXchalancreated(true);
			sales.setXvatait("No Vat");
			sales.setXtotamt(BigDecimal.ZERO);
			sales.setXait(BigDecimal.ZERO);
			sales.setXvatamt(BigDecimal.ZERO);
			sales.setXdiscamt(BigDecimal.ZERO);
			sales.setXgrandtot(BigDecimal.ZERO);
			sales.setXwh("01");  // set for central store invoice
			sales.setXcus(salesOrder.getXcus());  // set branch reference for this sales, customer
			sales.setXordernum(salesOrder.getXordernum());  // set sales order reference
			sales.setRequisitionnumber(salesOrder.getXpornum());  // set branch requisition number reference
			sales.setZid(sessionManager.getBusinessId());

			long count = opdoMapper.saveOpdoHeader(sales);
			if(count == 0) continue;
			salesSavedCount++;

			// find recently saved sales header
			Opdoheader savedSales = opdoMapper.findPoordHeaderByXordernumAndRequisitionnumber(salesOrder.getXordernum(), salesOrder.getXpornum(), sessionManager.getBusinessId());
			if(savedSales == null) continue;

			salesOrder.setXdornum(savedSales.getXdornum()); // now set sales reference back to sales order
			opordMapper.updateOpordHeader(salesOrder); // updated sales order with sales reference

			// now prepare item details from sales order to sales
			List<Oporddetail> salesOrdeItems = opordMapper.findOporddetailByXordernum(salesOrder.getXordernum(), sessionManager.getBusinessId(), getBusinessId());
			if(salesOrdeItems == null || salesOrdeItems.isEmpty()) continue;

			for(Oporddetail salesOrderItem : salesOrdeItems) {
				Caitem caitem = caitemMapper.findByXitem(salesOrderItem.getXitem(), sessionManager.getBusinessId());
				if(caitem == null) continue;

				Opdodetail salesItem = new Opdodetail();
				salesItem.setXdornum(savedSales.getXdornum());  // set sales reference for its items
				salesItem.setXitem(salesOrderItem.getXitem());
				salesItem.setXqtyord(salesOrderItem.getXqtyord() != null ? salesOrderItem.getXqtyord() : BigDecimal.ZERO);
				salesItem.setXunitsel(salesOrderItem.getXunit());
				salesItem.setXrate(caitem.getXrate() != null ? caitem.getXrate() : BigDecimal.ZERO);
				salesItem.setXlineamt(salesItem.getXqtyord().multiply(salesItem.getXrate()));
				salesItem.setXlineamt(salesItem.getXlineamt().add(salesItem.getXlineamt().multiply(caitem.getXvatrate() == null ? BigDecimal.ZERO : caitem.getXvatrate()).divide(BigDecimal.valueOf(100))));
				salesItem.setXcatitem(salesOrderItem.getXcatitem());
				salesItem.setXgitem(salesOrderItem.getXgitem());
				salesItem.setXdorrow(salesOrderItem.getXrow());
				salesItem.setZid(sessionManager.getBusinessId());

				long salesItemCount = saveDetail(salesItem);
				if(salesItemCount == 0) continue;

				// Add item for delivery chalan also
				Opdodetail chItem = opdoMapper.findOpdoDetailByXdornumAndXitem(deliveryChalan.getXdornum(), salesOrderItem.getXitem(), sessionManager.getBusinessId());
				if(chItem != null) {
					chItem.setXqtyord(chItem.getXqtyord().add(salesItem.getXqtyord()));
					opdoMapper.updateOpdoDetail(chItem);
				} else {
					salesItem.setXdornum(deliveryChalan.getXdornum());
					opdoMapper.saveOpdoDetail(salesItem);
				}

			}

		}

		if(salesSavedCount == salesOrdersOfProductionChalan.size()) {
			productionChalan.setInvoicecreated(true);
			productionChalan.setXdornum(deliveryChalan.getXdornum());
			opordMapper.updateOpordHeader(productionChalan);
		}

		return salesSavedCount == salesOrdersOfProductionChalan.size() ? 1 : 0;
	}

	@Override
	public List<BranchesRequisitions> getSalesInvoiceMatrxi(Date xdate) {
		if(xdate == null) return Collections.emptyList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return opdoMapper.getSalesInvoiceMatrxi(sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public Opdoheader findOpordheaderByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opdoMapper.findOpordheaderByXordernum(xordernum, sessionManager.getBusinessId());
	}

	

}
