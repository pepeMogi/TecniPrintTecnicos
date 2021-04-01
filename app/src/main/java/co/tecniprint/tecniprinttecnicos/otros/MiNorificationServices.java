package co.tecniprint.tecniprinttecnicos.otros;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MiNorificationServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Looper.prepare();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
               /* Toast.makeText(MiNorificationServices.this, remoteMessage.getNotification()
                        .getTitle(), Toast.LENGTH_SHORT).show();*/
                Toast.makeText(MiNorificationServices.this, "llego algo", Toast.LENGTH_SHORT).show();
                Log.e("MESSAGE",remoteMessage.getNotification().getTitle());
            }
        });

        Looper.loop();
    }
}
