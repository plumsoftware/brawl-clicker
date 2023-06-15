package ru.plumsoftware.brawlstarsclicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

import java.util.List;

import ru.plumsoftware.brawlstarsclicker.R;
import ru.plumsoftware.brawlstarsclicker.activities.MainActivity;
import ru.plumsoftware.brawlstarsclicker.activities.ShopActivity;
import ru.plumsoftware.brawlstarsclicker.data.Data;
import ru.plumsoftware.brawlstarsclicker.dialogs.CustomProgressDialog;
import ru.plumsoftware.brawlstarsclicker.heroes.Hero;

public class HeroAdapter extends RecyclerView.Adapter<HeroViewHolder> {
    private Context context;
    private List<Hero> list;

    private SharedPreferences sharedPreferences;
    private int gonePrice = 1;
//    private AdRequest adRequest;
//    private CustomProgressDialog progressDialog;
//    private InterstitialAd interstitialAd;

    public HeroAdapter(Context context, List<Hero> list) {
        this.context = context;
        this.list = list;
        sharedPreferences = context.getSharedPreferences(Data.SP_NAME, Context.MODE_PRIVATE);
//        progressDialog = new CustomProgressDialog(context);
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeroViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.hero_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Hero hero = list.get(position);

        holder.imageViewHero.setImageResource(hero.getResId());
        holder.textViewClick.setText("x" + Integer.toString(hero.getClick()));
        holder.textViewHeroName.setText(hero.getName());

        if (hero.isBuy()) {
            holder.imageViewIsBuy.setVisibility(View.VISIBLE);
            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.lay.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Уменьшаем размер view
                            v.setScaleX(0.9f);
                            v.setScaleY(0.9f);
                            break;
                        case MotionEvent.ACTION_UP:
                            // Возвращаем исходный размер view
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            // Возвращаем исходный размер view
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            break;
                    }
                    return false;
                }
            });
        } else {
            holder.textViewPrice.setVisibility(View.VISIBLE);
            if (gonePrice < 3) {
                holder.textViewPrice.setText(Long.toString(hero.getPrice()));
            } else {
                holder.textViewPrice.setText("???");
            }
            gonePrice = gonePrice + 1;

            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long score = sharedPreferences.getLong(Data.SP_SCORE, 0);

                    if (score >= hero.getPrice()) { // Can buy!
//                        progressDialog.showProgressDialog();
//                        interstitialAd = new InterstitialAd(context);
//                        interstitialAd.setAdUnitId("R-M-2428506-1");
//                        interstitialAd.setInterstitialAdEventListener(new InterstitialAdEventListener() {
//                            @Override
//                            public void onAdLoaded() {
//                                progressDialog.dismissProgressDialog();
//                                interstitialAd.show();
//                            }
//
//                            @Override
//                            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
//                                progressDialog.dismissProgressDialog();
//                            }
//
//                            @Override
//                            public void onAdShown() {
//
//                            }
//
//                            @Override
//                            public void onAdDismissed() {
//                                progressDialog.dismissProgressDialog();
//                            }
//
//                            @Override
//                            public void onAdClicked() {
//                                progressDialog.dismissProgressDialog();
//                            }
//
//                            @Override
//                            public void onLeftApplication() {
//
//                            }
//
//                            @Override
//                            public void onReturnedToApplication() {
//
//                            }
//
//                            @Override
//                            public void onImpression(@Nullable ImpressionData impressionData) {
//
//                            }
//                        });
//                        adRequest = new AdRequest.Builder().build();
//
//                        progressDialog = new CustomProgressDialog(context);
//                        progressDialog.setMessage("Загрузка...");

                        score = score - hero.getPrice();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Data.SP_CLICK, hero.getClick());
                        editor.putInt(Data.SP_IMAGE_RES_ID, hero.getResId());
                        editor.putBoolean(Data.SP_HEROES_IS_BUY[position], true);
                        editor.putLong(Data.SP_SCORE, score);
                        editor.apply();

                        holder.imageViewIsBuy.setVisibility(View.VISIBLE);
                        holder.textViewPrice.setVisibility(View.GONE);
                    }
                }
            });
            holder.lay.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Уменьшаем размер view
                            v.setScaleX(0.9f);
                            v.setScaleY(0.9f);
                            break;
                        case MotionEvent.ACTION_UP:
                            // Возвращаем исходный размер view
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            // Возвращаем исходный размер view
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class HeroViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imageViewHero;
    protected ImageView imageViewIsBuy;
    protected TextView textViewHeroName;
    protected TextView textViewClick;
    protected TextView textViewPrice;
    protected LinearLayout lay;

    public HeroViewHolder(@NonNull View itemView) {
        super(itemView);

        setIsRecyclable(false);

        imageViewHero = (ImageView) itemView.findViewById(R.id.imageViewHero);
        imageViewIsBuy = (ImageView) itemView.findViewById(R.id.imageViewIsBuy);

        textViewHeroName = (TextView) itemView.findViewById(R.id.textViewHeroName);
        textViewClick = (TextView) itemView.findViewById(R.id.textViewClick);
        textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);

        lay = (LinearLayout) itemView.findViewById(R.id.lay);
    }
}
