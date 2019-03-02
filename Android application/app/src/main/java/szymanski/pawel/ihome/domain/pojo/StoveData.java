package szymanski.pawel.ihome.domain.pojo;

public class StoveData {

    private double temperature;
    private long timeStamp;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "StoveData{" +
                "temperature=" + temperature +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
