package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpordService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/roommanagement/roombooking")
public class RoomBookingController extends ASLAbstractController {
	@Autowired
	private OpordService opordService;
	@Autowired
	private XcodesService xcodeService;
	
	@GetMapping
	public String loadBookingPage(Model model) {
		Oporddetail oporddetail = new Oporddetail();
		oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		oporddetail.setXlineamt(oporddetail.getXqtyord().multiply(oporddetail.getXrate()));
		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("oporddetail", oporddetail);
		
		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("availableRooms", opordService.findAvailableRoomsByDate(new Date()));
		model.addAttribute("soprefix", xtrnService.findByXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode()));
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getdefaultCode()));
		//model.addAttribute("")
		
		return "pages/roommanagement/roombooking/opord";
	}

	@GetMapping("/{xordernum}")
	public String loadBookingPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null)
			oh = getDefaultOpordHeader();
		Oporddetail oporddetail = new Oporddetail();
		oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		oporddetail.setXlineamt(oporddetail.getXqtyord().multiply(oporddetail.getXrate()));

		model.addAttribute("opordheader", oh);
		model.addAttribute("oporddetail", oporddetail);
		
		SimpleDateFormat dFormat = new SimpleDateFormat("dd-MMM-yyyy");
		if(!oh.getXcheckoutdate().before(new Date()) || dFormat.format(oh.getXcheckoutdate()).equals(dFormat.format(new Date())))
			model.addAttribute("availableRooms", opordService.findAvailableRoomsByDate(new Date()));
		model.addAttribute("bookedRooms", opordService.findBookedRoomsByXordernum(xordernum));
		//model.addAttribute("soprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("soprefix", xtrnService.findByXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode()));
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));

		return "pages/roommanagement/roombooking/opord";
	}
	
	public Opordheader getDefaultOpordHeader() {
		Opordheader oh = new Opordheader();
		
		oh.setXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode());
		oh.setXtotamt(BigDecimal.ZERO);
		oh.setXgrandtot(BigDecimal.ZERO);
		oh.setXfacamt(BigDecimal.ZERO);
		oh.setXfoodamt(BigDecimal.ZERO);
		oh.setXroomamt(BigDecimal.ZERO);
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXtotguest(0);
		
		return oh;
	}
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opordheader opordheader, BindingResult bindingResult, Model model){
		
		if(opordheader == null || StringUtils.isBlank(opordheader.getXtrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		Opordheader existOh = opordService.findOpordHeaderByXordernum(opordheader.getXordernum());
		if(existOh != null) {
			BeanUtils.copyProperties(opordheader, existOh, "xordernum", "xdate", "xbookdate", "xstatus");
			long count = opordService.updateOpordHeader(existOh);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Booking not updated");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/roommanagement/roombooking/" + existOh.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking order updated successfully");
			return responseHelper.getResponse();
		}

		// if new
		opordheader.setXstatus("Open");
		opordheader.setXdate(new Date());
		opordheader.setXbookdate(new Date());
		opordheader.setXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode());
		long count = opordService.saveOpordHeader(opordheader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Booking order was not created");
			return responseHelper.getResponse();
		}
		
		//if()

		responseHelper.setRedirectUrl("/roommanagement/roombooking");
		responseHelper.setSuccessStatusAndMessage("Room Booked successfully");
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("/archive/{xordernum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xordernum){
		return doArchiveOrRestore(xordernum, true);
	}

	@PostMapping("/restore/{xordernum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xordernum){
		return doArchiveOrRestore(xordernum, false);
	}
	
	

	public Map<String, Object> doArchiveOrRestore(String xordernum, boolean archive){
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		opordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = opordService.updateOpordHeader(opordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Booking updated successfully");
		responseHelper.setRedirectUrl("/roommanagement/roombooking/" + opordHeader.getXordernum());
		return responseHelper.getResponse();
	}
	
	@GetMapping("{xordernum}/oporddetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		model.addAttribute("purchaseUnit", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			Oporddetail oporddetail = new Oporddetail();
			oporddetail.setXordernum(xordernum);
			oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			oporddetail.setXlineamt(oporddetail.getXqtyord().multiply(oporddetail.getXrate()));
			model.addAttribute("oporddetail", oporddetail);
		} else {
			Oporddetail oporddetail = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
			if(oporddetail == null) {
				oporddetail = new Oporddetail();
				oporddetail.setXordernum(xordernum);
				oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				oporddetail.setXlineamt(oporddetail.getXqtyord().multiply(oporddetail.getXrate()));
			}
			model.addAttribute("oporddetail", oporddetail);
		}

		return "pages/roommanagement/roombooking/oporddetailmodal::oporddetailmodal";
	}

	@PostMapping("/oporddetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Oporddetail opordDetail){
		if(opordDetail == null || StringUtils.isBlank(opordDetail.getXordernum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(opordDetail.getXrow() == 0 && opordService.findOporddetailByXordernumAndXitem(opordDetail.getXordernum(), opordDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}
		
		// modify line amount
		opordDetail.setXlineamt(opordDetail.getXqtyord().multiply(opordDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Oporddetail existDetail = opordService.findOporddetailByXordernumAndXrow(opordDetail.getXordernum(), opordDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(opordDetail, existDetail, "xordernum", "xrow");
			long count = opordService.updateOpordDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			//updateAdditionalCharges(opordDetail);
			responseHelper.setRedirectUrl("/roommanagement/roombooking/" +  opordDetail.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opordService.saveOpordDetail(opordDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		//updateAdditionalCharges(opordDetail);
		responseHelper.setRedirectUrl("/roommanagement/roombooking/" +  opordDetail.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}
	
	public long updateAdditionalCharges(Oporddetail opordDetail) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(opordDetail.getXordernum());
		
		BigDecimal itemCharge = BigDecimal.ZERO;
		BigDecimal totalAmount = opordHeader.getXtotamt();
		
		if("Hall Facility".equalsIgnoreCase(opordDetail.getXcatitem())) {
			itemCharge = opordHeader.getXfacamt().add(opordDetail.getXlineamt());
			opordHeader.setXfacamt(itemCharge);
			
		}
		else if("Convention Hall".equalsIgnoreCase(opordDetail.getXcatitem())) {
			itemCharge = opordHeader.getXhallamt().add(opordDetail.getXlineamt());
			opordHeader.setXhallamt(itemCharge);
		}
		totalAmount = opordHeader.getXtotamt().add(itemCharge);
		opordHeader.setXtotamt(totalAmount);
		
		return opordService.updateOpordDetail(opordDetail);
	}

	@GetMapping("/oporddetail/{xordernum}")
	public String reloadOpordDetailTabble(@PathVariable String xordernum, Model model) {
		List<Oporddetail> detailList = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("oporddetailsList", detailList);
		Opordheader header = new Opordheader();
		header.setXordernum(xordernum);
		model.addAttribute("opordheader", header);
		return "pages/roommanagement/roombooking/opord::oporddetailtable";
	}

	@PostMapping("{xordernum}/oporddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpordDetail(@PathVariable String xordernum, @PathVariable String xrow, Model model) {
		Oporddetail od = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
		if(od == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = opordService.deleteOpordDetail(od);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/roommanagement/roombooking/" +  xordernum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/availableroombydate/{searchDate}")
	public String findAvailableRoomsByDate(@PathVariable Date searchDate, Model model) {
		
		model.addAttribute("availableRooms", opordService.findAvailableRoomsByDate(searchDate));
		
		return "pages/roommanagement/roombooking/opord::allRooms";
	}

}
