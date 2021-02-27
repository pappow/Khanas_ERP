package com.asl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.asl.enums.ProfileType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Data
@Entity
@Table(name = "Profile")
@EqualsAndHashCode(of = { "profileId" }, callSuper = false)
public class Profile extends AbstractModel<String> {

	private static final long serialVersionUID = 2616243655037864169L;

	@Column(name = "zid", nullable = false)
	private String zid;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "profileId", unique = true, nullable = false)
	private Long profileId;

	@Column(name = "profileCode", nullable = false)
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profileCode;

	@Column(name = "description")
	@Size(max = 500, message = "Description maximum 500 character allowed")
	private String description;

	@Column(name = "profileType")
	@Enumerated(EnumType.STRING)
	private ProfileType profileType;
}
