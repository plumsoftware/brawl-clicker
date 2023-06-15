package ru.plumsoftware.brawlstarsclicker.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.plumsoftware.brawlstarsclicker.R;

public class CustomProgressDialog extends Dialog {

    private ProgressBar progressBar;
    private TextView progressText;

    public CustomProgressDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.custom_progress_dialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setCancelable(false);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
    }

    public void setMessage(String message) {
        progressText.setText(message);
    }

    public void showProgressDialog() {
        show();
    }

    public void dismissProgressDialog() {
        dismiss();
    }

}
