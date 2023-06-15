package ru.plumsoftware.brawlstarsclicker.heroes;

public class Hero {
    private int resId;
    private String name;
    private long price;
    private int click;
    private boolean isBuy;

    public Hero(int resId, String name, long price, int click) {
        this.resId = resId;
        this.name = name;
        this.price = price;
        this.click = click;
    }

    public Hero(int resId, String name, long price, int click, boolean isBuy) {
        this.resId = resId;
        this.name = name;
        this.price = price;
        this.click = click;
        this.isBuy = isBuy;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}
