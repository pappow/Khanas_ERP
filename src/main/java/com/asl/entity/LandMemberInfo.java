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
@Table(name = "camember")
@IdClass(LandMemberInfoPK.class)
@EqualsAndHashCode(of = { "zid", "xmember" }, callSuper = false)
public class LandMemberInfo extends AbstractModel<String> {
	
	private static final long serialVersionUID = 4936313608548605124L;
	
	
	@Id
	@Basic(optional=false)
	@Column(name="zid")
	private String zid;

	
	@Id
	@Basic(optional=false)
	@Column(name="xmember")
	private String xmember;
	
	@Column(name="xperson")
	private String xperson;
	
	@Column(name="xdesignation ")
	private String xdesignation;
	
	@Column(name="xnote")
	private String xnote;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

}
