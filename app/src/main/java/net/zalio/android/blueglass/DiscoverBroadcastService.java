package net.zalio.android.blueglass;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.androidzeitgeist.ani.discovery.Discovery;
import com.androidzeitgeist.ani.discovery.DiscoveryException;
import com.androidzeitgeist.ani.discovery.DiscoveryListener;

import java.net.InetAddress;

/**
 * Created by Henry on 1/4/14.
 */
public class DiscoverBroadcastService extends Service implements DiscoveryListener {
    private static final String TAG = DiscoverBroadcastService.class.getSimpleName();
    public static String EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE";
    public static String KEY_SWITCH = "net.zalio.android.easyblue.switch";
    public static String KEY_BRIGHTNESS = "net.zalio.android.easyblue.brightness";
    private static final String EXTRA_MESSAGE = "message";

    public static final String ACTION_FIRE = "net.zalio.android.easyblue.PLUGIN_FIRE";

    protected static boolean isRunning = false;
    private Discovery mDiscovery;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        mDiscovery = new Discovery();
        mDiscovery.setDisoveryListener(this);
        try {

            Log.i(TAG, "enabling discovery");
            mDiscovery.enable();
            isRunning = true;
        } catch (DiscoveryException e) {
            Toast.makeText(this, "Enable Discovery Failed!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory");
        isRunning = false;
        if (mDiscovery != null) {
            mDiscovery.disable();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        Toast.makeText(this, "onStartCmd", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDiscoveryStarted() {
        Log.i(TAG, "onDiscoveryStarted");
    }

    @Override
    public void onDiscoveryStopped() {
        Log.i(TAG, "onDiscoveryStopped");
    }

    @Override
    public void onDiscoveryError(Exception exception) {
        Log.e(TAG, "(!) Discovery error: " + exception.getMessage());
    }

    @Override
    public void onIntentDiscovered(InetAddress address, Intent intent) {
        //Toast.makeText(this, "Received Intent! " + intent.getStringExtra(EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "received intent: " + intent.getStringExtra(EXTRA_MESSAGE));
        Intent i = new Intent();
        i.setComponent(new ComponentName("net.zalio.android.easyblue", "net.zalio.android.easyblue.EasyBlueControlService"));
        i.setAction(ACTION_FIRE);
        Bundle b = new Bundle();
        b.putBoolean(KEY_SWITCH, intent.getBooleanExtra(KEY_SWITCH, false));
        b.putInt(KEY_BRIGHTNESS, intent.getIntExtra(KEY_BRIGHTNESS, 0));

        i.putExtra(EXTRA_BUNDLE, b);
        startService(i);
    }
}
