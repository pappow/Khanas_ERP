package com.asl.entity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "aslbusiness")
@EqualsAndHashCode(of = {"zid"}, callSuper = false)
public class AslBusiness extends AbstractModel<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1625350837321833550L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Column(name = "businessname")
	private String businessname;
}
