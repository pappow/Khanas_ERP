package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
@Entity
@Table(name = "xcodes")
@IdClass(XcodesPK.class)
@EqualsAndHashCode(of = { "xcode" }, callSuper = false)
public class Xcodes extends AbstractModel<String> {

	private static final long serialVersionUID = 304110246928300496L;

	@Id
	@Basic(optional = false)
	@Column(name = "xcode")
	private String xcode;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xdescdet")
	private String xdescdet;

	@Column(name = "xprops")
	private String xprops;

	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xAccDisc")
	private String xAccDisc;
	
	@Column(name = "xAccVat")
	private String xAccVat;

	@Column(name = "xAccAit")
	private String xAccAit;

	@Column(name = "xAccPur")
	private String xAccPur;

	@Column(name = "xAccAdj")
	private String xAccAdj;

	@Column(name = "xAccCash")
	private String xAccCash;

	@Column(name = "xAccBank")
	private String xAccBank;

	@Column(name = "xAccArdBank")
	private String xAccArdBank; 
}
