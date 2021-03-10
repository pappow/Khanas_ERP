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
@Table(name = "pdmst")
@IdClass(PdmstPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff" }, callSuper = false)
public class Pdmst extends AbstractModel<String> {
	
	private static final long serialVersionUID = -1426693265670268657L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private String xstaff;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xsex")
	private String xsex;
	
	@Column(name = "xposition")
	private String xposition;
	
	@Column(name = "xdesignation")
	private String xdesignation;
	
	@Column(name = "xdeptname")
	private String xdeptname;
	
	@Column(name = "xempcategory")
	private String xempcategory;
	
	@Column(name = "xbirthdate")
	@Temporal(TemporalType.DATE)
	private Date xbirthdate;
	
	@Column(name = "xdatejoin")
	@Temporal(TemporalType.DATE)
	private Date xdatejoin;
	
	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;
	
	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;
	
	@Column(name = "xempstatus")
	private String xempstatus;
	
	@Column(name = "xemptype")
	private String xemptype;
	
	@Column(name = "xdistrct")
	private String xdistrct;
	
	@Column(name = "xreligion")
	private String xreligion;
	
	@Column(name = "xbloodgroup")
	private String xbloodgroup;
	
	@Column(name = "xphone")
	private String xphone;
	
	@Column(name = "xpadd")
	private String xpadd;
	
	@Column(name = "xmobile")
	private String xmobile;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "xmadd")
	private String xmadd;
	
}
