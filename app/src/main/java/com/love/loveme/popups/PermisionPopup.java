package com.love.loveme.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.love.loveme.R;
import com.love.loveme.databinding.PopupPermissionBinding;

public class PermisionPopup {

    private final PopupPermissionBinding popupbinding;
    private final Dialog dialog;
    private PopupClickListnear popupClickListnear;

    public PermisionPopup(Context context) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        popupbinding = DataBindingUtil.inflate(inflater, R.layout.popup_permission, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(popupbinding.getRoot());
        dialog.show();
        popupbinding.btnallow.setOnClickListener(v -> {
            dialog.dismiss();
            popupClickListnear.onPositiveClick();
        });
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

    public interface PopupClickListnear {
        void onPositiveClick();

    }

}
