package ru.plumsoftware.brawlstarsclicker.heroes;

import java.util.ArrayList;
import java.util.List;

import ru.plumsoftware.brawlstarsclicker.R;

public abstract class Backs {
    public static final String[] BACKS_NAMES = new String[]{
            "Классический задний фон",
            "Морской задний фон",
            "Пустыня",
            "Каньон"
    };
    public static final int[] BACKS_RES_ID = new int[]{
            R.drawable.back_2,
            R.drawable.back_3,
            R.drawable.back_4,
            R.drawable.back_5
    };
    public static final int[] BACKS_PRICES = new int[]{
            5000,
            100000,
            2000000,
            5500000
    };

    public static List<Back> buildHeroes() {
        List<Back> list = new ArrayList<>();
        for (int i = 0; i < BACKS_NAMES.length; i++) {
            list.add(new Back(BACKS_RES_ID[i], BACKS_NAMES[i], BACKS_PRICES[i]));
        }
        return list;
    }
}
