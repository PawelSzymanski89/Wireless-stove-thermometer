package szymanski.pawel.ihome.domain;

public class StoveStatus {

    public static final int STATUS_WORKING = 1;
    public static final int STATUS_STARTING = 2;
    public static final int STATUS_STOPING = 3;
    public static final int STATUS_ERROR = 4;
    public static final int STATUS_OUTDATED = 5;

    private double temperature;
    private int status;

    public StoveStatus(double temperature, int status) {
        this.temperature = temperature;
        this.status = status;
    }

    public StoveStatus(int status) {
        this.status = status;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getStatus() {
        return status;
    }

}
