package com.alvastudio.election2.Controllers;

import android.os.AsyncTask;

import com.alvastudio.election2.Classes.Utils;
import com.alvastudio.election2.Models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemsPostRequestTask extends AsyncTask<Void, Void, String> {

    private static final String REQUEST_METHOD = "POST";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    private String requestUrl;
    private String requestBody;
    private ItemsInterface itemsInterface;

    public ItemsPostRequestTask(String requestUrl, String requestBody, ItemsInterface itemsInterface) {
        this.requestUrl = requestUrl;
        this.requestBody = requestBody;
        this.itemsInterface = itemsInterface;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // создаем URL-адрес
            URL url = new URL(requestUrl);

            // создаем соединение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // записываем тело запроса
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

            // выполняем запрос
            int responseCode = connection.getResponseCode();

            // проверяем код ответа
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // читаем тело ответа
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                inputStream.close();

                // возвращаем тело ответа
                return response.toString();
            } else {
                itemsInterface.updateItemsError("Unexpected code " + responseCode);
            }
        } catch (IOException e) {
            itemsInterface.updateItemsError(e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // обрабатываем результат
        if (result != null) {
            // успешный ответ
            itemsLoadingComplete(result, itemsInterface);
        } else {
            // ошибка
            itemsInterface.updateItemsError("Response error");
        }
    }

    private void itemsLoadingComplete(String response, ItemsInterface itemsInterface) {
        if (Utils.isJSONValid(response)) {
            ArrayList<Item> arrayList = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(jsonObject.getString("id")));
                    item.setFirstName(jsonObject.getString("firstname"));
                    item.setSecondName(jsonObject.getString("secondname"));
                    item.setThirdName(jsonObject.getString("thirdname"));
                    item.setParty(jsonObject.getString("party"));
                    item.setDesc(jsonObject.getString("description"));
                    item.setWeb(jsonObject.getString("web"));
                    item.setImage(jsonObject.getString("image"));
                    item.setVotes(Integer.parseInt(jsonObject.getString("votes")));
                    item.setSelected(Integer.parseInt(jsonObject.getString("selected")));

                    arrayList.add(item);
                }

                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                int total = Integer.parseInt(jsonObject.getString("total"));

                itemsInterface.updateItems(arrayList, total);

            } catch (JSONException error) {
                itemsInterface.updateItemsError(error.toString());
            }
        } else {
            itemsInterface.updateItemsError("Json invalid");
        }
    }
}
