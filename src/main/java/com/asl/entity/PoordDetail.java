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
@Table(name = "poorddetail")
@IdClass(PoordDetailPK.class) // ?
@EqualsAndHashCode(of = { "zid","xpornum" }, callSuper = false)
public class PoordDetail extends AbstractModel<String> {
	
	private static final long serialVersionUID = 4124498529281042988L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;
	
	@Column(name = "xrow")
	private String xrow;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xqtyord")
	private String xqtyord;
	
	@Column(name = "xrate")
	private String xrate;
	
	@Column(name = "xunitpur")
	private String xunitpur;
	
	@Column(name = "xlineamt")
	private String xlineamt;
	
	@Column(name = "xqtygrn")
	private String xqtygrn;
	
	@Column(name = "xcfpur")
	private String xcfpur;
	
	@Column(name = "xqtypur")
	private String xqtypur;

}
