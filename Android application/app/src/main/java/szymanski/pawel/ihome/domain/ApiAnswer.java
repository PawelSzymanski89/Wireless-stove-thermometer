package szymanski.pawel.ihome.domain;

public class ApiAnswer {

    public static final int RESPONSE_CODE_200 = 200; //OK
    public static final int RESPONSE_CODE_408 = 408; //TIMEOUT

    private int responseCode;
    private String responseBody;

    public ApiAnswer(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public ApiAnswer(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "ApiAnswer{" +
                "responseCode=" + responseCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
