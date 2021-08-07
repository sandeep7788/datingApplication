package com.love.loveme.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.love.loveme.R;
import com.love.loveme.databinding.PopupCallrejectBinding;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CallRejectPopup {
    private final Dialog dialog;
    PopupClickListnear popupClickListnear;

    public CallRejectPopup(Context context, String msg) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        PopupCallrejectBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.popup_callreject, null, false);

        popupbinding.tvReview.setText(msg);
        popupbinding.tvcontinue.setOnClickListener(v -> popupClickListnear.onContinueClick());
        popupbinding.tvPositive.setOnClickListener(v -> popupClickListnear.onPositiveClik());
        dialog.setContentView(popupbinding.getRoot());
        dialog.show();


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
        void onContinueClick();

        void onPositiveClik();


    }
}
