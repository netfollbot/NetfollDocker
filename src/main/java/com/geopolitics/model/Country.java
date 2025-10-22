package com.geopolitics.model;

public enum Country {
    GEORGIA("country.ge", "🇬🇪", 5_500_000L, 2.5),
    ARMENIA("country.am", "🇦🇲", 3_500_000L, 2.0),
    AZERBAIJAN("country.az", "🇦🇿", 7_000_000L, 0.45);

    private final String i18nKey;
    private final String flagEmoji;
    private final long initialPopulation;
    private final double initialEconomyBillions;

    Country(String i18nKey, String flagEmoji, long initialPopulation, double initialEconomyBillions) {
        this.i18nKey = i18nKey;
        this.flagEmoji = flagEmoji;
        this.initialPopulation = initialPopulation;
        this.initialEconomyBillions = initialEconomyBillions;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public String getFlagEmoji() {
        return flagEmoji;
    }

    public long getInitialPopulation() {
        return initialPopulation;
    }

    public double getInitialEconomyBillions() {
        return initialEconomyBillions;
    }
}
