package com.synnex.shellexecutor.bo;

public class JsonEntity<T> implements java.io.Serializable {
    private static final long serialVersionUID = -1771426378340695807L;
    T data;
    private int status = 200;
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> JsonEntity<T> of(T data) {
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setData(data);
        return jsonEntity;
    }

    @Override
    public String toString() {
        return "JsonEntity{" +
               "status=" + status +
               ", message='" + message + '\'' +
               ", data=" + data +
               '}';
    }
}
