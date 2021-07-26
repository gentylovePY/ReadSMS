package com.example.readsms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText e1,e2;
    String  name,hobby;
    TextView tres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }

        e1 = findViewById(R.id.editTextTextEmailAddress);
        e2 = findViewById(R.id.editTextTextPassword);
        tres = findViewById(R.id.textView);




//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    init();
//
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        };
//        Thread secThread = new Thread(runnable);
//        secThread.start();
//
//        SharedPreferences prefs = getSharedPreferences("json", MODE_PRIVATE);
//        String name = prefs.getString("json", null);
//        textview.setText(name);


    }


//    private void init() throws IOException, JSONException {
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("email", "sha-sabir@yandex.ru");
//        params.put("password", "sabirmipt");
//        params.put("installationId", "sabir_test_device4");
//        JSONObject parameter = new JSONObject(params);
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, parameter.toString());
//
//
//        Request request = new Request.Builder()
//                .url("https://.online/api/1/login")
//                .addHeader("Content-Type", "application/json")
//                .post(body)
//                .build();
//
//
//        Response response = client.newCall(request).execute();//response
//        String jsonData = response.body().string();//respons in string
//        JSONObject Jobject = new JSONObject(jsonData); //object json
//        JSONArray Jarray = Jobject.getJSONArray("user"); //get json from array
//
//        for (int i = 0; i < Jarray.length(); i++) {
//            JSONObject object = Jarray.getJSONObject(i);
//
//
//
//            SharedPreferences.Editor editor = getSharedPreferences("json", MODE_PRIVATE).edit();
//            editor.putString("json", object.toString());
//
//            editor.apply();
//        }
//
//
//       // Log.e("post", str_value.toString());
//
//
//    }


    public void Post(View view) {
        name = e1.getText().toString();
        hobby = e2.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://iproxy.online/api/1/login")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistRetrofit registRetrofit = retrofit.create(RegistRetrofit.class);
        RegistrSet registrSet = new RegistrSet(name,hobby);
       Call<RegistrSet> call = registRetrofit.PosrData(registrSet);

       call.enqueue(new Callback<RegistrSet>() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onResponse(Call<RegistrSet> call, Response<RegistrSet> response) {
               tres.setText(response.body().getJson().getName()+response.body().getJson().getHobby());

           }

           @Override
           public void onFailure(Call<RegistrSet> call, Throwable t) {
               tres.setText("");

           }
       });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "not ok", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}