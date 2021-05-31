package com.asl.entity;

import java.util.Date;

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
@Table(name = "acmst")
@IdClass(AcmstPK.class)
@EqualsAndHashCode(of = { "zid", "xacc" }, callSuper = false)
public class Acmst extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3861938671021034446L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private String xacc;
	
	@Column(name = "xgroup")
	private String xgroup;
	
	@Column(name = "xdecs")
	private String xdecs;
	
	@Column(name = "xacctype")
	private String xacctype;
	
	@Column(name = "xhrc1")
	private String xhrc1;
	
	@Column(name = "xhrc2")
	private String xhrc2;

	@Column(name = "xhrc3")
	private String xhrc3;
	
	@Column(name = "xhrc4")
	private String xhrc4;
	
	@Column(name = "xhrc5")
	private String xhrc5;
	
	@Column(name = "xnum")
	private Integer xnum;
	
	@Column(name = "xaccusage")
	private String xaccusage;
	
}
