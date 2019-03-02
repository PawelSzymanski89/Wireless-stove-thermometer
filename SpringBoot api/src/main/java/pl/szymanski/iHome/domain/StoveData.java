package pl.szymanski.iHome.domain;


public class StoveData {
    private String temperature;
    private long timeStamp;


    public StoveData(String temperature, long timeStamp) {
        this.temperature = temperature;
        this.timeStamp = timeStamp;
    }

    public String getTemperature() {
        return temperature;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "StoveData{" +
                "temperature='" + temperature + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
