/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asl.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 *
 * @author Administrator
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "aslbusiness")
@XmlRootElement
public class ASLBusiness implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "businessId")
	private String businessId;

	@Column(name = "businessName")
	private String businessName;

}
