package com.example.tradingplatform.reposiroty;

public enum AuctionDuration {
    ONE_DAY(1),
    FIVE_DAYS(5),
    TEN_DAYS(10),
    TWENTY_DAYS(20);

    private final int days;

    AuctionDuration(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}

