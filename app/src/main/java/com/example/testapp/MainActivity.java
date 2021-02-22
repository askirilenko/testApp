package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    private EditText editTextUserName;
    private EditText editTextPassword;
    final static String BASE_URL = "http://www.alarstudios.com/test/auth.cgi";

    private OkHttpClient client;
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        TextView buttonConnect = (TextView) findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusRequest();
            }
        });

        client = new OkHttpClient();

    }

    public void statusRequest(){
        HttpUrl.Builder urlBuilder= HttpUrl.parse(BASE_URL + "?").newBuilder();

        urlBuilder.addQueryParameter("username", editTextUserName.getText().toString());
        urlBuilder.addQueryParameter("password", editTextPassword.getText().toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String authorizationStatus = Jobject.get("status").toString();
                    String authorizationCode = Jobject.get("code").toString();
                    if (checkStatus(authorizationStatus)) switchToDataTable(authorizationCode);
                    else showErrorToast();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    private boolean checkStatus(String authorizationStatus){
        return authorizationStatus.equals("ok");
    }

    private void showErrorToast(){
        runOnUiThread(new Runnable() {
            public void run() {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Имя пользователя или пароль введены не верно!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void switchToDataTable(String authorizationCode){
        Intent intent = new Intent(this, DataTable.class);
        intent.putExtra("code", authorizationCode);
        startActivity(intent);
    }
}