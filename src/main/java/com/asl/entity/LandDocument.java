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
@Table(name = "landdocument")
@IdClass(LandDocumentPK.class)
@EqualsAndHashCode(of = { "zid","xdoc","xperson","xsurveyor","xland" }, callSuper = false)
public class LandDocument extends AbstractModel<String>{
	
	private static final long serialVersionUID = -1482216443995609989L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xdoc")
	private String xdoc;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xperson")
	private String xperson;

	@Id
	@Basic(optional = false)
	@Column(name = "xsurveyor")
	private String xsurveyor;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xland")
	private String xland;
	
	@Column(name = "xdoctype")
	private String xdoctype;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xdocument")
	private String xdocument;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
	
}
