package com.asl.entity;

import java.math.BigDecimal;

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
@Table(name = "opcrndetail")
@IdClass(PocrndetailPK.class)
@EqualsAndHashCode(of = { "zid","xrow", "xcrnnum" }, callSuper = false)
public class Opcrndetail extends AbstractModel<String>{

	private static final long serialVersionUID = -8703054248831312503L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private String xcrnnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyord")
	private BigDecimal xqtyord;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xlong")
	private BigDecimal xlong;
	
	//We didn't kept vat system for indivisual item
	@Column(name = "xvatrate")
	private BigDecimal xvatrate;
	
	//unknown field
	@Column(name = "xsrate")
	private BigDecimal xsrate;
	
	//What is the use of grn rate in 
	@Column(name = "xrategrn")
	private BigDecimal xrategrn;
	
	@Column(name = "xdocrow")
	private Integer xdocrow;

}
