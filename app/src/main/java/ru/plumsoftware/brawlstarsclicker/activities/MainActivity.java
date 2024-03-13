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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mobile.ads.appopenad.AppOpenAd;
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener;
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener;
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader;
import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.InitializationListener;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAd;
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener;
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener;
import com.yandex.mobile.ads.rewarded.RewardedAdLoader;

import ru.plumsoftware.brawlstarsclicker.R;
import ru.plumsoftware.brawlstarsclicker.data.Data;
import ru.plumsoftware.brawlstarsclicker.dialogs.CustomProgressDialog;

public class MainActivity extends AppCompatActivity {
    private long score;
    private int click;
    private int mul = 1;
    private int imageResId;
    private SharedPreferences sharedPreferences;
    @Nullable
    private RewardedAd mRewardedAd = null;
    @Nullable
    private RewardedAdLoader mRewardedAdLoader = null;
    private AdRequest adRequest;
    private CustomProgressDialog progressDialog;
    private AppOpenAdLoader appOpenAdLoader = null;
    private final String AD_UNIT_ID = "R-M-2428506-3";
    private final AdRequestConfiguration adRequestConfiguration = new AdRequestConfiguration.Builder(AD_UNIT_ID).build();
    private AppOpenAd mAppOpenAd = null;


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

        progressDialog = new CustomProgressDialog(context);
        progressDialog.setMessage("Загрузка...");
        progressDialog.showProgressDialog();

        MobileAds.initialize(this, () -> {

        });

        if (getIntent().getBooleanExtra("soa", true)) {
            appOpenAdLoader = new AppOpenAdLoader(context);
            AppOpenAdLoadListener appOpenAdLoadListener = new AppOpenAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull final AppOpenAd appOpenAd) {
                    // The ad was loaded successfully. Now you can show loaded ad.
                    mAppOpenAd = appOpenAd;
                    mAppOpenAd.show(activity);
                    progressDialog.dismissProgressDialog();
                }

                @Override
                public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                    progressDialog.dismissProgressDialog();
                }
            };
            appOpenAdLoader.setAdLoadListener(appOpenAdLoadListener);
            appOpenAdLoader.loadAd(adRequestConfiguration);

            AppOpenAdEventListener appOpenAdEventListener = new AppOpenAdEventListener() {
                @Override
                public void onAdShown() {
                    // Called when ad is shown.
                }

                @Override
                public void onAdFailedToShow(@NonNull final AdError adError) {
                    // Called when ad failed to show.
                }

                @Override
                public void onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after dismiss and preload new ad.
                }

                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdImpression(@Nullable final ImpressionData impressionData) {
                    // Called when an impression is recorded for an ad.
                }
            };

            if (mAppOpenAd != null) {
                mAppOpenAd.setAdEventListener(appOpenAdEventListener);
            }

            if (mAppOpenAd != null) {
                mAppOpenAd.show(activity);
            }
        } else {
            progressDialog.dismissProgressDialog();
        }
//        endregion

//        region::Find views
        ImageView ads = (ImageView) findViewById(R.id.ads);
        ImageView buy = (ImageView) findViewById(R.id.buy);
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView buyBack = (ImageView) findViewById(R.id.buy_back);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        TextView textViewNew = (TextView) findViewById(R.id.textViewNew);
        CardView cardShop = (CardView) findViewById(R.id.cardShop);
        CardView cardAds = (CardView) findViewById(R.id.cardAds);
        CardView cardBackgroundShop = (CardView) findViewById(R.id.cardShopBack);
//        endregion

//        region::Animations
        Animation pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse);
//        Animation zoomInAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
//        Animation zoomOutAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
//        endregion

//        region::Get data
        sharedPreferences = context.getSharedPreferences(Data.SP_NAME, Context.MODE_PRIVATE);


        layout.setBackgroundResource(sharedPreferences.getInt(Data.SP_IMAGE_BACK_RES_ID, R.drawable.back_2));

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
        if (
                sharedPreferences.getBoolean("11.03.2024_1", true) ||
                sharedPreferences.getBoolean("11.03.2024_2", true) ||
                sharedPreferences.getBoolean("13.03.2024_1", true)
        )
            textViewNew.startAnimation(pulseAnimation);
        else
            textViewNew.setVisibility(View.GONE);
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

        ads.setOnClickListener(view -> {
            progressDialog.showProgressDialog();
            adRequest = new AdRequest.Builder().build();
            mRewardedAdLoader = new RewardedAdLoader(MainActivity.this);

            mRewardedAdLoader.setAdLoadListener(new RewardedAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull final RewardedAd rewardedAd) {
                    mRewardedAd = rewardedAd;
                    progressDialog.dismissProgressDialog();
                    rewardedAd.show(MainActivity.this);
                    mRewardedAd.setAdEventListener(new RewardedAdEventListener() {
                        @Override
                        public void onAdShown() {
                            progressDialog.dismissProgressDialog();
                        }

                        @Override
                        public void onAdFailedToShow(@NonNull AdError adError) {

                        }

                        @Override
                        public void onAdDismissed() {
                            progressDialog.dismissProgressDialog();
                        }

                        @Override
                        public void onAdClicked() {

                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {

                        }

                        @Override
                        public void onRewarded(@NonNull Reward reward) {

                            mul = 5;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mul = 1;
                                }
                            }, 3000);

                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                    progressDialog.dismissProgressDialog();
                    Toast.makeText(activity, "Не удалось загрузить реакламу:(\nПопробуйте позже", Toast.LENGTH_SHORT).show();
                }
            });

            if (mRewardedAdLoader != null) {
                final AdRequestConfiguration adRequestConfiguration =
                        new AdRequestConfiguration.Builder("R-M-2428506-2").build();
                mRewardedAdLoader.loadAd(adRequestConfiguration);
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
                Intent i = new Intent(context, ShopActivity.class);
                i.putExtra("soa", false);
                i.putExtra("interstitial_ads_shop", getIntent().getBooleanExtra("interstitial_ads_shop", true));
                i.putExtra("interstitial_ads_background_shop", getIntent().getBooleanExtra("interstitial_ads_background_shop", true));
                startActivity(i);
                overridePendingTransition(0, 0);
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

        buyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(context, BackgroundShopActivity.class);
                i.putExtra("soa", false);
                i.putExtra("interstitial_ads_background_shop", getIntent().getBooleanExtra("interstitial_ads_background_shop", true));
                i.putExtra("interstitial_ads_shop", getIntent().getBooleanExtra("interstitial_ads_shop", true));
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        buyBack.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Уменьшаем размер view
                        cardBackgroundShop.setScaleX(0.9f);
                        cardBackgroundShop.setScaleY(0.9f);
                        cardBackgroundShop.setCardBackgroundColor(context.getResources().getColor(R.color.color_2));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Возвращаем исходный размер view
                        cardBackgroundShop.setScaleX(1.0f);
                        cardBackgroundShop.setScaleY(1.0f);
                        cardBackgroundShop.setCardBackgroundColor(context.getResources().getColor(R.color.color_4));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                }
                return false;
            }
        });
//        endregion
    }
}