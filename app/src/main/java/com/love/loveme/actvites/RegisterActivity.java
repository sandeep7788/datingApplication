package com.love.loveme.actvites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.love.loveme.R;
import com.love.loveme.SessionManager;
import com.love.loveme.databinding.ActivityRegisterBinding;
import com.love.loveme.models.UserRoot;
import com.love.loveme.retrofit.Const;
import com.love.loveme.retrofit.RetrofitBuilder;
import com.love.loveme.utils.ads.MyInterstitialAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    ActivityRegisterBinding binding;
    FirebaseUser currentUser;
    private SessionManager sessionManager;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private MyInterstitialAds myInterstitialAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mCallbackManager = CallbackManager.Factory.create();

        myInterstitialAds = new MyInterstitialAds(this);
    }

    @Override
    public void onBackPressed() {
        myInterstitialAds.showAds();
        super.onBackPressed();
    }

    public void onClickGoogle(View view) {

        Log.d("TAG", "onClickGoogle: " + view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.pd.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            binding.pd.setVisibility(View.GONE);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    String notificationToken = sessionManager.getStringValue(Const.NOTIFICATION_TOKEN);
                    if (account.getPhotoUrl() != null && !account.getPhotoUrl().toString().isEmpty()) {
                        sessionManager.saveStringValue(Const.PROFILE_IMAGE, account.getPhotoUrl().toString());
                    } else {
                        sessionManager.saveStringValue(Const.PROFILE_IMAGE, "");
                    }
                    Call<UserRoot> call = RetrofitBuilder.create().registerUser(Const.DEV_KEY, notificationToken, account.getEmail(),
                            account.getDisplayName(), "google", account.getEmail(), account.getEmail(), "android");
                    registerUser(call);


                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.getIdToken());


                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void registerUser(Call<UserRoot> call) {

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(@Nullable Call<UserRoot> call, @Nullable Response<UserRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null) {
                    sessionManager.saveUser(response.body().getData());
                    sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                    if (response.body() != null) {
                        sessionManager.saveStringValue(Const.USER_TOKEN, response.body().getData().getToken());

                    }

                    Log.d("TAG", "onResponse: usercreted success");
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }


                binding.pd.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(@Nullable Call<UserRoot> call, @Nullable Throwable t) {
                Log.d("TAG", "faill " + t);
            }
        });
    }


    /*public void onClickFacebok(View view) {
        Log.d("TAG", "onClickFacebok: " + view);


        LoginButton loginButton = binding.loginButton;
        loginButton.setPermissions("email", "public_profile");
        binding.lytfacebook.setOnClickListener(v -> loginButton.performClick());
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            private void handleFacebookAccessToken(AccessToken token) {
                Log.d("TAG", "handleFacebookAccessToken:" + token);

                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user.getPhotoUrl() != null && !user.getPhotoUrl().toString().isEmpty()) {
                                    sessionManager.saveStringValue(Const.PROFILE_IMAGE, user.getPhotoUrl().toString());
                                } else {
                                    sessionManager.saveStringValue(Const.PROFILE_IMAGE, "");
                                }
                                String notificationToken = sessionManager.getStringValue(Const.NOTIFICATION_TOKEN);
                                Call<UserRoot> call = RetrofitBuilder.create().registerUser(Const.DEV_KEY, notificationToken, user.getEmail(),
                                        user.getDisplayName(), "facebook", user.getEmail(), user.getEmail(), "android");
                                registerUser(call);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }


                        });
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });

    }*/

    public void onclickAggrement(View view) {
        Log.d("TAG", "onclickAggrement: " + view);
        startActivity(new Intent(this, WebActivity.class).putExtra("URL", Const.ABOUTUS).putExtra("TITLE", "Tearms of Use"));

    }

    public void onclickPrivacy(View view) {
        Log.d("TAG", "onclickPrivacy: " + view);
        startActivity(new Intent(this, WebActivity.class).putExtra("URL", Const.ABOUTUS).putExtra("TITLE", "Tearms of Use"));

    }
}