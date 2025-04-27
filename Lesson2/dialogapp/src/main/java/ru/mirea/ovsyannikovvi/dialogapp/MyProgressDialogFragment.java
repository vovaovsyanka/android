package ru.mirea.ovsyannikovvi.dialogapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    @Override
    public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Загрузка...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        // Имитация длительной операции
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Закрываем диалог после завершения "загрузки"
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    // Показываем Snackbar или Toast с сообщением о завершении
                    ((MainActivity) getActivity()).showSnackbar("Загрузка завершена!");
                }
            }
        }, 5000); // Задержка в 5 секунд

        return progressDialog;
    }
}