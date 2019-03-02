package pl.szymanski.iHome.domain.repository;

import org.springframework.stereotype.Repository;
import pl.szymanski.iHome.domain.StoveData;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class StoveRepository {

    private List<StoveData> data = new ArrayList<>();

    public void addNewMeasurement(String temperature) {
        StoveData stoveData = new StoveData(temperature, System.currentTimeMillis());
        System.out.println("Adding: " + temperature + " result: " + data.add(stoveData));
    }

    public StoveData getLastetstMeasurement() {
        return data.get(data.size()-1);
    }

    public Object getOld(long minutes) {
        long current = System.currentTimeMillis();
        for (StoveData stoveData : data) {
            long diffrence = TimeUnit.MILLISECONDS.toMinutes(current - stoveData.getTimeStamp());
            if (diffrence == minutes) {
                return stoveData;
            } else if (diffrence < minutes) {
                return stoveData;
            }
        }
        return null;
    }

    @PostConstruct
    private void init(){
        addNewMeasurement("21.2");
        addNewMeasurement("25.2");
        addNewMeasurement("26.2");
        addNewMeasurement("29.2");
        addNewMeasurement("21.2");
        addNewMeasurement("41.2");
    }
}

