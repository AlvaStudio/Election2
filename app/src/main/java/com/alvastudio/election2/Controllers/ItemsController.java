package com.alvastudio.election2.Controllers;

import android.content.Context;
import android.provider.SyncStateContract;

import androidx.annotation.Nullable;

import com.alvastudio.election2.Classes.Constants;
import com.alvastudio.election2.Classes.FileUtils;
import com.alvastudio.election2.Classes.Utils;
import com.alvastudio.election2.Models.Item;
import com.alvastudio.election2.Models.User;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemsController {
    ArrayList<Item> arrayList;
    Context context;

    public ItemsController(Context context) {
        this.context = context;
    }

    public void loadItems(ItemsInterface itemsInterface) {
        arrayList = new ArrayList<>();

        String url = Constants.HOST + Constants.API_COMMAND_LIST;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemsLoadingComplete(response, itemsInterface);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        itemsInterface.updateItemsError(error.toString());
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = UserController.loadUser(context);
                Map<String, String> params = new HashMap<>();
                params.put(Constants.API_PARAM_DEVICE_ID, user.getUserId());
                params.put(Constants.API_PARAM_DEVICE_NAME, user.getUserName());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(request);
    }

    private void itemsLoadingComplete(String response, ItemsInterface itemsInterface) {
        if (Utils.isJSONValid(response)) {
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

    public void sendVote(ItemsInterface itemsInterface, int voteId, int lastVoteId) {
        String url = Constants.HOST + Constants.API_COMMAND_SEND_VOTE;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendVoteComplete(response, itemsInterface, voteId, lastVoteId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        itemsInterface.updateVoteError(error.toString());
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = UserController.loadUser(context);
                Map<String, String> params = new HashMap<>();
                params.put(Constants.API_PARAM_DEVICE_ID, user.getUserId());
                params.put(Constants.API_PARAM_DEVICE_NAME, user.getUserName());
                params.put(Constants.API_PARAM_VOTE_ID, String.valueOf(voteId));
                params.put(Constants.API_PARAM_LAST_VOTE_ID, String.valueOf(lastVoteId));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(request);
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
