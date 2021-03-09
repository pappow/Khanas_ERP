package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "users")
@IdClass(UsersPK.class)
@EqualsAndHashCode(of = { "username","businessid" }, callSuper = false)
public class Users extends AbstractModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4314117183238230351L;

	@Id
	@Basic(optional = false)
	@Column(name = "username")
	private String username;

	@Id
	@Basic(optional = false)
	@Column(name = "businessid")
	private String businessid;
	
	@Column(name = "createdate")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdate;
	
	@Column(name = "createdby")
	private String createdby;
	
	@Column(name = "createdbyip")
	private String createdbyip;
	
	@Column(name = "lastmodifiedby")
	private String lastmodifiedby;
	
	@Column(name = "lastmodifiedbyip")
	private String lastmodifiedbyip;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "updatedate")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date updatedate;
	
	@Column(name = "admin")
	private String admin;
	
	@Column(name = "appuser")
	private String appuser;
	
	@Column(name = "driver")
	private String driver;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "pswd")
	private String pswd;
	
	@Column(name = "role1")
	private String role1;
	
	@Column(name = "role2")
	private String role2;
	
	@Column(name = "role3")
	private String role3;
	
	@Column(name = "role4")
	private String role4;
	
	@Column(name = "role5")
	private String role5;
	
	@Column(name = "role6")
	private String role6;
	
	@Column(name = "role7")
	private String role7;
	
	@Column(name = "role8")
	private String role8;
	
	@Column(name = "role9")
	private String role9;
	
	@Column(name = "role10")
	private String role10;
	
	@Column(name = "subscriber")
	private String subscriber;
	
	@Column(name = "systemadmin")
	private String systemadmin;
	
}
