package com.dbsys.rs.client;

/**
 * Kelas untukmen-generate entity menjadi JSON.
 * 
 * @author Deddy Christoper Kakunsi
 *
 * @param <T>
 */
public class EntityRestMessage<T> extends RestMessage {
    
    private T model;

    public EntityRestMessage() {
        super();
    }

    protected EntityRestMessage(Exception ex) {
        super(ex);
    }

    public EntityRestMessage(T model) {
        super("Berhasil", Type.ENTITY, model);
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public static <T> EntityRestMessage<T> entityError(Exception cause) {
        return new EntityRestMessage<>(cause);
    }

}
