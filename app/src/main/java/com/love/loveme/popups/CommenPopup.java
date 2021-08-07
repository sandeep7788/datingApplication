package com.love.loveme.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.love.loveme.R;
import com.love.loveme.databinding.PopupCommenBinding;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CommenPopup {
    Dialog dialog;
    PopupClickListnear popupClickListnear;
    PopupCommenBinding popupbinding;
    public CommenPopup(Context context, String title, String description, String positive, String negtive) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupbinding = DataBindingUtil.inflate(inflater, R.layout.popup_commen, null, false);

        popupbinding.tvTitle.setText(title);
        popupbinding.tvDes.setText(description);
        popupbinding.tvPositive.setText(positive);
        popupbinding.tvNagetive.setText(negtive);

        popupbinding.tvPositive.setOnClickListener(v -> popupClickListnear.onPositiveClick());
        popupbinding.tvNagetive.setOnClickListener(v -> popupClickListnear.onNegativeClick());
        popupbinding.btnClose.setOnClickListener(v -> popupClickListnear.onCloseClick());
        dialog.setContentView(popupbinding.getRoot());
        dialog.show();
        dialog.setCancelable(false);
        showCloseButton(false);

    }

    public PopupClickListnear getPopupClickListnear() {
        return popupClickListnear;
    }

    public void setPopupClickListnear(PopupClickListnear popupClickListnear) {
        this.popupClickListnear = popupClickListnear;
    }

    public void closePopup() {
        if(dialog != null) {
            dialog.dismiss();
        }
    }

    public void showCloseButton(boolean b) {
        if(b) {
            popupbinding.btnClose.setVisibility(View.VISIBLE);
        } else {
            popupbinding.btnClose.setVisibility(View.GONE);
        }
    }

    public interface PopupClickListnear {
        void onPositiveClick();

        void onNegativeClick();

        void onCloseClick();
    }
}
