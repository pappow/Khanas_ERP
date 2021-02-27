package com.asl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.asl.enums.MenuProfile;
import com.asl.enums.ProfileType;

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
@Table(name = "ProfileLine")
@EqualsAndHashCode(of = { "profileLineId" }, callSuper = false)
public class ProfileLine extends AbstractModel<String> {

	private static final long serialVersionUID = -9011140154721641297L;

	@Column(name = "zid", nullable = false)
	private String zid;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "profileLineId", unique = true, nullable = false)
	private Long profileLineId;

	@Column(name = "profileId")
	private Long profileId;

	@Column(name = "profileCode", nullable = false)
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profileCode;

	@Column(name = "profileType")
	@Enumerated(EnumType.STRING)
	private ProfileType profileType;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "display")
	private boolean display;

	@Column(name = "required")
	private boolean required;

	@Column(name = "screenPrompt")
	private String screenPrompt;

	@Column(name = "seqn")
	private int seqn;

	@Transient
	private String managerPrompt;

	public ProfileLine(DataList dl) {
		if(dl == null || StringUtils.isBlank(dl.getListValue1())) return;
		try {
			this.seqn = Integer.parseInt(dl.getListValue2());
			this.profileType = ProfileType.valueOf(dl.getListValue3());
		} catch (Exception e) {
			log.error("Errir is : {}, {}", e.getMessage(), e);
			return;
		}
		this.profileCode = dl.getListValue1();
		this.managerPrompt = dl.getListValue4();
		this.screenPrompt = dl.getListValue5();
		this.display = "Y".equalsIgnoreCase(dl.getListValue6());
		this.required = "Y".equalsIgnoreCase(dl.getListValue7());
	}

	public ProfileLine(MenuProfile rm) {
		this.seqn = rm.getSeqn();
		this.profileType = ProfileType.M;
		this.profileCode = rm.getCode();
		this.managerPrompt = rm.getDescription();
		this.screenPrompt = rm.getDescription();
		this.display = "Y".equalsIgnoreCase(rm.getDefaultAccess());
		this.required = false;
	}
}
