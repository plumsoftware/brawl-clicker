package ru.plumsoftware.brawlstarsclicker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.InitializationListener;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;

import ru.plumsoftware.brawlstarsclicker.R;
import ru.plumsoftware.brawlstarsclicker.data.Data;
import ru.plumsoftware.brawlstarsclicker.dialogs.CustomProgressDialog;

public class MainActivity extends AppCompatActivity {
    private long score;
    private int click;
    private int mul = 1;
    private int imageResId;
    private SharedPreferences sharedPreferences;
    private RewardedAd rewardedAd;
    private AdRequest adRequest;
    private CustomProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        region::Hide ui views
//        Скрыть status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Скрыть bottom navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//        endregion

//        region::Base variables
        Context context = MainActivity.this;
        Activity activity = MainActivity.this;

        progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage("Загрузка...");

        MobileAds.initialize(this, new InitializationListener() {
            @Override
            public void onInitializationCompleted() {

            }
        });
//        endregion

//        region::Find views
        ImageView ads = (ImageView) findViewById(R.id.ads);
        ImageView buy = (ImageView) findViewById(R.id.buy);
        ImageView image = (ImageView) findViewById(R.id.image);
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        CardView cardShop = (CardView) findViewById(R.id.cardShop);
        CardView cardAds = (CardView) findViewById(R.id.cardAds);
//        endregion

//        region::Animations
        Animation pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse);
//        Animation zoomInAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
//        Animation zoomOutAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
//        endregion

//        region::Get data
        sharedPreferences = context.getSharedPreferences(Data.SP_NAME, Context.MODE_PRIVATE);
        score = sharedPreferences.getLong(Data.SP_SCORE, 0);
        click = sharedPreferences.getInt(Data.SP_CLICK, 1);
        imageResId = sharedPreferences.getInt(Data.SP_IMAGE_RES_ID, R.drawable.spike_1);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Data.SP_HEROES_IS_BUY[0], true);
        edit.apply();
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                textViewScore.setText(Long.toString(sharedPreferences.getLong(Data.SP_SCORE, 0)));
                image.setImageResource(sharedPreferences.getInt(Data.SP_IMAGE_RES_ID, R.drawable.spike_1));
                click = sharedPreferences.getInt(Data.SP_CLICK, 1);
            }
        });
//        endregion

//        region::Start animations
        ads.startAnimation(pulseAnimation);
//        endregion

//        region::Setup data
        textViewScore.setText(Long.toString(score));
        image.setImageResource(imageResId);
//        endregion

//        region::Clickers
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = score + (long) click * mul;
                textViewScore.setText(Long.toString(score));

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(Data.SP_SCORE, score);
                editor.putInt(Data.SP_CLICK, click);
                editor.putInt(Data.SP_IMAGE_RES_ID, imageResId);
                editor.apply();
            }
        });

        image.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Уменьшаем размер view
                        v.setScaleX(0.8f);
                        v.setScaleY(0.8f);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Возвращаем исходный размер view
                        v.setScaleX(1.0f);
                        v.setScaleY(1.0f);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                }
                return false;
            }
        });

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.showProgressDialog();
                adRequest = new AdRequest.Builder().build();
                rewardedAd = new RewardedAd(MainActivity.this);
                rewardedAd.setAdUnitId("R-M-2428506-2");
                rewardedAd.setRewardedAdEventListener(new RewardedAdEventListener() {
                    @Override
                    public void onAdLoaded() {
                        progressDialog.dismissProgressDialog();
                        rewardedAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                        progressDialog.dismissProgressDialog();
                        Toast.makeText(activity, "Не удалось загрузить реакламу:(\nПопробуйте позже", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdShown() {
                        progressDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onAdDismissed() {
                        progressDialog.dismissProgressDialog();
//                        Toast.makeText(activity, "Вы закрыли рекламу раньшн времени:(\nВы не получите награду", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRewarded(@NonNull Reward reward) {
                        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long l) {
                                mul = 5;
                            }

                            @Override
                            public void onFinish() {
                                mul = 1;
                            }
                        };
                        countDownTimer.start();
                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onLeftApplication() {

                    }

                    @Override
                    public void onReturnedToApplication() {

                    }

                    @Override
                    public void onImpression(@Nullable ImpressionData impressionData) {

                    }
                });
                rewardedAd.loadAd(adRequest);
            }
        });

        ads.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Уменьшаем размер view
                        cardAds.setScaleX(0.9f);
                        cardAds.setScaleY(0.9f);
                        cardAds.setCardBackgroundColor(context.getResources().getColor(R.color.color_2));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Возвращаем исходный размер view
                        cardAds.setScaleX(1.0f);
                        cardAds.setScaleY(1.0f);
                        cardAds.setCardBackgroundColor(context.getResources().getColor(R.color.color_4));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                }
                return false;
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(new Intent(context, ShopActivity.class));
            }
        });

        buy.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Уменьшаем размер view
                        cardShop.setScaleX(0.9f);
                        cardShop.setScaleY(0.9f);
                        cardShop.setCardBackgroundColor(context.getResources().getColor(R.color.color_2));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Возвращаем исходный размер view
                        cardShop.setScaleX(1.0f);
                        cardShop.setScaleY(1.0f);
                        cardShop.setCardBackgroundColor(context.getResources().getColor(R.color.color_4));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                }
                return false;
            }
        });
//        endregion
    }
}