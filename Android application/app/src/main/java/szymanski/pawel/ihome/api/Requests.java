package szymanski.pawel.ihome.api;

import android.util.Log;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import szymanski.pawel.ihome.domain.pojo.StoveData;
import szymanski.pawel.ihome.utils.Utils;

public class Requests {

    private OkHttpClient client;

    public Requests() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(4, TimeUnit.SECONDS)
                .readTimeout(6, TimeUnit.SECONDS)
                .build();
    }

    public StoveData getStoveData() {
        Request request = new Request.Builder()
                .url(Utils.SERVER_ADRESS + "/getStoveTemperature")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            return new Gson().fromJson(jsonObject.getString("message"), StoveData.class);
        } catch (IOException | JSONException e) {
            Log.e("getStoveData", e.getMessage());
        }
        return null;
    }

    public StoveData getOldStoveData(long minutes) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Utils.SERVER_ADRESS + "/getOld").newBuilder();
        urlBuilder.addQueryParameter("minutes", String.valueOf(minutes));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            return new Gson().fromJson(jsonObject.getString("message"), StoveData.class);
        } catch (IOException | JSONException e) {
            Log.e("getOldStoveData", e.getMessage());
        }
        return null;
    }



}
