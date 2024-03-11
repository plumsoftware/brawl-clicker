package ru.plumsoftware.brawlstarsclicker.heroes;

import java.util.ArrayList;
import java.util.List;

import ru.plumsoftware.brawlstarsclicker.R;

public abstract class Heroes {
    public static final String[] HEROES_NAMES = new String[]{
            "Спайк",
            "Танцевальный Поко",
            "Брок рэпер",
            "Легендарный Спайк",
            "Нита и медведь",
            "Эль` Примо кароль",
            "Кольт",
            "Поко в смокинге",
            "Космический Булл",
            "Легендарная Лола",
            "Леон",
            "Леон желтый",
            "Легендарный Эль` Примо",
            "Легендарный медведь Ниты",
            "Космический Брок",
            "Легендарный Леон",
            "Секретный бравлер",
            "Ужасная птица \uD83D\uDE2E",
            "???",
            "Громила",
            "Элитная птица"

    };
    public static final int[] HEROES_RES_ID = new int[]{
            R.drawable.spike_1,
            R.drawable.poco_1,
            R.drawable.brock_1,
            R.drawable.spike_gold,
            R.drawable.nita_2,
            R.drawable.elprimo_2,
            R.drawable.colt_1,
            R.drawable.poco_2,
            R.drawable.bull_1,
            R.drawable.lola_gold,
            R.drawable.leon_1,
            R.drawable.leon_2,
            R.drawable.elprimo_gold,
            R.drawable.nita_gold,
            R.drawable.brock_2,
            R.drawable.leon_gold,
            R.drawable.amogus,
            R.drawable.bird,
            R.drawable.monster,
            R.drawable.grom_1,
            R.drawable.gold_bird_1
    };
    public static final int[] HEROES_CLICKS = new int[]{
            1,
            2,
            5,
            10,
            20,
            25,
            30,
            40,
            50,
            65,
            80,
            100,
            120,
            140,
            200,
            250,
            300,
            350,
            400,
            450,
            500
    };
    public static final int[] HEROES_PRICES = new int[]{
            0, // 0
            25, // 1
            100, // 2
            500, // 5
            1500, // 10
            3000, // 20
            9000, // 25
            15000, // 30
            25000, // 40
            55000, // 50
            70000, // 65
            100000, // 80
            150000, // 100
            250000, // 120
            500000, // 140
            1000000, // 200
            1200000, // 350
            1750000, // 400
            2500000, // 450
            3000000, // 500
            4000000,
    };

    public static List<Hero> buildHeroes() {
        List<Hero> list = new ArrayList<>();
        for (int i = 0; i < HEROES_NAMES.length; i++) {
            list.add(new Hero(HEROES_RES_ID[i], HEROES_NAMES[i], HEROES_PRICES[i], HEROES_CLICKS[i]));
        }
        return list;
    }
}
