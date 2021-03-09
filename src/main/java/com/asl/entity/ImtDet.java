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
@Table(name = "imtdet")
@IdClass(ImtDetPK.class)
@EqualsAndHashCode(of = { "zid", "xtagnum", "xrow"}, callSuper = false)
public class ImtDet extends AbstractModel<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2277712716108894115L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xtagnum")
	private String xtagnum;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqty")
	private String xqty;
	
	@Column(name = "xstatustag")
	private String xstatustag;
	
}
