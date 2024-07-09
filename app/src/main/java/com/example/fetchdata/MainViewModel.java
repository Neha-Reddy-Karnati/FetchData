package com.example.fetchdata;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>();

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public void fetchItems() {
        new FetchDataTask().execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchDataTask extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected List<Item> doInBackground(String... urls) {
            List<Item> itemList = new ArrayList<>();
            try {
                JSONArray jsonArray = getJsonArray(urls);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // Check if "name" exists and is not empty
                    if (!jsonObject.isNull("name")) {
                        String name = jsonObject.getString("name").trim(); // Trim to handle whitespace
                        if (!name.isEmpty()) {
                            Item item = new Item();
                            item.setId(jsonObject.getInt("id"));
                            item.setListId(jsonObject.getInt("listId"));
                            item.setName(name);
                            itemList.add(item);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Sort items by listId and then by name
            return itemList.stream()
                    .sorted(Comparator.comparingInt(Item::getListId)
                            .thenComparing(Item::getName))
                    .collect(Collectors.toList());
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            items.postValue(result);
        }
    }



    private static JSONArray getJsonArray(String[] urls) throws IOException, JSONException {
        URL url = new URL(urls[0]);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();
        return new JSONArray(json.toString());
    }
}
