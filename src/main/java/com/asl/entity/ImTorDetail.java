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
@Table(name = "imtordetail")
@IdClass(ImTorDetailPK.class)
@EqualsAndHashCode(of = { "zid", "xtornum", "xrow" }, callSuper = false)
public class ImTorDetail extends AbstractModel<String> {/**
	 * 
	 */
	private static final long serialVersionUID = 7944093590743606577L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private String xtornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private String xrow;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyord")
	private int xqtyord;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xrate")
	private int xrate;
}
