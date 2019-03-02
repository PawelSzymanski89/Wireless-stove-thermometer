package szymanski.pawel.ihome.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import szymanski.pawel.ihome.R;
import szymanski.pawel.ihome.domain.StoveStatus;
import szymanski.pawel.ihome.utils.StoveUtils;

public class StoveService extends IntentService {

    public static final String ACTION_START = "szymanski.pawel.ihome.broadcast.action.START";
    public static final String ACTION_STOP = "szymanski.pawel.ihome.broadcast.action.STOP";
    public static final String ACTION_TO_SERVICE = "szymanski.pawel.ihome.broadcast.action.TO_SERVICE";
    public static final String ACTION_FROM_SERVICE = "szymanski.pawel.ihome.broadcast.action.FROM_SERVICE";

    private Handler handler;
    private Runnable runnable;

    private TextView temperatureTextView;


    public StoveService() {
        super("Stove temperature monitor service");
    }

    @Override
    public void onCreate() {
        handler = new MonitorHandler(this);
        handler.sendEmptyMessage(666);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        runnable = () -> new GetTemperature(this).execute();
        runnable.run();
    }


    private void sendToActivity(StoveStatus data) {
        Log.d("TMS", "Sending message to service: " + data.getStatus() + " " + data.getTemperature());
        Intent intent = new Intent(ACTION_FROM_SERVICE);
        intent.putExtra("StoveStatus", new ArrayList<StoveStatus>().add(data));
        sendBroadcast(intent, szymanski.pawel.ihome.Manifest.permission.ALLOW);
    }

    private void addTextViewToScreen() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                statusBarHeight,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.CENTER;

        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParameteres);
        temperatureTextView = new TextView(this);
        temperatureTextView.setTextSize(15);
        temperatureTextView.setTextColor(Color.RED);
        temperatureTextView.setText("n/a");
        ll.addView(temperatureTextView);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(ll, parameters);
    }

    private static class GetTemperature extends AsyncTask<Void, Void, StoveStatus> {

        private StoveUtils stoveUtils = new StoveUtils();
        private WeakReference<StoveService> serviceReference;

        GetTemperature(StoveService stoveService) {
            serviceReference = new WeakReference<>(stoveService);
        }

        @Override
        protected StoveStatus doInBackground(Void... voids) {
            return stoveUtils.getStoveStatus();
        }

        @Override
        protected void onPostExecute(StoveStatus stoveStatus) {
            StoveService stoveService = serviceReference.get();
            if (stoveService == null) return;

            Message msg = new Message();
            msg.arg1 = stoveStatus.getStatus();
            msg.obj = stoveStatus;
            stoveService.handler.sendMessage(msg);
            stoveService.sendToActivity(stoveStatus);
            stoveService.handler.postDelayed(stoveService.runnable, 10000);
        }
    }

    private static class MonitorHandler extends Handler {
        private final WeakReference<StoveService> temperatureMonitorWeakReference;
        private Context context;

        private MonitorHandler(StoveService stoveService) {
            temperatureMonitorWeakReference = new WeakReference<>(stoveService);
            context = stoveService.getApplicationContext();
        }

        private String temperature = "";
        private String CHANEL_ID = "DEFAULT";

        @Override
        public void handleMessage(Message msg) {
            StoveService stoveService = temperatureMonitorWeakReference.get();
            if (stoveService != null) {
                if (msg.what == 666) {
                    stoveService.addTextViewToScreen();
                }
                StoveStatus sd = (StoveStatus) msg.obj;

                if (sd != null) temperature = sd.getTemperature() + " \u00b0C";

                switch (msg.arg1) {
                    case StoveStatus.STATUS_WORKING:
                        stoveService.temperatureTextView.setText(temperature);
                        stoveService.temperatureTextView.setTextColor(Color.GREEN);
                        break;
                    case StoveStatus.STATUS_ERROR:
                        stoveService.temperatureTextView.setText("N/A");
                        stoveService.temperatureTextView.setTextColor(Color.RED);
                        break;
                    case StoveStatus.STATUS_OUTDATED:
                        String warning = "!!! " + temperature;
                        stoveService.temperatureTextView.setText(warning);
                        stoveService.temperatureTextView.setTextColor(Color.YELLOW);
                        break;
                    case StoveStatus.STATUS_STOPING:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                                .setSmallIcon(R.drawable.kiln)
                                .setContentTitle("WYGASANIE PIECA !!!")
                                .setContentText("TEMPERATURA: " + temperature)
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(""))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(889, builder.build());


                        stoveService.temperatureTextView.setText(temperature);
                        stoveService.temperatureTextView.setTextColor(Color.RED);
                        break;
                }
            }
        }
    }

}
