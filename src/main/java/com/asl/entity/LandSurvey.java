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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "landsurvey")
@IdClass(LandSurveyPK.class)
@EqualsAndHashCode(of = { "zid", "xland","xrow"}, callSuper = false)
public class LandSurvey extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3036589229579591189L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xland")
	private String xland;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xsurveyor")
	private String xsurveyor;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Transient
	private boolean newData;

}
