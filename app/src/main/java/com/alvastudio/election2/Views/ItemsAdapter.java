package com.alvastudio.election2.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvastudio.election2.Classes.Constants;
import com.alvastudio.election2.MainActivity;
import com.alvastudio.election2.Models.Item;
import com.alvastudio.election2.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ItemsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Item> arrayList;

    static class ViewHolder {
        TextView itemId;
        TextView name;
        TextView secondName;
        TextView party;
        ImageView image;
        TextView votes;
        ImageButton selected;
    }

    public ItemsAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = new ArrayList<>();
    }

    public void setArrayList(ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        Item item = (Item)getItem(i);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_row, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.itemId = view.findViewById(R.id.item_id);
            viewHolder.secondName = view.findViewById(R.id.item_second_name);
            viewHolder.name = view.findViewById(R.id.item_name);
            viewHolder.party = view.findViewById(R.id.item_party);
            viewHolder.image = view.findViewById(R.id.item_image);
            viewHolder.votes = view.findViewById(R.id.item_votes);
            viewHolder.selected = view.findViewById(R.id.item_select);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSelect(i);
            }
        });
        if (item.getSelected() == 1) {
            viewHolder.selected.setImageResource(R.drawable.check_box_on);
        } else {
            viewHolder.selected.setImageResource(R.drawable.check_box_off);
        }

        viewHolder.itemId.setText("ID: " + String.valueOf(item.getItemId()));
        viewHolder.secondName.setText(item.getSecondName());
        viewHolder.name.setText(item.getFirstName() + " " + item.getThirdName());
        viewHolder.party.setText(item.getParty());
        viewHolder.votes.setText(String.valueOf(item.getVotes()));

        Glide.with(viewGroup.getContext())
                .load(Constants.HOST_IMAGES + item.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.default_img)
                .into(viewHolder.image);

        return view;
    }

    private void actionSelect(int position) {
        ((MainActivity) context).selectItem(position);
    }
}
