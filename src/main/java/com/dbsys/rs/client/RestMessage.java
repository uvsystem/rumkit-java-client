package com.dbsys.rs.client;

public class RestMessage {
    private String message;
    private Type tipe;
    private Object object;

    public RestMessage() {
        super();
    }

    protected RestMessage(String message, Type tipe) {
        super();
        this.message = message;
        this.object = message;
        this.tipe = tipe;
    }

    protected RestMessage(String message, Type tipe, Object object) {
        this(message, tipe);
        this.object = object;
    }

    protected RestMessage(Exception ex) {
        this(ex.getMessage(), Type.ERROR);
    }

    private RestMessage(Object object) {
        this("Berhasil", Type.OBJECT);
        this.object = object;

        if (object instanceof String) {
            this.message = String.valueOf(object);
            this.tipe = Type.MESSAGE;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getTipe() {
        return tipe;
    }

    public void setTipe(Type tipe) {
        this.tipe = tipe;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public enum Type {
        SUCCESS,
        ERROR,
        MESSAGE,
        ENTITY,
        LIST,
        OBJECT
    }

    public static RestMessage success() {
        return new RestMessage("Berhasil", Type.SUCCESS);
    }

    public static RestMessage success(String message) {
        return new RestMessage(message, Type.SUCCESS);
    }

    public static RestMessage error(Exception cause) {
        return new RestMessage(cause);
    }

    public static RestMessage create(Object object) {
        return new RestMessage(object);
    }

}
