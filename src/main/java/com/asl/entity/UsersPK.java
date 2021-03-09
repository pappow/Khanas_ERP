package com.asl.entity;
import java.io.Serializable;

import lombok.Data;
@Data
public class UsersPK implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 5479867862989811145L;

	private String username;
	private String businessid;
}
