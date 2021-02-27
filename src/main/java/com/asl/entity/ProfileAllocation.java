package com.asl.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 4, 2021
 */
@Data
@Entity
@Table(name = "ProfileAllocation")
@EqualsAndHashCode(of = { "paid" }, callSuper = false)
public class ProfileAllocation extends AbstractModel<String> {

	private static final long serialVersionUID = -8668618359438471462L;

	@Column(name = "zid", nullable = false)
	private String zid;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "paid", unique = true, nullable = false)
	private Long paid;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "reportProfileId")
	private Long reportProfileId;

	@Column(name = "menuProfileId")
	private Long menuProfileId;

	@Column(name = "userProfileId")
	private Long userProfileId;

	@Transient
	private String fullName;
	@Transient
	private String menuProfileName;
	@Transient
	private String userProfileName;
	@Transient
	private String reportProfileName;
	@Transient
	private List<Profile> reportProfiles;
	@Transient
	private List<Profile> menuProfiles;
}
