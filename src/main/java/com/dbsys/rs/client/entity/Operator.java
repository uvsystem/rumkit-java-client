package com.dbsys.rs.client.entity;

/**
 * Representasi tabel operator.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public class Operator {

    public enum Role {
        ADMIN, OPERATOR
    }

    private Long id;
    private String username;
    private String password;
    private Role role;
    private String nama;
    private Unit unit;

    public Operator() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Mengambil username operator.
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Mengatur username operator.
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Mengambil password operator.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mengatur password operator.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return role operator.
     * 
     * @return Role operator
     */
    public Role getRole() {
        return role;
    }

    /**
     * Mengatur role operator.
     * 
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
            this.nama = nama;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nama == null) ? 0 : nama.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Operator other = (Operator) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nama == null) {
            if (other.nama != null)
                return false;
        } else if (!nama.equals(other.nama))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (role != other.role)
            return false;
        if (unit == null) {
            if (other.unit != null)
                return false;
        } else if (!unit.equals(other.unit))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}
