package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataTable extends AppCompatActivity {
    final static String BASE_URL = "http://www.alarstudios.com/test/data.cgi";
    final static String TAG = "TAG_ERROR";

    private OkHttpClient client;
    RecyclerView dataRecyclerView;
    private DataAdapter dataAdapter;
    ArrayList<DataItem> dataItemArrayList;

    int currentPage = 1;
    int itemCount = 0;

    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);

        final String authorizationCode = getIntent().getStringExtra("code");
        client = new OkHttpClient();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dataRecyclerView = (RecyclerView) findViewById(R.id.dataRecyclerView);
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.setHasFixedSize(true);
        dataItemArrayList = new ArrayList<>();

        dataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (lastvisibleitemposition == dataAdapter.getItemCount() - 1) {
                    if (!isLastPage) {
                        currentPage++;
                        loadJSON(authorizationCode);
                    }
                }
            }
        });

        dataAdapter = new DataAdapter(dataItemArrayList,DataTable.this);
        dataRecyclerView.setAdapter(dataAdapter);

        loadJSON(authorizationCode);
    }

    private void loadJSON(String authorizationCode)  {
        HttpUrl.Builder urlBuilder= HttpUrl.parse(BASE_URL + "?").newBuilder();

        urlBuilder.addQueryParameter("code", authorizationCode);
        urlBuilder.addQueryParameter("p", String.valueOf(currentPage));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            public void onResponse(Call call, Response response)
                    throws IOException {

                String jsonData = response.body().string();
                if (jsonData.length()==0){
                    isLastPage=true;
                    return;
                }
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                } catch (JSONException e) {
                    Log.d(TAG,"Jobject is null");
                    return;
                }
               loadDataItem(Jobject);
            }

            public void onFailure(Call call, IOException e) {
            }
        });
    }

    private void loadDataItem(JSONObject Jobject){
        JSONArray jsonArray=null;

        try {
            try {jsonArray = Jobject.getJSONArray("data");}
            catch (NullPointerException e){
                Log.d(TAG,"jsonArray is null");
            }

            for (int i=0;i< jsonArray.length();i++){
                itemCount++;
                DataItem dataItem = new DataItem();
                JSONObject userData=jsonArray.getJSONObject(i);
                dataItem.setId(userData.getString("id"));
                dataItem.setName(userData.getString("name"));
                dataItem.setCountry(userData.getString("country"));
                dataItem.setLat(userData.getString("lat"));
                dataItem.setLon(userData.getString("lon"));
                dataItemArrayList.add(dataItem);
            }

        } catch (JSONException e) {
            Log.d(TAG,"jsonDataItem error");
            e.printStackTrace();
        }

        showData();
    }

    private void showData() {
        runOnUiThread(new Runnable() {
            public void run() {
                dataAdapter.notifyDataSetChanged();
            }
        });
    }
}