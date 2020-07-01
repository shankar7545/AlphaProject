package com.example.alpha.Wallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Activity.SupportActivity;
import com.example.alpha.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DialogPaymentFragment extends DialogFragment {

    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.dialog_payment, container, false);
        root_view.findViewById(R.id.bt_close).setOnClickListener(v -> dismiss());
        (root_view.findViewById(R.id.lyt_add_card)).setOnClickListener(v -> startActivity(new Intent(getContext(), HelpActivity.class)));
        (root_view.findViewById(R.id.lyt_request)).setOnClickListener(v -> startActivity(new Intent(getContext(), SupportActivity.class)));
        (root_view.findViewById(R.id.lyt_link_account)).setOnClickListener(v -> startActivity(new Intent(getContext(), HelpActivity.class)));
        return root_view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode);
    }

}