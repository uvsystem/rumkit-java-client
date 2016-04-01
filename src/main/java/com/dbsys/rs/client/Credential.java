package com.dbsys.rs.client;

/**
 * Kelas untuk menampung data password dari JSON.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public class Credential {

    private String username;
    private String password;

    /**
     * Mengambil username dari JSON.
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Mengatur username pada JSON.
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Mengambil password dari JSON.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mengatur password pada JSON.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
