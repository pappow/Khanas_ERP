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
@Table(name = "landexperience")
@IdClass(LandExperiencePK.class)
@EqualsAndHashCode(of = { "zid", "xperson","xrow"}, callSuper = false)
public class LandExperience extends AbstractModel<String>{
	

	private static final long serialVersionUID = -8985247501677946563L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xperson")
	private String xperson;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xduration")
	private int xduration;
	
	@Column(name = "xdesignation")
	private String xdesignation;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xnote")
	private String xnote;
	
}
