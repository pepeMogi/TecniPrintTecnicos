package co.tecniprint.tecniprinttecnicos.pruebas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.MediaType;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.otros.MySingleton;

public class Pruebas extends AppCompatActivity {
    private static final int IMG = 4;
    private static final int REQUEST_DIRECTORY = 42;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAgoHWsk8:APA91bEB-8lk3e2wsLGzOBIFVhm-4_2oo13RDpY7BSgMpyUZbgryu8HdzpZn5KqQsKLfw1beNnd8-oSyG46zxWuzT7Go0v_B9-wCg5fh_8gus6BZcqTupi8LjKYZxbY9vEQuYqU7RF6-";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;



    private TextView txtPrueba, txtOffline;
    private Button btnSubir, btnEscoger, btnGuardar, btnCamino, btnTxt, btnOffline, btnSubirOffline, btnNotificacion;
    private ProgressBar prbPruebas;
    private ModelPrueba model;
    private ArrayList<String> arreglo = new ArrayList<>();
    private String camino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        setWidget();
        funBotones();
        model = new ViewModelProvider(this).get(ModelPrueba.class);
        escucharNumero();
        escucharSiOffline();


        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setNumero(0);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  String img = arreglo.get(0);
                Bitmap este = getBipmap(Uri.parse(img));
                guardar(este);*/

            }
        });

        btnCamino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogProperties properties = new DialogProperties();
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.DIR_SELECT;
                properties.root = new File(DialogConfigs.STORAGE_DIR);
                properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
                properties.offset = new File(DialogConfigs.DEFAULT_DIR);
                properties.extensions = null;
                properties.show_hidden_files = false;

                FilePickerDialog dialog = new FilePickerDialog(Pruebas.this, properties);
                dialog.setTitle("Select a File");
                dialog.show();

                dialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        Log.e("Camino Diaglo, => ", files[0]);
                    }
                });

            }
        });


        final Diagnostico diagnostico = new Diagnostico("Luis M",
                "este es el diagnostico", "esta es la soluicion", new ArrayList<String>(), new ArrayList<String>(),
                "este el el comentario", Timestamp.now());


        btnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = new Utilidades().diagnosticoToString(diagnostico);
                try {
                    File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "/AA");
                    if (!nuevaCarpeta.exists()) {
                        nuevaCarpeta.mkdir();
                        BufferedWriter br = new BufferedWriter(new FileWriter(nuevaCarpeta, true));
                        br.write(json);
                    }
                    try {
                        File file = new File(nuevaCarpeta, "reporte" + ".txt");
                        file.createNewFile();
                        // codigo para escribir el reporte
                        BufferedWriter br = new BufferedWriter(new FileWriter(file, true));
                        br.write(json);
                        br.close();

                    } catch (Exception ex) {
                        Log.e("Error", "ex: " + ex);
                    }
                } catch (Exception e) {
                    Log.e("Error", "e: " + e);
                }
            }
        });

        btnNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarNotificacion();
            }
        });


    }

    private void enviarNotificacion() {


        TOPIC = "/topics/torre"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "este es titulo";
        NOTIFICATION_MESSAGE = "mensaje de la notificacion";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", "Titulo");
            notifcationBody.put("body", "el mensaje");
            notifcationBody.put("image", "https://firebasestorage.googleapis.com/v0/b/ptecni.appspot.com/o/0812791%20(2).jpg?alt=media&token=d3074d28-82f6-4cea-b1d0-9830b9582185");

            notification.put("to", TOPIC);
            notification.put("notification", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }




        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        //edtTitle.setText("");
                        //edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Pruebas.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void escucharSiOffline() {


        SharedPreferences sharedPref = getSharedPreferences(Constantes.DATOS, MODE_PRIVATE);
        String uri = sharedPref.getString("imgs", null);
        if (uri != null) {
            btnSubirOffline.setVisibility(View.VISIBLE);

            btnSubirOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    final StorageReference ref = storageRef.child("offline/an" + Timestamp.now().getNanoseconds() + ".jpg");
                    UploadTask uploadTask = ref.putFile(Uri.parse(uri));

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isSuccessful()) ;
                            Uri uri1 = (Uri) task.getResult();

                            Log.e("img subida de Offline", String.valueOf(uri1));
                            txtOffline.setText(String.valueOf(uri1));


                        }
                    });


                }
            });
        }
    }

    public Bitmap getBipmap(Uri uri) {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void guardar(final Bitmap finalBitmap) {

        @SuppressLint("StaticFieldLeak") AsyncTask fileTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                // colocamos la ruta


                String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sdcard";
                File myDir = new File("/mnt/sdcard/AA");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String name = " " + n + ".jpg";
                File pictureFile = new File(myDir, name);
                try {
                    pictureFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream out = new FileOutputStream(pictureFile);
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    out.close();
                    Log.e("Enviado => ", "parece");
                    Log.e("Enviado => ", pictureFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        fileTask.execute();
    }

    private void escucharNumero() {

        model.getNumero().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (arreglo.size() > integer) {
                    subirImagen(Uri.parse(arreglo.get(integer)), integer);
                    prbPruebas.setVisibility(View.VISIBLE);
                } else {
                    prbPruebas.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void subirImagen(Uri uri, final Integer numero) {

        Log.e("Subiendo img", String.valueOf(uri));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final StorageReference ref = storageRef.child("online/anexodos" + numero + ".jpg");
        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) ;
                Uri uri1 = (Uri) task.getResult();
                Integer num = numero + 1;
                Log.e("img subida", String.valueOf(uri1));
                model.setNumero(num);

            }
        });
    }

    private void funBotones() {


        btnEscoger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(false)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(20)
                        .getIntent(Pruebas.this);

                startActivityForResult(intent, IMG);
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirImgOffline(Uri.parse(arreglo.get(0)));
            }
        });

    }

    private void subirImgOffline(Uri uri) {


        SharedPreferences sharedPref = getSharedPreferences(Constantes.DATOS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("imgs", String.valueOf(uri));
        editor.apply();

    }

    private void setWidget() {

        txtPrueba = findViewById(R.id.txt_prueba);
        txtOffline = findViewById(R.id.txt_prueba_offline);
        btnSubir = findViewById(R.id.btn_subir);
        btnEscoger = findViewById(R.id.btn_img_escoger);
        prbPruebas = findViewById(R.id.prb_prueba);
        btnGuardar = findViewById(R.id.btn_img_guardar);
        btnOffline = findViewById(R.id.btn_img_txt_offline);
        btnCamino = findViewById(R.id.btn_img_camino);
        btnTxt = findViewById(R.id.btn_img_txt);
        btnSubirOffline = findViewById(R.id.btn_img_txt_subir);
        btnNotificacion = findViewById(R.id.btn_img_txt_not);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMG) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos", String.valueOf(result.getUri()));

            arreglo.add(String.valueOf(uriResult));

            String text = "";
            for (int i = 0; i < arreglo.size(); i++) {
                text += arreglo.get(i) + "\n";
            }

            txtPrueba.setText(text);

        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_DIRECTORY) {


          /*  Log.e("Camino este ", String.valueOf(Environment.getExternalStorageDirectory()));
          //  camino = data.getData().getEncodedPath();
            String currentPath = data.getStringExtra(DirectoryPicker.BUNDLE_CHOSEN_DIRECTORY);
            //Do whatever you need to in this directory
            Log.e("Directory: =>",currentPath);*/

            Uri uri = data.getData();
            String src = uri.getPath();
            String caminoBueno = Environment.getExternalStorageState();

            Log.e("Directorio => ", src);
            Log.e("Directorio => ", caminoBueno);

        }
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
}
