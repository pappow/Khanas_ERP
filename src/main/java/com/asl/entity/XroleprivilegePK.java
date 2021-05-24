package com.asl.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author ASL
 * @since Jan 30, 2021
 */
@Embeddable
public class XroleprivilegePK implements Serializable {

	private static final long serialVersionUID = -2415435591799865512L;

	@Basic(optional = false)
	@Column(name = "xrole")
	private String xrole;
	@Basic(optional = false)
	@Column(name = "zscreen")
	private String zscreen;

	public XroleprivilegePK() {
	}

	public XroleprivilegePK(String xrole, String zscreen) {
		this.xrole = xrole;
		this.zscreen = zscreen;
	}

	public String getXrole() {
		return xrole;
	}

	public void setXrole(String xrole) {
		this.xrole = xrole;
	}

	public String getZscreen() {
		return zscreen;
	}

	public void setZscreen(String zscreen) {
		this.zscreen = zscreen;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (xrole != null ? xrole.hashCode() : 0);
		hash += (zscreen != null ? zscreen.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof XroleprivilegePK)) {
			return false;
		}
		XroleprivilegePK other = (XroleprivilegePK) object;
		if ((this.xrole == null && other.xrole != null) || (this.xrole != null && !this.xrole.equals(other.xrole))) {
			return false;
		}
		if ((this.zscreen == null && other.zscreen != null)
				|| (this.zscreen != null && !this.zscreen.equals(other.zscreen))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.zab.bean.XroleprivilegePK[ xrole=" + xrole + ", zscreen=" + zscreen + " ]";
	}

}
