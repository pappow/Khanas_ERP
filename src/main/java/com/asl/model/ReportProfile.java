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


	private ProfileLine RM0301;
	private ProfileLine RM0302;
	private ProfileLine RM0303;
	private ProfileLine RM0304;
	private ProfileLine RM0305;
	private ProfileLine RM0306;
	private ProfileLine RM0307;
	private ProfileLine RM0308;
	private ProfileLine RM0309;
	private ProfileLine RM0310;
	private ProfileLine RM0311;
	private ProfileLine RM0312;

	private ProfileLine RM0401;
	private ProfileLine RM0402;
	private ProfileLine RM0403;
	private ProfileLine RM0404;
	private ProfileLine RM0405;
	private ProfileLine RM0406;
	private ProfileLine RM0407;
	private ProfileLine RM0408;
	private ProfileLine RM0409;
	private ProfileLine RM0410;

	private ProfileLine RM0501;
	private ProfileLine RM0502;
	private ProfileLine RM0503;
	private ProfileLine RM0504;

	private ProfileLine RM0601;
	private ProfileLine RM0602;
	private ProfileLine RM0603;
	private ProfileLine RM0604;
	private ProfileLine RM0605;
	private ProfileLine RM0606;
	private ProfileLine RM0607;



	private List<ProfileLine> profileLines = new ArrayList<>();
	private static Map<String, Method> profileSetterMethods = new HashMap<>();

	/**
	 * Add newly added menu option declared in PROFILE list
	 */
	static {
		setterCaller();
	}

	public static void setterCaller() {
		EnumSet.allOf(ReportMenu.class).forEach(rm -> addSetter(rm.name(), rm.name()));
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
		if(profileLine == null || StringUtils.isEmpty(profileLine.getProfilelinecode())) {
			log.warn("No profile or profile code found....");
			return;
		}

		String code = profileLine.getProfilelinecode().toUpperCase();
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
	public void setRM0301(ProfileLine profileLine) {
		this.RM0301 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0302(ProfileLine profileLine) {
		this.RM0302 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0303(ProfileLine profileLine) {
		this.RM0303 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0304(ProfileLine profileLine) {
		this.RM0304 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0305(ProfileLine profileLine) {
		this.RM0305 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0306(ProfileLine profileLine) {
		this.RM0306 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0307(ProfileLine profileLine) {
		this.RM0307 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0308(ProfileLine profileLine) {
		this.RM0308 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0309(ProfileLine profileLine) {
		this.RM0309 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0310(ProfileLine profileLine) {
		this.RM0310 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0311(ProfileLine profileLine) {
		this.RM0311 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0312(ProfileLine profileLine) {
		this.RM0312 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setRM0401(ProfileLine profileLine) {
		this.RM0401 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0402(ProfileLine profileLine) {
		this.RM0402 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0403(ProfileLine profileLine) {
		this.RM0403 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0404(ProfileLine profileLine) {
		this.RM0404 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0405(ProfileLine profileLine) {
		this.RM0405 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0406(ProfileLine profileLine) {
		this.RM0406 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0407(ProfileLine profileLine) {
		this.RM0407 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0408(ProfileLine profileLine) {
		this.RM0408 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0409(ProfileLine profileLine) {
		this.RM0409 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0410(ProfileLine profileLine) {
		this.RM0410 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setRM0501(ProfileLine profileLine) {
		this.RM0501 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0502(ProfileLine profileLine) {
		this.RM0502 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0503(ProfileLine profileLine) {
		this.RM0503 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0504(ProfileLine profileLine) {
		this.RM0504 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setRM0601(ProfileLine profileLine) {
		this.RM0601 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0602(ProfileLine profileLine) {
		this.RM0602 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0603(ProfileLine profileLine) {
		this.RM0603 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0604(ProfileLine profileLine) {
		this.RM0604 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0605(ProfileLine profileLine) {
		this.RM0605 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0606(ProfileLine profileLine) {
		this.RM0606 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0607(ProfileLine profileLine) {
		this.RM0607 = profileLine;
		addToProfileLineMap(profileLine);
	}
	

}
