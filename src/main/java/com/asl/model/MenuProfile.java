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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Slf4j
@Data
public class MenuProfile implements Serializable {

	private static final long serialVersionUID = 5690006573031426825L;

	private ProfileLine dashboard;
	private ProfileLine profileMaintenance;
	private ProfileLine profileAllocation;
	private ProfileLine listMaintenance;
	private ProfileLine listImportExport;
	private ProfileLine report;
	private ProfileLine contact;
	private ProfileLine xuser;
	private ProfileLine shop;

	private List<ProfileLine> profileLines = new ArrayList<>();
	private static Map<String, Method> profileSetterMethods = new HashMap<>();

	/**
	 * Add newly added menu option declared in PROFILE list
	 */
	static {
		setterCaller();
	}

	public static void setterCaller() {
		EnumSet.allOf(com.asl.enums.MenuProfile.class).forEach(mp -> addSetter(mp.getCode(), mp.getProfileField()));
	}

	public static void addSetter(String code, String fieldName) {
		try {
			String methodName = "set" + StringUtils.capitalize(fieldName);
			Method method = MenuProfile.class.getMethod(methodName, ProfileLine.class);
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
	public void setDashboard(ProfileLine profileLine) {
		this.dashboard = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setProfileMaintenance(ProfileLine profileLine) {
		this.profileMaintenance = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setProfileAllocation(ProfileLine profileLine) {
		this.profileAllocation = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setListMaintenance(ProfileLine profileLine) {
		this.listMaintenance = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setListImportExport(ProfileLine profileLine) {
		this.listImportExport = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setReport(ProfileLine profileLine) {
		this.report = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setContact(ProfileLine profileLine) {
		this.contact = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setXuser(ProfileLine profileLine) {
		this.xuser = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setShop(ProfileLine profileLine) {
		this.shop = profileLine;
		addToProfileLineMap(profileLine);
	}
}
