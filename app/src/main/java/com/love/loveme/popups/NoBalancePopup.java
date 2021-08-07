package com.love.loveme.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.love.loveme.R;
import com.love.loveme.adapters.CoinPlanAdapter;
import com.love.loveme.databinding.PopupNobalanceBinding;
import com.love.loveme.models.CoinPackageRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class NoBalancePopup {
    Dialog dialog;
    PopupClickListnear popupClickListnear;

    public NoBalancePopup(Context context, String balance) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        PopupNobalanceBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.popup_nobalance, null, false);

        popupbinding.tvbalance.setText(String.valueOf(balance));


        Call<CoinPackageRoot> call1 = RetrofitBuilder.create().getCoinPackages(Const.DEV_KEY);
        call1.enqueue(new Callback<CoinPackageRoot>() {
            @Override
            public void onResponse(Call<CoinPackageRoot> call, Response<CoinPackageRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && !response.body().getData().isEmpty()) {
                    CoinPlanAdapter coinPlanAdapter = new CoinPlanAdapter(response.body().getData(), amount -> popupClickListnear.onPlanItemClick(amount));
                    popupbinding.rvplans.setAdapter(coinPlanAdapter);


                }
            }

            @Override
            public void onFailure(Call<CoinPackageRoot> call, Throwable t) {
//
            }
        });


        dialog.setContentView(popupbinding.getRoot());
        dialog.setOnDismissListener(dialog -> popupClickListnear.onDismiss());
        dialog.show();
        popupbinding.btnClose.setOnClickListener(v -> dialog.dismiss());

    }

    public PopupClickListnear getPopupClickListnear() {
        return popupClickListnear;
    }

    public void setPopupClickListnear(PopupClickListnear popupClickListnear) {
        this.popupClickListnear = popupClickListnear;
    }

    public void closePopup() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface PopupClickListnear {
        void onPlanItemClick(CoinPackageRoot.DataItem value);

        void onDismiss();

    }
}
