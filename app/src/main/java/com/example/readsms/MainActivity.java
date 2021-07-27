package com.example.readsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText e1,e2;

    TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }


        textViewResult = findViewById(R.id.textView);






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