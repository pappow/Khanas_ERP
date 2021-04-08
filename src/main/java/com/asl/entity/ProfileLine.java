package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.asl.enums.MenuProfile;
import com.asl.enums.ProfileType;
import com.asl.enums.ReportMenu;
import com.asl.enums.TransactionCodeType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Data
@Slf4j
@Entity
@NoArgsConstructor
@IdClass(ProfileLinePK.class)
@Table(name = "profileline")
@EqualsAndHashCode(of = { "zid", "profilelineid", "profilelinecode", "profilecode" }, callSuper = false)
public class ProfileLine extends AbstractModel<String> {

	private static final long serialVersionUID = -9011140154721641297L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "profilelineid")
	private String profilelineid;

	@Id
	@Basic(optional = false)
	@Column(name = "profilelinecode")
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profilelinecode;

	@Id
	@Basic(optional = false)
	@Column(name = "profilecode")
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profilecode;

	@Column(name = "profiletype")
	@Enumerated(EnumType.STRING)
	private ProfileType profiletype;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "display")
	private boolean display;

	@Column(name = "required")
	private boolean required;

	@Column(name = "screenprompt")
	private String screenprompt;

	@Column(name = "seqn")
	private int seqn;

	@Column(name= "xtypetrn")
	private String xtypetrn;

	@Column(name= "xtrn")
	private String xtrn;

	@Column(name = "pgroup")
	private String pgroup;

	@Transient
	private String pgroupname;

	@Column(name = "parent")
	private String parent;

	@Transient
	private String managerprompt;

	public ProfileLine(DataList dl) {
		if(dl == null || StringUtils.isBlank(dl.getListValue1())) return;
		try {
			this.seqn = Integer.parseInt(dl.getListValue2());
			this.profiletype = ProfileType.valueOf(dl.getListValue3());
		} catch (Exception e) {
			log.error("Errir is : {}, {}", e.getMessage(), e);
			return;
		}
		this.profilecode = dl.getListValue1();
		this.managerprompt = dl.getListValue4();
		this.screenprompt = dl.getListValue5();
		this.display = "Y".equalsIgnoreCase(dl.getListValue6());
		this.required = "Y".equalsIgnoreCase(dl.getListValue7());
	}

	public ProfileLine(ReportMenu rm) {
		this.seqn = rm.getSeqn();
		this.pgroup = rm.getGroup();
		this.pgroupname = rm.getGroupName();

		this.profilelinecode = rm.name();
		this.profiletype = ProfileType.R;

		this.managerprompt = rm.getDescription();
		this.screenprompt = rm.getDescription();
		this.display = "Y".equalsIgnoreCase(rm.getDefaultAccess());
		this.required = false;
		this.enabled = rm.isFopEnabled();

		this.xtypetrn = TransactionCodeType.PROFILE_LINE.getCode();
		this.xtrn = TransactionCodeType.PROFILE_LINE.getdefaultCode();
	}

	public ProfileLine(MenuProfile rm) {
		this.seqn = rm.getSeqn();
		this.pgroup = rm.getGroup();
		this.parent = rm.getParent();

		this.profilelinecode = rm.getCode();
		this.profiletype = ProfileType.M;

		this.managerprompt = rm.getDescription();
		this.screenprompt = rm.getDescription();
		this.display = "Y".equalsIgnoreCase(rm.getDefaultAccess());
		this.required = false;

		this.xtypetrn = TransactionCodeType.PROFILE_LINE.getCode();
		this.xtrn = TransactionCodeType.PROFILE_LINE.getdefaultCode();
	}
}
