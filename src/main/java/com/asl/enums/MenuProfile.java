package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
public enum MenuProfile {

	DASH(510, "DASH", "Dashboard", "dashboard", "Y", "/dashboard"),
	PRFL(520, "PRFL", "Profile", "profileMaintenance", "N", "/system/profile"),
	PRAL(530, "PRAL", "Proflie allocation", "profileAllocation", "N", "/system/profileallocation"),
	LIST(540, "LIST", "List", "listMaintenance", "Y", "/system/list"),
	LSIE(550, "LSIE", "List Export Import", "listImportExport", "Y", "/importexport/listimportexport"),
	REPT(560, "REPT", "Report", "report", "Y", "/report"),
	CONT(570, "CONT", "Contact", "contact", "Y", "/contact"),
	XUSR(580, "XUSR", "Manage User", "xuser", "Y", "/user"),
	SHOP(590, "SHOP", "Shop", "shop", "Y", "/shop");

	private int seqn;
	private String code;
	private String description;
	private String profileField;
	private String defaultAccess;
	private String menuPath;

	private MenuProfile(int seqn, String code, String desc, String profileField, String defaultAccess, String path) {
		this.seqn = seqn;
		this.code = code;
		this.description = desc;
		this.profileField = profileField;
		this.defaultAccess = defaultAccess;
		this.menuPath = path;
	}

	public int getSeqn() {
		return seqn;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getProfileField() {
		return profileField;
	}

	public String getDefaultAccess() {
		return defaultAccess;
	}

	public String getMenuPath() {
		return menuPath;
	}
}
