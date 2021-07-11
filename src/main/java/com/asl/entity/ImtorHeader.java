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
@Table(name = "imtorheader")
@IdClass(ImtorHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xtornum" }, callSuper = false)
public class ImtorHeader extends AbstractModel<String> {

	private static final long serialVersionUID = -1246892187349642823L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private String xtornum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;

	@Column(name = "xfwh")
	private String xfwh;

	@Column(name = "xtwh")
	private String xtwh;

	@Column(name = "xstatustor")
	private String xstatustor;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xchalanref")
	private String xchalanref;

	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Transient
	private String xorg;
}
