package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Vatait;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.OpordService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.util.CKTime;

import lombok.Data;

@Controller
@RequestMapping("/conventionmanagement/hallbooking")
public class ConventionHallBookingController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private XcodesService xcodeService;
	@Autowired private VataitService vataitService;
	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadBookingPage(Model model) {
		model.addAttribute("hallbookingpreffix", xtrnService.findByXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("vataitList", vataitService.getAllVatait());

		

		model.addAttribute("oporddetail", getDefaultOpordDetail());

		model.addAttribute("availableHalls", opordService.findAvailableHallsByDate(new Date()));
		model.addAttribute("soprefix",
				xtrnService.findByXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode()));
		model.addAttribute("bookingOrderList",
				opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(),
						TransactionCodeType.HALL_BOOKING_SALES_ORDER.getdefaultCode()));

		return "pages/conventionmanagement/hallbooking/opord";
	}

	@GetMapping("/{xordernum}")
	public String loadBookingPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if (oh == null) return "redirect:/conventionmanagement/hallbooking";
		model.addAttribute("opordheader", oh);
		model.addAttribute("vataitList", vataitService.getAllVatait());
		
		
		
		
		model.addAttribute("oporddetail", getDefaultOpordDetail());

		// model.addAttribute("soprefix",
		// xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(),
		// TransactionCodeType.SALES_ORDER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("availableHalls", opordService.findAvailableHallsByDate(new Date()));
		model.addAttribute("bookedHalls", opordService.findBookedHallsByXordernum(xordernum));
		model.addAttribute("soprefix", xtrnService.findByXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode()));
		model.addAttribute("bookingOrderList",
				opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(),
						TransactionCodeType.HALL_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));

		return "pages/conventionmanagement/hallbooking/opord";
	}

	public Opordheader getDefaultOpordHeader() {
		Opordheader oh = new Opordheader();

		oh.setXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode());

		oh.setXtotguest(0);
		oh.setXstarttime("00:00");
		oh.setXendtime("23:59");

		oh.setXhallamt(BigDecimal.ZERO);
		oh.setXfunctionamt(BigDecimal.ZERO);
		oh.setXfoodamt(BigDecimal.ZERO);
		oh.setXfacamt(BigDecimal.ZERO);
		oh.setXtotamt(BigDecimal.ZERO);

		oh.setXvatait("No Vat");

		oh.setXvatamt(BigDecimal.ZERO);
		oh.setXaitamt(BigDecimal.ZERO);
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXgrandtot(BigDecimal.ZERO);

		oh.setXstatus("Open");

		return oh;
	}

	public Oporddetail getDefaultOpordDetail() {
		Oporddetail od = new Oporddetail();

		od.setXrow(0);
		od.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		od.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		od.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));

		return od;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opordheader opordheader, BindingResult bindingResult, Model model) {
		if (opordheader == null || StringUtils.isBlank(opordheader.getXtypetrn()) || StringUtils.isBlank(opordheader.getXtrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// set default
		if(StringUtils.isBlank(opordheader.getXordernum())) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			opordheader.setXdate(cal.getTime());
		}

		// Validation
		if(StringUtils.isBlank(opordheader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer name required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXtotguest() <= 0) {
			responseHelper.setErrorStatusAndMessage("Guest Quantity invalid");
			return responseHelper.getResponse();
		}
		if(opordheader.getXstartdate() == null) {
			responseHelper.setErrorStatusAndMessage("Start date required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXstarttime() == null) {
			responseHelper.setErrorStatusAndMessage("Start time required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXenddate() == null) {
			responseHelper.setErrorStatusAndMessage("End date required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXendtime()== null) {
			responseHelper.setErrorStatusAndMessage("End time required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXdate().after(opordheader.getXstartdate())) {
			responseHelper.setErrorStatusAndMessage("Booking date can't be after Start date");
			return responseHelper.getResponse();
		}

		Calendar stdt  = Calendar.getInstance();
		stdt.setTime(opordheader.getXstartdate());
		stdt.set(Calendar.HOUR_OF_DAY, new CKTime(opordheader.getXstarttime()).getHour());
		stdt.set(Calendar.MINUTE, new CKTime(opordheader.getXstarttime()).getMinute());
		stdt.set(Calendar.SECOND, 0);

		Calendar endt  = Calendar.getInstance();
		endt.setTime(opordheader.getXenddate());
		endt.set(Calendar.HOUR_OF_DAY, new CKTime(opordheader.getXendtime()).getHour());
		endt.set(Calendar.MINUTE, new CKTime(opordheader.getXendtime()).getMinute());
		endt.set(Calendar.SECOND, 0);

		if(stdt.getTime().after(endt.getTime())) {
			responseHelper.setErrorStatusAndMessage("Start date can't be after End date");
			return responseHelper.getResponse();
		}


		if(opordheader.getXhallamt() == null) opordheader.setXhallamt(BigDecimal.ZERO);
		if(opordheader.getXfunctionamt() == null) opordheader.setXfunctionamt(BigDecimal.ZERO);
		if(opordheader.getXfoodamt() == null) opordheader.setXfoodamt(BigDecimal.ZERO);
		if(opordheader.getXfacamt() == null) opordheader.setXfacamt(BigDecimal.ZERO);
		if(opordheader.getXtotamt() == null) {
			BigDecimal tot = opordheader.getXhallamt().add(opordheader.getXfunctionamt()).add(opordheader.getXfoodamt()).add(opordheader.getXfacamt());
			opordheader.setXtotamt(tot);
		}
		if(opordheader.getXdiscamt() == null) opordheader.setXdiscamt(BigDecimal.ZERO);
		if(StringUtils.isBlank(opordheader.getXvatait())) opordheader.setXvatait("No Vat");

		Vatait vatait = vataitService.findVataitByXvatait(opordheader.getXvatait());
		if(vatait != null) {
			if(opordheader.getXvatamt() == null) opordheader.setXvatamt((opordheader.getXtotamt().multiply(vatait.getXvat())).divide(BigDecimal.valueOf(100)));
			if(opordheader.getXaitamt() == null) opordheader.setXaitamt((opordheader.getXtotamt().multiply(vatait.getXait())).divide(BigDecimal.valueOf(100)));
		} else {
			if(opordheader.getXvatamt() == null) opordheader.setXvatamt((opordheader.getXtotamt().multiply(BigDecimal.ZERO)).divide(BigDecimal.valueOf(100)));
			if(opordheader.getXaitamt() == null) opordheader.setXaitamt((opordheader.getXtotamt().multiply(BigDecimal.ZERO)).divide(BigDecimal.valueOf(100)));
		}

		BigDecimal grandTotal = (opordheader.getXtotamt().add(opordheader.getXvatamt()).add(opordheader.getXaitamt())).subtract(opordheader.getXdiscamt());
		if(opordheader.getXgrandtot() == null) opordheader.setXgrandtot(grandTotal);


		// if existing then update
		Opordheader existOh = opordService.findOpordHeaderByXordernum(opordheader.getXordernum());
		if (existOh != null) {
			BeanUtils.copyProperties(opordheader, existOh, "xtypetrn", "xtrn", "xordernum");

			long count = opordService.updateOpordHeader(existOh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Booking");
				return responseHelper.getResponse();
			}

			responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + existOh.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking order updated successfully");
			return responseHelper.getResponse();
		}

		// if new record
		long count = opordService.saveOpordHeader(opordheader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Booking order not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking");
		responseHelper.setSuccessStatusAndMessage("Booking Order created successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xordernum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xordernum) {
		return doArchiveOrRestore(xordernum, true);
	}

	@PostMapping("/restore/{xordernum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xordernum) {
		return doArchiveOrRestore(xordernum, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xordernum, boolean archive) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if (opordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		opordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = opordService.updateOpordHeader(opordHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Booking updated successfully");
		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + opordHeader.getXordernum());
		return responseHelper.getResponse();
	}

	@GetMapping("{xordernum}/oporddetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		model.addAttribute("functions", caitemService.findByXcatitem("Function"));
		model.addAttribute("halls", caitemService.findByXcatitem("Convention Hall"));
		model.addAttribute("facilities", caitemService.findByXcatitem("Hall Facility"));
		model.addAttribute("foods", caitemService.findByXcatitem("Convention Hall Food"));

		return "pages/conventionmanagement/hallbooking/oporddetailmodal::oporddetailmodal";
	}

	@PostMapping("/oporddetails/save")
	public @ResponseBody Map<String, Object> saveOporddetail(HallitemsWrapper hallitemsWrapper) {
		if(hallitemsWrapper == null) {
			responseHelper.setErrorStatusAndMessage("Items not found to add");
			return responseHelper.getResponse();
		}


		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}

	@PostMapping("/oporddetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Oporddetail opordDetail) {
		if (opordDetail == null || StringUtils.isBlank(opordDetail.getXordernum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if (opordDetail.getXrow() == 0 && opordService.findOporddetailByXordernumAndXitem(opordDetail.getXordernum(),
				opordDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage(
					"Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// modify line amount
		opordDetail
				.setXlineamt(opordDetail.getXqtyord().multiply(opordDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Oporddetail existDetail = opordService.findOporddetailByXordernumAndXrow(opordDetail.getXordernum(),
				opordDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opordDetail, existDetail, "xordernum", "xrow");
			long count = opordService.updateOpordDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			long uCount = updateAdditionalCharges(existDetail, opordDetail);
			if (uCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + opordDetail.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opordService.saveOpordDetail(opordDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		long uCount = updateAdditionalCharges(null, opordDetail);
		if (uCount == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + opordDetail.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	public long updateAdditionalCharges(Oporddetail prevDetail, Oporddetail opordDetail) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(opordDetail.getXordernum());
		BigDecimal itemCharge = BigDecimal.ZERO;
		BigDecimal totalAmount = opordHeader.getXtotamt() == null ? BigDecimal.ZERO : opordHeader.getXtotamt();
		BigDecimal facAmount = opordHeader.getXfacamt() == null ? BigDecimal.ZERO : opordHeader.getXfacamt();
		BigDecimal hallAmount = opordHeader.getXhallamt() == null ? BigDecimal.ZERO : opordHeader.getXhallamt();
		if (prevDetail != null) {
			totalAmount = totalAmount.subtract(prevDetail.getXlineamt());
			if ("Hall Facility".equalsIgnoreCase(prevDetail.getXcatitem())) {
				facAmount = facAmount.subtract(prevDetail.getXlineamt());
			} else if ("Convention Hall".equalsIgnoreCase(prevDetail.getXcatitem())) {
				hallAmount = hallAmount.subtract(prevDetail.getXlineamt());
			}

		}
		if ("Hall Facility".equalsIgnoreCase(opordDetail.getXcatitem())) {
			itemCharge = facAmount.add(opordDetail.getXlineamt());
			opordHeader.setXfacamt(itemCharge);
		} else if ("Convention Hall".equalsIgnoreCase(opordDetail.getXcatitem())) {
			itemCharge = hallAmount.add(opordDetail.getXlineamt());
			opordHeader.setXhallamt(itemCharge);
		}
		totalAmount = totalAmount.add(itemCharge);
		opordHeader.setXtotamt(totalAmount);

		long count = opordService.updateOpordHeader(opordHeader);
		if (count == 0)
			return count;

		/*
		 * count = opordService.updateOpordDetail(opordDetail); if(count==0) return
		 * count;
		 */
		return count;
	}

	@GetMapping("/oporddetail/{xordernum}")
	public String reloadOpordDetailTabble(@PathVariable String xordernum, Model model) {
		List<Oporddetail> detailList = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("oporddetailsList", detailList);
		Opordheader header = new Opordheader();
		header.setXordernum(xordernum);
		model.addAttribute("opordheader", header);
		return "pages/conventionmanagement/hallbooking/opord::oporddetailtable";
	}

	@PostMapping("{xordernum}/oporddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpordDetail(@PathVariable String xordernum,
			@PathVariable String xrow, Model model) {
		Oporddetail od = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
		if (od == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = opordService.deleteOpordDetail(od);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + xordernum);
		return responseHelper.getResponse();
	}

	@GetMapping("/availablehallbydate/{searchDate}")
	public String findAvailableHallsByDate(@PathVariable Date searchDate, Model model) {

		model.addAttribute("availableHalls", opordService.findAvailableHallsByDate(searchDate));
		// model.addAttribute("bookedHalls",
		// opordService.findBookedHallsByXfuncdate(searchDate));

		return "pages/conventionmanagement/hallbooking/opord::availableHalls";
	}

	
	
}

@Data
class HallitemsWrapper {
	private String[] items;
}
