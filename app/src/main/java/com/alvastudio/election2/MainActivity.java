package com.alvastudio.election2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alvastudio.election2.Controllers.ItemsController;
import com.alvastudio.election2.Controllers.ItemsInterface;
import com.alvastudio.election2.Controllers.UserController;
import com.alvastudio.election2.Models.Item;
import com.alvastudio.election2.Models.User;
import com.alvastudio.election2.Views.ItemsAdapter;
import com.alvastudio.election2.Views.LoadingErrorView;
import com.alvastudio.election2.Views.LoadingView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemsInterface {

    ListView listView;
    ItemsAdapter itemsAdapter;
    LoadingView loadingView;
    LoadingErrorView loadingErrorView;
    TextView totalTextView;
    ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.item_list_view);
        listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);

        itemsAdapter = new ItemsAdapter(this);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onItemClick", "i = " + i);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("item", itemList.get(i));
                startActivity(intent);
            }
        });

        totalTextView = findViewById(R.id.text_total);
        itemList = new ArrayList<>();

        updateListView();
    }

    private void updateListView() {
        showLoading();
        ItemsController itemsController = new ItemsController(this);
        itemsController.loadItems(this);
    }

    private void showLoading() {
        loadingView = new LoadingView();
        loadingView.create(this, getResources().getString(R.string.loading_title));
    }

    private void removeLoading() {
        if (loadingView != null) {
            loadingView.remove();
            loadingView = null;
        }
    }

    private void showLoadingError(String error) {
        loadingErrorView = new LoadingErrorView();
        loadingErrorView.create(this, error);
        loadingErrorView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLoadingError();
            }
        });
    }

    private void removeLoadingError() {
        if (loadingErrorView != null) {
            loadingErrorView.remove();
            loadingErrorView = null;
        }
    }

    @Override
    public void updateItems(ArrayList<Item> arrayList, int total) {
        removeLoading();

        itemList = arrayList;

        totalTextView.setText(getResources().getString(R.string.total_title) + String.valueOf(total));

        itemsAdapter.setArrayList(itemList);
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateItemsError(String error) {
        removeLoading();
        showLoadingError(error);
    }

    @Override
    public void updateVote(int total) {
        // предварительгное обновление таблицы локально
        updateItems();
    }

    @Override
    public void updateVoteError(String error) {
        showLoadingError(error);
    }

    public void updateItems() {
        itemsAdapter.setArrayList(itemList);
        itemsAdapter.notifyDataSetChanged();
        // полное обновление таблицы с севрера
        updateListView();
    }

    public void selectItem(int position) {
        Item item = itemList.get(position);
        if (item.getSelected() == 1) {
            item.setSelected(0);
        } else {
            item.setSelected(1);
        }
        for (int i = 0; i < itemList.size(); i++) {
            if (i != position) {
                Item tItem = itemList.get(i);
                tItem.setSelected(0);
            }
        }

        User user = UserController.loadUser(this);

        ItemsController itemsController = new ItemsController(this);
        itemsController.sendVote(this, item.getItemId(), user.getVoteId());
    }
}