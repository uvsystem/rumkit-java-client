package com.dbsys.rs.client;

import java.io.Serializable;

/**
 * General exception untuk semua error yang mungkin terjadi dalam aplikasi.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public class ApplicationException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message) {
		super(message);
	}
}
