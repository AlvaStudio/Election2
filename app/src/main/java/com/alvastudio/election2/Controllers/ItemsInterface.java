package com.alvastudio.election2.Controllers;

import com.alvastudio.election2.Models.Item;

import java.util.ArrayList;

public interface ItemsInterface {
    void updateItems(ArrayList<Item> arrayList, int total);
    void updateItemsError(String error);

    void updateVote(int total);
    void updateVoteError(String error);

}
