package ru.plumsoftware.brawlstarsclicker.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;

import java.util.List;

import ru.plumsoftware.brawlstarsclicker.R;
import ru.plumsoftware.brawlstarsclicker.adapter.BackAdapter;
import ru.plumsoftware.brawlstarsclicker.data.Data;
import ru.plumsoftware.brawlstarsclicker.dialogs.CustomProgressDialog;
import ru.plumsoftware.brawlstarsclicker.heroes.Back;
import ru.plumsoftware.brawlstarsclicker.heroes.Backs;

public class BackgroundShopActivity extends AppCompatActivity {
    private long score;
    private int click;
    private int imageResId;
    private SharedPreferences sharedPreferences;
    @Nullable
    private InterstitialAd mInterstitialAd = null;
    @Nullable
    private InterstitialAdLoader mInterstitialAdLoader = null;
    private CustomProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_shop);

//        region::Base variables
        Context context = BackgroundShopActivity.this;
        Activity activity = BackgroundShopActivity.this;

        MobileAds.initialize(this, () -> {

        });

        mInterstitialAdLoader = new InterstitialAdLoader(BackgroundShopActivity.this);
        mInterstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                progressDialog.dismissProgressDialog();
                if (mInterstitialAd != null) {
                    mInterstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                        @Override
                        public void onAdShown() {

                        }

                        @Override
                        public void onAdFailedToShow(@NonNull AdError adError) {

                        }

                        @Override
                        public void onAdDismissed() {
                            finish();
                            overridePendingTransition(0, 0);
                            Intent i = new Intent(BackgroundShopActivity.this, MainActivity.class);
                            i.putExtra("soa", false);
                            int a = getIntent().getIntExtra("interstitial_ads_count", 0) + 1;
                            i.putExtra("interstitial_ads_count", a);
                            startActivity(i);
                        }

                        @Override
                        public void onAdClicked() {
                            finish();
                            overridePendingTransition(0, 0);
                            Intent i = new Intent(BackgroundShopActivity.this, MainActivity.class);
                            i.putExtra("soa", false);
                            int a = getIntent().getIntExtra("interstitial_ads_count", 0) + 1;
                            i.putExtra("interstitial_ads_count", a);
                            startActivity(i);
                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {

                        }
                    });
                }
                mInterstitialAd.show(BackgroundShopActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                progressDialog.dismissProgressDialog();
                finish();
                overridePendingTransition(0, 0);
                Intent i = new Intent(BackgroundShopActivity.this, MainActivity.class);
                i.putExtra("soa", false);
                int a = getIntent().getIntExtra("interstitial_ads_count", 0);
                i.putExtra("interstitial_ads_count", a);
                startActivity(i);
            }
        });
        progressDialog = new CustomProgressDialog(BackgroundShopActivity.this);
        progressDialog.setMessage("Загрузка...");
//        endregion

//        region::Find views
        TextView textViewScore2 = (TextView) activity.findViewById(R.id.textViewScore2);
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerViewHeroes);
//        endregion

//        region::Get data
        sharedPreferences = context.getSharedPreferences(Data.SP_NAME, Context.MODE_PRIVATE);
        score = sharedPreferences.getLong(Data.SP_SCORE, 0);
        click = sharedPreferences.getInt(Data.SP_CLICK, 1);
        imageResId = sharedPreferences.getInt(Data.SP_IMAGE_RES_ID, R.drawable.leon_1);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                textViewScore2.setText(Long.toString(sharedPreferences.getLong(Data.SP_SCORE, 0)));
            }
        });
//        endregion

//        region::Setup heroes
        textViewScore2.setText(Long.toString(score));
        List<Back> list = Backs.buildHeroes();
        for (int i = 0; i < list.size(); i++) {
            Back back = list.get(i);
            back.setBuy(sharedPreferences.getBoolean(Data.SP_BACK_IS_BUY[i], false));
            list.set(i, back);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new BackAdapter(context, list));
//        endregion
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAdLoader != null) {
            if (
                    getIntent().getIntExtra("interstitial_ads_count", 0) % 4 == 0
            ) {
                progressDialog.showProgressDialog();
                final AdRequestConfiguration adRequestConfiguration =
                        new AdRequestConfiguration.Builder("R-M-2428506-1").build();
                mInterstitialAdLoader.loadAd(adRequestConfiguration);
            } else {
                progressDialog.dismissProgressDialog();

                finish();
                overridePendingTransition(0, 0);

                Intent i = new Intent(BackgroundShopActivity.this, MainActivity.class);
                i.putExtra("soa", false);
                int a = getIntent().getIntExtra("interstitial_ads_count", 0);
                i.putExtra("interstitial_ads_count", a);
                startActivity(i);
            }
        } else {
            finish();
            overridePendingTransition(0, 0);

            Intent i = new Intent(BackgroundShopActivity.this, MainActivity.class);
            i.putExtra("soa", false);
            int a = getIntent().getIntExtra("interstitial_ads_count", 0);
            i.putExtra("interstitial_ads_count", a);
            startActivity(i);
        }
    }
}