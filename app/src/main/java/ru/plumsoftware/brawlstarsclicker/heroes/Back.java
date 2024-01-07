package ru.plumsoftware.brawlstarsclicker.heroes;

public class Back {
    private int resId;
    private String name;
    private long price;
    private boolean isBuy;

    public Back(int resId, String name, long price) {
        this.resId = resId;
        this.name = name;
        this.price = price;
    }

    public Back(int resId, String name, long price, boolean isBuy) {
        this.resId = resId;
        this.name = name;
        this.price = price;
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

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}
