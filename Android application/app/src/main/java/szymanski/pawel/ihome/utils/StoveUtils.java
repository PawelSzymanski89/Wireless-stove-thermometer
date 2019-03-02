package szymanski.pawel.ihome.utils;

import android.util.Log;
import szymanski.pawel.ihome.api.Requests;
import szymanski.pawel.ihome.domain.pojo.StoveData;
import szymanski.pawel.ihome.domain.StoveStatus;

public class StoveUtils {

    private double temperature;
    private Requests requests;

    public StoveUtils() {
        this.requests = new Requests();
    }

    public StoveStatus getStoveStatus() {
        StoveData stoveData = requests.getStoveData();
        StoveData stoveOldData = requests.getOldStoveData(60);
        if (stoveData != null) {
            temperature = stoveData.getTemperature();
            long timeDiffrence = System.currentTimeMillis() - stoveData.getTimeStamp();
            Log.d("TIME DIFFRENCE", timeDiffrence + " mils");
            if (timeDiffrence > 30000)
                return new StoveStatus(temperature, StoveStatus.STATUS_OUTDATED);
            if (stoveOldData.getTemperature() > 48 && stoveData.getTemperature() < 48)
                return new StoveStatus(temperature, StoveStatus.STATUS_STOPING);

        } else {
            new StoveStatus(StoveStatus.STATUS_ERROR);
        }
        return new StoveStatus(temperature, StoveStatus.STATUS_WORKING);
    }
}
