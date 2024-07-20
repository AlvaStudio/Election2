package com.alvastudio.election2.Controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.alvastudio.election2.Models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendVotePostRequestTask extends AsyncTask<Void, Void, String> {
    private static final String REQUEST_METHOD = "POST";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    private String requestUrl;
    private String requestBody;
    private ItemsInterface itemsInterface;
    private int voteId;
    private int lastVoteId;

    private Context context;

    public SendVotePostRequestTask(String requestUrl, String requestBody, ItemsInterface itemsInterface, int voteId, int lastVoteId, Context context) {
        this.requestUrl = requestUrl;
        this.requestBody = requestBody;
        this.itemsInterface = itemsInterface;
        this.voteId = voteId;
        this.lastVoteId = lastVoteId;
        this.context = context;
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
                itemsInterface.updateVoteError("Unexpected code " + responseCode);
            }
        } catch (IOException e) {
            itemsInterface.updateVoteError(e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // обрабатываем результат
        if (result != null) {
            // успешный ответ
            sendVoteComplete(result, itemsInterface, voteId, lastVoteId);
        } else {
            // ошибка
            itemsInterface.updateItemsError("Response error");
        }
    }

    private void sendVoteComplete(String response, ItemsInterface itemsInterface, int voteId, int lastVoteId) {
        // Проверяем что ответ не пустой и в нем одно слово
        boolean isOneWord = !response.isEmpty() && response.matches("\\b\\w+\\b");

        if (isOneWord) {
            try {
                int total = Integer.parseInt(response);
                // Возвращается одна цифра, которая означает количество голосов для выбранного кандидата

                User user = UserController.loadUser(context);
                user.setVoteId(voteId);
                user.setLastVoteId(lastVoteId);
                UserController.writeUser(user, context);

                // Слово может быть преобразовано в целое число
                itemsInterface.updateVote(total);
            } catch (NumberFormatException e) {
                // Слово не может быть преобразовано в целое число.
                itemsInterface.updateVoteError(e.toString());
            }
        } else {
            // "Строка пуста или содержит более одного слова."
            itemsInterface.updateVoteError("Response error");
        }
    }
}
