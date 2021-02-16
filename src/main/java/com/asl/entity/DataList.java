package com.asl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Data
@Entity
@Table(name = "LS")
@EqualsAndHashCode(of = { "listId" }, callSuper = false)
public class DataList extends AbstractModel<String> {

	private static final long serialVersionUID = 2402818361445233745L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "listId", unique = true, nullable = false)
	private Long listId;
	@Column(name = "listCode", nullable = false, length = 100)
	private String listCode;
	@Column(name = "description", length = 100)
	private String description;
	@Column(name = "listValue1")
	private String listValue1;
	@Column(name = "listValue2")
	private String listValue2;
	@Column(name = "listValue3")
	private String listValue3;
	@Column(name = "listValue4")
	private String listValue4;
	@Column(name = "listValue5")
	private String listValue5;
	@Column(name = "listValue6")
	private String listValue6;
	@Column(name = "listValue7")
	private String listValue7;
	@Column(name = "listValue8")
	private String listValue8;
	@Column(name = "listValue9")
	private String listValue9;
	@Column(name = "listValue10")
	private String listValue10;
	@Column(name = "listValue11")
	private String listValue11;
	@Column(name = "listValue12")
	private String listValue12;
	@Column(name = "listValue13")
	private String listValue13;
	@Column(name = "listValue14")
	private String listValue14;
	@Column(name = "listValue15")
	private String listValue15;
	@Column(name = "listValue16")
	private String listValue16;
	@Column(name = "extraValue1")
	private String extraValue1;
	@Column(name = "extraValue2")
	private String extraValue2;
	@Column(name = "extraValue3")
	private String extraValue3;
	@Column(name = "extraValue4")
	private String extraValue4;
	@Column(name = "extraValue5")
	private String extraValue5;
}
