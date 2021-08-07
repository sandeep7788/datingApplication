package com.love.loveme.retrofit;

import com.love.loveme.models.BrandingImageRoot;
import com.love.loveme.models.CoinPackageRoot;
import com.love.loveme.models.CommentRoot;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.CountryVideoListRoot;
import com.love.loveme.models.GiftCategoryRoot;
import com.love.loveme.models.GiftRoot;
import com.love.loveme.models.MessageRoot;
import com.love.loveme.models.MessageUserRoot;
import com.love.loveme.models.OfferCoinPackageRoot;
import com.love.loveme.models.RandomVideoRoot;
import com.love.loveme.models.RestResponseNumber;
import com.love.loveme.models.RestResponseRoot;
import com.love.loveme.models.SettingRoot;
import com.love.loveme.models.UserRoot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitService {

    @GET("Settings/setting")
    Call<SettingRoot> getSettings(@Header("Unique-Key") String appkey);

    @FormUrlEncoded
    @POST("User/registration")
    Call<UserRoot> registerUser(@Header("Unique-Key") String appkey,
                                @Field("device_token") String devicetkn,
                                @Field("user_email") String email,
                                @Field("full_name") String name,
                                @Field("login_type") String logintype,
                                @Field("user_name") String username,
                                @Field("identity") String identity,
                                @Field("device_type") String devicetype);

    @GET("User/logout")
    Call<RestResponseRoot> logoutUser(@Header("Secret-Key") String token,
                                      @Header("Unique-Key") String appkey);

    @GET("User/user_details")
    Call<UserRoot> getUserDetail(@Header("Secret-Key") String token,
                                 @Header("Unique-Key") String appkey);

    @POST("Country/country_list")
    Call<CountryRoot> getCountryList(@Header("Unique-Key") String appkey);

    @FormUrlEncoded
    @POST("Country/video_by_countryid")
    Call<CountryVideoListRoot> getGirlsList(@Header("Unique-Key") String appkey,
                                            @Field("country_id") String cid,
                                            @Field("start") int start,
                                            @Field("count") int count);

    @FormUrlEncoded
    @POST("Country/comment_by_countryid")
    Call<CommentRoot> getComments(@Header("Unique-Key") String appkey,
                                  @Field("country_id") String cid,
                                  @Field("start") int start,
                                  @Field("count") int count);


    @POST("Gift/gift_category_list")
    Call<GiftCategoryRoot> getGiftCategory(@Header("Unique-Key") String appkey);

    @FormUrlEncoded
    @POST("Gift/gifts_by_catid")
    Call<GiftRoot> getGifts(@Header("Unique-Key") String appkey,
                            @Field("category_id") String cid,
                            @Field("start") int start,
                            @Field("count") int count);


    @POST("Country/random_video")
    Call<RandomVideoRoot> getRandomVideo(@Header("Unique-Key") String appkey);

    @GET("CoinPackage/branding_image_list")
    Call<BrandingImageRoot> getBrandingImages(@Header("Unique-Key") String appkey);

    @GET("CoinPackage/coin_packge_list")
    Call<CoinPackageRoot> getCoinPackages(@Header("Unique-Key") String appkey);

    @GET("CoinPackage/offer_coin_packge_list")
    Call<OfferCoinPackageRoot> getOfferCoinsPakages(@Header("Unique-Key") String appkey);


    @FormUrlEncoded
    @POST("User/add_coin")
    Call<RestResponseNumber> addCoin(@Header("Secret-Key") String token,
                                     @Header("Unique-Key") String appkey,
                                     @Field("coin") int coin);

    @FormUrlEncoded
    @POST("User/remove_coin")
    Call<RestResponseNumber> lessCoin(@Header("Secret-Key") String token,
                                      @Header("Unique-Key") String appkey,
                                      @Field("coin") int coin);

    @GET("ChatProfile/chat_profile_list")
    Call<MessageUserRoot> getMessageUserList(@Header("Unique-Key") String appkey);

    @GET("ChatProfile/chat_messages_list")
    Call<MessageRoot> getMessageList(@Header("Unique-Key") String appkey);


}
