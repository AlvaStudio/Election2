package com.alvastudio.election2.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alvastudio.election2.R;

public class LoadingErrorView {
    Dialog dialog;
    Button button;

    public void create(Context context, String message) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_error_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = dialog.findViewById(R.id.title);
        textView.setText(message);

        button = dialog.findViewById(R.id.button);

        dialog.show();
    }

    public void remove() {
        dialog.dismiss();
    }

    public Button getButton() {
        return button;
    }
}
