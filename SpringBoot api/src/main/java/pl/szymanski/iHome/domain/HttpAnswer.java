package pl.szymanski.iHome.domain;

public class HttpAnswer {
    private int status;
    private String error;
    private Object message;

    public HttpAnswer(int status, String error, Object message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HttpAnswer{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message=" + message +
                '}';
    }
}
