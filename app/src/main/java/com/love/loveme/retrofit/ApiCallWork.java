package com.love.loveme.retrofit;

import android.content.Context;
import android.util.Log;

import com.love.loveme.SessionManager;
import com.love.loveme.models.RestResponseNumber;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallWork {

    private final String userTkn;
    OnResponseListnear onResponseListnear;
    SessionManager sessionManager;

    public ApiCallWork(Context context) {

        sessionManager = new SessionManager(context);
        userTkn = sessionManager.getUser().getToken();
    }

    public OnResponseListnear getOnResponseListnear() {
        return onResponseListnear;
    }

    public void setOnResponseListnear(OnResponseListnear onResponseListnear) {
        this.onResponseListnear = onResponseListnear;
    }

    public void lessCoin(String coin) {
        Call<RestResponseNumber> call = RetrofitBuilder.create().lessCoin(userTkn, Const.DEV_KEY, Integer.parseInt(coin));
        call.enqueue(new Callback<RestResponseNumber>() {
            @Override
            public void onResponse(Call<RestResponseNumber> call, Response<RestResponseNumber> response) {
                if(response.code() == 200) {
                    if (response.body().getStatus()) {
                        Log.d("TAG", "onResponse: lesscoin= " + coin);
                        onResponseListnear.onSuccess();
                    } else {
                        onResponseListnear.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponseNumber> call, Throwable t) {
//
            }
        });
    }

    public void addCoin(String userTkn, int coin) {
        Call<RestResponseNumber> call = RetrofitBuilder.create().addCoin(userTkn, Const.DEV_KEY, coin);
        call.enqueue(new Callback<RestResponseNumber>() {
            @Override
            public void onResponse(Call<RestResponseNumber> call, Response<RestResponseNumber> response) {
                if(response.code() == 200) {
                    if (response.body().getStatus()) {
                        Log.d("TAG", "onResponse: addcoin= " + coin);
                        onResponseListnear.onSuccess();
                    } else {
                        onResponseListnear.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponseNumber> call, Throwable t) {
//
            }
        });
    }

    public interface OnResponseListnear {
        void onSuccess();

        void onFailure();
    }

}
