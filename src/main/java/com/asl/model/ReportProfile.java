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
	private static final String ERROR = "Error is : {}, {}";

	private ProfileLine SUPL;
	private ProfileLine CUSL;
	private ProfileLine STOCKL;
	private ProfileLine ITEML;
	private ProfileLine POL;
	private ProfileLine PDL;
	private ProfileLine GRNL;
	private ProfileLine SUGL;
	private ProfileLine SL;
	private ProfileLine CL;
	private ProfileLine BOML;
	private ProfileLine PL;
	private ProfileLine SOL;

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
			log.error(ERROR, e.getMessage(),e);
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
	public void setSUPL(ProfileLine profileLine) {
		this.SUPL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setCUSL(ProfileLine profileLine) {
		this.CUSL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSTOCKL(ProfileLine profileLine) {
		this.STOCKL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setITEML(ProfileLine profileLine) {
		this.ITEML = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setPOL(ProfileLine profileLine) {
		this.POL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setPDL(ProfileLine profileLine) {
		this.PDL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setGRNL(ProfileLine profileLine) {
		this.GRNL = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setSUGL(ProfileLine profileLine) {
		this.SUGL = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setSL(ProfileLine profileLine) {
		this.SL = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setCL(ProfileLine profileLine) {
		this.CL = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setBOML(ProfileLine profileLine) {
		this.BOML = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setPL(ProfileLine profileLine) {
		this.PL = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setSOL(ProfileLine profileLine) {
		this.SOL = profileLine;
		addToProfileLineMap(profileLine);
	}
}
