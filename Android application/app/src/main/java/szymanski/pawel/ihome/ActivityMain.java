package szymanski.pawel.ihome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import szymanski.pawel.ihome.services.StoveService;

public class ActivityMain extends AppCompatActivity {

    public final static int REQUEST_CODE = 3365;

    @BindView(R.id.justtext)
    TextView text;

    public TextView getText() {
        return text;
    }

    private static class MainActivityHandler extends Handler {

        private final WeakReference<ActivityMain> activityMainWeakReference;

        private MainActivityHandler(ActivityMain activityMain) {
            activityMainWeakReference = new WeakReference<>(activityMain);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityMain activityMain = activityMainWeakReference.get();
            if (activityMain != null) {
                //TODO: PREPARE ACTIVITY MAIN SCREEN WIDGETS
                switch (msg.arg1 = 22) {
                    case 22:
                        activityMain.getText().setText((String) msg.obj);
                        break;
                }
            }
        }
    }

    MainActivityHandler handler = new MainActivityHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final IntentFilter myFilter = new IntentFilter(StoveService.ACTION_FROM_SERVICE);
        registerReceiver(mReceiver, myFilter);

        checkDrawOverlayPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, StoveService.class));
            }
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            startService(new Intent(this, StoveService.class));
        }

    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getCharSequenceExtra("data");
        }
    };

}

