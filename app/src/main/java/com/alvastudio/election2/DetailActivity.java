package com.alvastudio.election2;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alvastudio.election2.Classes.Constants;
import com.alvastudio.election2.Controllers.ImageDownloadTask;
import com.alvastudio.election2.Models.Item;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Item item = (Item) getIntent().getSerializableExtra("item");

        TextView secondName = findViewById(R.id.item_second_name);
        TextView name = findViewById(R.id.item_name);
        ImageView imageView = findViewById(R.id.item_image);
        TextView party = findViewById(R.id.item_party);
        TextView desc = findViewById(R.id.item_desc);

        secondName.setText(item.getSecondName());
        name.setText(item.getFirstName() + " " + item.getThirdName());
        party.setText(item.getParty());
        desc.setText(item.getDesc());

        new ImageDownloadTask(imageView).execute(Constants.HOST_IMAGES + item.getImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // обработка нажатия на кнопку "Назад"
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}