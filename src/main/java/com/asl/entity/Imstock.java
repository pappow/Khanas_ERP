package com.asl.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "imstock")
@IdClass(ImstockPK.class)
@EqualsAndHashCode(of = { "zid", "xitem", "xwh" }, callSuper = false)
public class Imstock extends AbstractModel<String>{

	private static final long serialVersionUID = -8592245858082968887L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private String xitem;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xdesc")
	private String xdesc;
	
	@Column(name = "xrate")
	private Integer xrate;
	
	@Column(name = "xmrp")
	private String xmrp;
	
	@Column(name = "xdealerp")
	private Integer xdealerp;
	
	@Column(name = "xgitem")
	private String xgitem;
	
	@Column(name = "xcatitem")
	private String xcatitem;
	
	@Column(name = "xopalc")
	private Integer xopalc;
	
	@Column(name = "xqtyit")
	private Integer xqtyit;
	
	@Column(name = "xqtyrt")
	private Integer xqtyrt;
	
	@Column(name = "xinhand")
	private Integer xinhand;
	
	@Column(name = "xavail")
	private Integer xavail;
}
