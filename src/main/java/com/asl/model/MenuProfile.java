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

	private ProfileLine M0100;
	private ProfileLine M0101;
	private ProfileLine M0102;
	private ProfileLine M0103;
	private ProfileLine M0104;
	private ProfileLine M0105;
	private ProfileLine M0106;
	private ProfileLine M0107;
	private ProfileLine M0108;
	private ProfileLine M0109;
	private ProfileLine M0110;
	private ProfileLine M0111;

	private ProfileLine M0200;
	private ProfileLine M0201;
	private ProfileLine M0202;
	private ProfileLine M0203;
	private ProfileLine M0204;
	private ProfileLine M0205;
	private ProfileLine M0206;

	private ProfileLine M0300;
	private ProfileLine M0301;
	private ProfileLine M0302;
	private ProfileLine M0303;
	private ProfileLine M0304;
	private ProfileLine M0305;
	private ProfileLine M0306;
	private ProfileLine M0307;

	private ProfileLine M0400;
	private ProfileLine M0401;
	private ProfileLine M0402;
	private ProfileLine M0403;
	private ProfileLine M0404;
	private ProfileLine M0405;
	private ProfileLine M0406;
	private ProfileLine M0407;
	private ProfileLine M0408;
	private ProfileLine M0409;
	private ProfileLine M0410;
	private ProfileLine M0411;

	private ProfileLine M0500;
	private ProfileLine M0501;
	private ProfileLine M0502;
	private ProfileLine M0503;
	private ProfileLine M0504;

	private ProfileLine M0600;
	private ProfileLine M0601;
	private ProfileLine M0602;
	private ProfileLine M0603;
	private ProfileLine M0604;
	private ProfileLine M0605;

	private ProfileLine M0700;

	private ProfileLine M0800;
	private ProfileLine M0801;
	private ProfileLine M0802;
	private ProfileLine M0803;

	private ProfileLine M0900;
	private ProfileLine M0901;
	private ProfileLine M0902;
	private ProfileLine M0903;

	private List<ProfileLine> profileLines = new ArrayList<>();
	private static Map<String, Method> profileSetterMethods = new HashMap<>();

	/**
	 * Add newly added menu option declared in PROFILE list
	 */
	static {
		setterCaller();
	}

	public static void setterCaller() {
		EnumSet.allOf(com.asl.enums.MenuProfile.class).forEach(mp -> addSetter(mp.getCode(), mp.getCode()));
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
	public void setM0100(ProfileLine profileLine) {
		this.M0100 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0101(ProfileLine profileLine) {
		this.M0101 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0102(ProfileLine profileLine) {
		this.M0102 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0103(ProfileLine profileLine) {
		this.M0103 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0104(ProfileLine profileLine) {
		this.M0104 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0105(ProfileLine profileLine) {
		this.M0105 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0106(ProfileLine profileLine) {
		this.M0106 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0107(ProfileLine profileLine) {
		this.M0107 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0108(ProfileLine profileLine) {
		this.M0108 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0109(ProfileLine profileLine) {
		this.M0109 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0110(ProfileLine profileLine) {
		this.M0110 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0111(ProfileLine profileLine) {
		this.M0111 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0200(ProfileLine profileLine) {
		this.M0200 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0201(ProfileLine profileLine) {
		this.M0201 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0202(ProfileLine profileLine) {
		this.M0202 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0203(ProfileLine profileLine) {
		this.M0203 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0204(ProfileLine profileLine) {
		this.M0204 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0205(ProfileLine profileLine) {
		this.M0205 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0206(ProfileLine profileLine) {
		this.M0206 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0300(ProfileLine profileLine) {
		this.M0300 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0301(ProfileLine profileLine) {
		this.M0301 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0302(ProfileLine profileLine) {
		this.M0302 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0303(ProfileLine profileLine) {
		this.M0303 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0304(ProfileLine profileLine) {
		this.M0304 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0305(ProfileLine profileLine) {
		this.M0305 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0306(ProfileLine profileLine) {
		this.M0306 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0307(ProfileLine profileLine) {
		this.M0307 = profileLine;
		addToProfileLineMap(profileLine);
	}



	public void setM0400(ProfileLine profileLine) {
		this.M0400 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0401(ProfileLine profileLine) {
		this.M0401 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0402(ProfileLine profileLine) {
		this.M0402 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0403(ProfileLine profileLine) {
		this.M0403 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0404(ProfileLine profileLine) {
		this.M0404 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0405(ProfileLine profileLine) {
		this.M0405 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0406(ProfileLine profileLine) {
		this.M0406 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0407(ProfileLine profileLine) {
		this.M0407 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0408(ProfileLine profileLine) {
		this.M0408 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0409(ProfileLine profileLine) {
		this.M0409 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0410(ProfileLine profileLine) {
		this.M0410 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0500(ProfileLine profileLine) {
		this.M0500 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0501(ProfileLine profileLine) {
		this.M0501 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0502(ProfileLine profileLine) {
		this.M0502 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0503(ProfileLine profileLine) {
		this.M0503 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0504(ProfileLine profileLine) {
		this.M0504 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0600(ProfileLine profileLine) {
		this.M0600 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0601(ProfileLine profileLine) {
		this.M0601 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0602(ProfileLine profileLine) {
		this.M0602 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0603(ProfileLine profileLine) {
		this.M0603 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0604(ProfileLine profileLine) {
		this.M0604 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0605(ProfileLine profileLine) {
		this.M0605 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setM0700(ProfileLine profileLine) {
		this.M0700 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setM0800(ProfileLine profileLine) {
		this.M0800 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0801(ProfileLine profileLine) {
		this.M0801 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0802(ProfileLine profileLine) {
		this.M0802 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0803(ProfileLine profileLine) {
		this.M0803 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0900(ProfileLine profileLine) {
		this.M0900 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0901(ProfileLine profileLine) {
		this.M0901 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0902(ProfileLine profileLine) {
		this.M0902 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0903(ProfileLine profileLine) {
		this.M0903 = profileLine;
		addToProfileLineMap(profileLine);
	}

}
