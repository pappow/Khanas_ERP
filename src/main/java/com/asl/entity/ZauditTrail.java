package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "zaudittrail")
@EqualsAndHashCode(of = { "zauditid" }, callSuper = false)
public class ZauditTrail extends AbstractModel<String>{/**
	 * 
	 */
	private static final long serialVersionUID = 655414790827535367L;

	@Id
	@Basic(optional = false)
	@Column(name = "zauditid")
	private String zauditid;
	
	@Column(name = "zemail")
	private String zemail;
	
	@Column(name = "zscreen")
	private String zscreen;
	
	@Column(name = "zstatement")
	private String zstatement;
	
	@Column(name = "zpkey")
	private String zpkey;
	
	@Column(name = "zmessage")
	private String zmessage;
	
	@Column(name = "zcommand")
	private String zcommand;
	
}
