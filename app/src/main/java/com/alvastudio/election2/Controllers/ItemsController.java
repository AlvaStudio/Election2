package com.alvastudio.election2.Controllers;

import android.content.Context;
import com.alvastudio.election2.Classes.Constants;
import com.alvastudio.election2.Models.User;

public class ItemsController {
    Context context;

    public ItemsController(Context context) {
        this.context = context;
    }

    public void loadItems(ItemsInterface itemsInterface) {
        User user = UserController.loadUser(context);
        String requestUrl = Constants.HOST + Constants.API_COMMAND_LIST;
        String requestBody = Constants.API_PARAM_DEVICE_ID + "=" + user.getUserId() + "&" + Constants.API_PARAM_DEVICE_NAME + "=" + user.getUserName();

        ItemsPostRequestTask itemsPostRequestTask = new ItemsPostRequestTask(requestUrl, requestBody, itemsInterface);
        itemsPostRequestTask.execute();
    }

    public void sendVote(ItemsInterface itemsInterface, int voteId, int lastVoteId) {
        User user = UserController.loadUser(context);
        String requestUrl = Constants.HOST + Constants.API_COMMAND_SEND_VOTE;
        String requestBody = Constants.API_PARAM_DEVICE_ID + "=" + user.getUserId() + "&" + Constants.API_PARAM_DEVICE_NAME + "=" + user.getUserName() +
                "&" + Constants.API_PARAM_VOTE_ID + "=" + String.valueOf(voteId) + "%" + Constants.API_PARAM_LAST_VOTE_ID + "=" + String.valueOf(lastVoteId);

        SendVotePostRequestTask sendVotePostRequestTask = new SendVotePostRequestTask(requestUrl, requestBody, itemsInterface, voteId, lastVoteId, context);
        sendVotePostRequestTask.execute();
    }

}


