package com.asl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author ASL
 * @since Jan 30, 2021
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "xroleprivilege")
@NamedQueries({ @NamedQuery(name = "Xroleprivilege.findAll", query = "SELECT x FROM Xroleprivilege x") })
public class Xroleprivilege implements Serializable {

	private static final long serialVersionUID = 476805549058959852L;

	@EmbeddedId
	protected XroleprivilegePK xroleprivilegePK;
	@Column(name = "xfields")
	private String xfields;
	@Column(name = "xoption")
	private String xoption;
	@Column(name = "xtype")
	private String xtype;
	@Basic(optional = false)
	@Column(name = "zactive")
	private String zactive;
	@Column(name = "zauserid", updatable = false)
	@CreatedBy
	private String zauserid;
	@Basic(optional = false)
	@Column(name = "zid")
	private int zid;
	@Column(name = "ztime", updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date ztime;
	@Column(name = "zutime")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date zutime;
	@Column(name = "zuuserid")
	@LastModifiedBy
	private String zuuserid;
	@JoinColumn(name = "xrole", referencedColumnName = "xrole", insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Xroles xroles;
	@Column(name = "xshopno")
	private String xshopno;

	public Xroleprivilege() {
	}

	public Xroleprivilege(XroleprivilegePK xroleprivilegePK) {
		this.xroleprivilegePK = xroleprivilegePK;
	}

	public Xroleprivilege(XroleprivilegePK xroleprivilegePK, String zactive, int zid) {
		this.xroleprivilegePK = xroleprivilegePK;
		this.zactive = zactive;
		this.zid = zid;
	}

	public Xroleprivilege(String xrole, String zscreen) {
		this.xroleprivilegePK = new XroleprivilegePK(xrole, zscreen);
	}

	public XroleprivilegePK getXroleprivilegePK() {
		return xroleprivilegePK;
	}

	public void setXroleprivilegePK(XroleprivilegePK xroleprivilegePK) {
		this.xroleprivilegePK = xroleprivilegePK;
	}

	public String getXfields() {
		return xfields;
	}

	public void setXfields(String xfields) {
		this.xfields = xfields;
	}

	public String getXoption() {
		return xoption;
	}

	public void setXoption(String xoption) {
		this.xoption = xoption;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public String getZactive() {
		return zactive;
	}

	public void setZactive(String zactive) {
		this.zactive = zactive;
	}

	public String getZauserid() {
		return zauserid;
	}

	public void setZauserid(String zauserid) {
		this.zauserid = zauserid;
	}

	public int getZid() {
		return zid;
	}

	public void setZid(int zid) {
		this.zid = zid;
	}

	public Date getZtime() {
		return ztime;
	}

	public void setZtime(Date ztime) {
		this.ztime = ztime;
	}

	public Date getZutime() {
		return zutime;
	}

	public void setZutime(Date zutime) {
		this.zutime = zutime;
	}

	public String getZuuserid() {
		return zuuserid;
	}

	public void setZuuserid(String zuuserid) {
		this.zuuserid = zuuserid;
	}

	public Xroles getXroles() {
		return xroles;
	}

	public void setXroles(Xroles xroles) {
		this.xroles = xroles;
	}

	public String getXshopno() {
		return xshopno;
	}

	public void setXshopno(String xshopno) {
		this.xshopno = xshopno;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (xroleprivilegePK != null ? xroleprivilegePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Xroleprivilege)) {
			return false;
		}
		Xroleprivilege other = (Xroleprivilege) object;
		if ((this.xroleprivilegePK == null && other.xroleprivilegePK != null)
				|| (this.xroleprivilegePK != null && !this.xroleprivilegePK.equals(other.xroleprivilegePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.zab.bean.Xroleprivilege[ xroleprivilegePK=" + xroleprivilegePK + " ]";
	}

}
