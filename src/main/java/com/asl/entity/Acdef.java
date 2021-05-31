package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "acdef")
public class Acdef{

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Column(name = "xyear")
	private String xyear;
	
	@Column(name = "xper")
	private Integer xper;
	
	@Column(name = "xoffset")
	private Integer xoffset;
	
	@Column(name = "xaccpl")
	private String xaccpl;
	
	@Column(name = "xaccrule")
	private String xaccrule;
	
	@Column(name = "xgetper")
	private String xgetper;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xlength")
	private Integer xlength;
	
	@Column(name = "xaccgl")
	private String xaccgl;
	
	@Column(name = "xyesno")
	private String xyesno;
	
	@Column(name = "xday")
	private Integer xday;
	
	@Column(name = "xdatedue")
	@Temporal(TemporalType.DATE)
	private Date xdatedue;
	
	@Column(name = "xpreventry")
	private String xpreventry;
	
	@Column(name = "xdateexp")
	@Temporal(TemporalType.DATE)
	private Date xdatxdateexpedue;
	
	@Column(name = "xbanklock")
	private String xbanklock;
	
}
