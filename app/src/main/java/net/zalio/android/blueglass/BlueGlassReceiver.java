package net.zalio.android.blueglass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Henry on 1/4/14.
 */
public class BlueGlassReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DiscoverBroadcastService.isRunning) {
            context.startService(new Intent(context, DiscoverBroadcastService.class));
        }
    }
}
