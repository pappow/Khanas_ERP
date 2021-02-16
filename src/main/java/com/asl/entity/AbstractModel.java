package com.asl.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.asl.enums.RecordStatus;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
/**
 * @author Zubayer Ahamed
 *
 * @since Nov 28, 2020
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractModel<U> implements Serializable {

	private static final long serialVersionUID = -3736149934368733226L;

	@Column(name = "bsid")
	private String businessId;

	@CreatedBy
	@Column(name = "ctby")
	private U createdBy;

	@LastModifiedBy
	@Column(name = "lmod")
	private U lastModifiedBy;

	@Column(name = "crip")
	private String createdByIP;

	@Column(name = "mdip")
	private String lastModifiedByIP;

	@Column(name = "status", length = 1)
	@Enumerated(EnumType.STRING)
	private RecordStatus status = RecordStatus.L;

	public RecordStatus getStatus() {
		if(this.status == null) return RecordStatus.L;
		return status;
	}

	@CreationTimestamp
	@Column(name = "createDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@UpdateTimestamp
	@Column(name = "updateDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate = new Date();

	@Transient
	private String copyId;

	@PreUpdate
	public void onUpdate() {
		try {
			setLastModifiedByIP(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@PrePersist
	public void onPersist() {
		try {
			setCreatedByIP(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
