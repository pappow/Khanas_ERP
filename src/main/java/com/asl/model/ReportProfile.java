package com.asl.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.asl.entity.ProfileLine;
import com.asl.enums.ReportMenu;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Slf4j
@Data
public class ReportProfile implements Serializable {

	private static final long serialVersionUID = 5690006573031426825L;

	private ProfileLine salesReportSummary;
	private ProfileLine salesReportDetail;
	private ProfileLine cancelBillReport;
	private ProfileLine categorySalesReport;
	private ProfileLine itemWiseSalesSummary;
	private ProfileLine setMenuWiseSalesSummary;
	private ProfileLine itemWiseSalesSummaryTP;
	private ProfileLine itemList;
	private ProfileLine itemPriceHistory;
	private ProfileLine collectionReportSummary;
	private ProfileLine collectionReportDetail;
	private ProfileLine creditCardCollectionDetail;
	private ProfileLine salesEatInOrTakeAway;
	private ProfileLine vatReport;
	private ProfileLine syncStatus;
	private ProfileLine discountReport;
	private ProfileLine deliveryServiceReport;

	private ProfileLine salesReportSummaryV;
	private ProfileLine salesReportDetailV;
	private ProfileLine itemWiseSalesSummaryV;
	private ProfileLine itemPriceHistoryV;
	private ProfileLine collectionReportSummaryV;
	private ProfileLine collectionReportDetailV;
	private ProfileLine creditCardCollectionDetailV;
	private ProfileLine salesEatInOrTakeAwayV;

	// OUTLET SERVER REPORTS
	private ProfileLine outletSalesStatement;
	private ProfileLine hourlyCCAndSales;
	private ProfileLine itemSalesStatement;
	private ProfileLine outletSalesBreakdown;
	private ProfileLine billOrDayWiseAccountStatement;
	private ProfileLine dayWiseSalesSummary;
	private ProfileLine outletSyncStatus;

	private List<ProfileLine> profileLines = new ArrayList<>();
	private static Map<String, Method> profileSetterMethods = new HashMap<>();

	/**
	 * Add newly added menu option declared in PROFILE list
	 */
	static {
		setterCaller();
	}

	public static void setterCaller() {
		EnumSet.allOf(ReportMenu.class).forEach(rm -> addSetter(rm.getCode(), rm.getProfileField()));
	}

	public static void addSetter(String code, String fieldName) {
		try {
			String methodName = "set" + StringUtils.capitalize(fieldName);
			Method method = ReportProfile.class.getMethod(methodName, ProfileLine.class);
			profileSetterMethods.put(code.toUpperCase(), method);
		} catch (NoSuchMethodException | SecurityException  e) {
			log.error("Error is : {}, {}", e.getMessage(),e);
		}
	}

	public static ProfileLine invokeGetter(Object obj, String variableName){
		try {
			PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
			Method getter = pd.getReadMethod();
			return (ProfileLine) getter.invoke(obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addToProfileLineMap(ProfileLine profileLine) {
		profileLines.add(profileLine);
		profileLines.sort(Comparator.comparing(ProfileLine::getSeqn));
	}

	public void setProfileLine(ProfileLine profileLine) {
		if(profileLine == null || StringUtils.isEmpty(profileLine.getProfileCode())) {
			log.warn("No profile or profile code found....");
			return;
		}

		String code = profileLine.getProfileCode().toUpperCase();
		if(profileSetterMethods.containsKey(code)) {
			Method method = profileSetterMethods.get(code);
			try {
				method.invoke(this, profileLine);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.warn("Unbale to set profile for {}", code, e);
			}
		} else {
			addToProfileLineMap(profileLine);
		}
	}

	// Setter methods
	public void setSalesReportSummary(ProfileLine profileLine) {
		this.salesReportSummary = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSalesReportDetail(ProfileLine profileLine) {
		this.salesReportDetail = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCancelBillReport(ProfileLine profileLine) {
		this.cancelBillReport = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCategorySalesReport(ProfileLine profileLine) {
		this.categorySalesReport = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemWiseSalesSummary(ProfileLine profileLine) {
		this.itemWiseSalesSummary = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSetMenuWiseSalesSummary(ProfileLine profileLine) {
		this.setMenuWiseSalesSummary = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemWiseSalesSummaryTP(ProfileLine profileLine) {
		this.itemWiseSalesSummaryTP = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemList(ProfileLine profileLine) {
		this.itemList = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemPriceHistory(ProfileLine profileLine) {
		this.itemPriceHistory = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCollectionReportSummary(ProfileLine profileLine) {
		this.collectionReportSummary = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCollectionReportDetail(ProfileLine profileLine) {
		this.collectionReportDetail = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCreditCardCollectionDetail(ProfileLine profileLine) {
		this.creditCardCollectionDetail = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSalesEatInOrTakeAway(ProfileLine profileLine) {
		this.salesEatInOrTakeAway = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setVatReport(ProfileLine profileLine) {
		this.vatReport = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSyncStatus(ProfileLine profileLine) {
		this.syncStatus = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setDiscountReport(ProfileLine profileLine) {
		this.discountReport = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setDeliveryServiceReport(ProfileLine profileLine) {
		this.deliveryServiceReport = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSalesReportSummaryV(ProfileLine profileLine) {
		this.salesReportSummaryV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSalesReportDetailV(ProfileLine profileLine) {
		this.salesReportDetailV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemWiseSalesSummaryV(ProfileLine profileLine) {
		this.itemWiseSalesSummaryV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemPriceHistoryV(ProfileLine profileLine) {
		this.itemPriceHistoryV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCollectionReportSummaryV(ProfileLine profileLine) {
		this.collectionReportSummaryV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCollectionReportDetailV(ProfileLine profileLine) {
		this.collectionReportDetailV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCreditCardCollectionDetailV(ProfileLine profileLine) {
		this.creditCardCollectionDetailV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSalesEatInOrTakeAwayV(ProfileLine profileLine) {
		this.salesEatInOrTakeAwayV = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setOutletSalesStatement(ProfileLine profileLine) {
		this.outletSalesStatement = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setHourlyCCAndSales(ProfileLine profileLine) {
		this.hourlyCCAndSales = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setItemSalesStatement(ProfileLine profileLine) {
		this.itemSalesStatement = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setOutletSalesBreakdown(ProfileLine profileLine) {
		this.outletSalesBreakdown = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setBillOrDayWiseAccountStatement(ProfileLine profileLine) {
		this.billOrDayWiseAccountStatement = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setDayWiseSalesSummary(ProfileLine profileLine) {
		this.dayWiseSalesSummary = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setOutletSyncStatus(ProfileLine profileLine) {
		this.outletSyncStatus = profileLine;
		addToProfileLineMap(profileLine);
	}
}
