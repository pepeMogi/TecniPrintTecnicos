package co.tecniprint.tecniprinttecnicos.otros;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.tecniprint.tecniprinttecnicos.pruebas.Pruebas;

public class MyNotificacion {

    private Context context;

    public MyNotificacion(Context context) {
        this.context = context;
    }

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAgoHWsk8:APA91bEB-8lk3e2wsLGzOBIFVhm-4_2oo13RDpY7BSgMpyUZbgryu8HdzpZn5KqQsKLfw1beNnd8-oSyG46zxWuzT7Go0v_B9-wCg5fh_8gus6BZcqTupi8LjKYZxbY9vEQuYqU7RF6-";
    final private String contentType = "application/json";

    public void enviarNotificacion(String titulo, String mensaje, String img) {




        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", titulo);
            notifcationBody.put("body", mensaje);
            notifcationBody.put("image", img);

            notification.put("to", "/topics/torre");
            notification.put("notification", notifcationBody);
        } catch (JSONException e) {
            Log.e("ERROR", "onCreate: " + e.getMessage() );
        }




        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("E", "onResponse: " + response.toString());
                        //edtTitle.setText("");
                        //edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Pruebas.this, "Request error", Toast.LENGTH_LONG).show();
                        //Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
