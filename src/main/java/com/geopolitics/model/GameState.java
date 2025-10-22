package com.geopolitics.model;

import java.time.LocalDate;

public class GameState {
    public enum Speed { NORMAL, DOUBLE, TRIPLE }

    private Country country;
    private LocalDate currentDate;
    private long population; // total population
    private double economyBillions; // GDP in billions USD
    private long militaryReserve; // 3% of population
    private Speed speed = Speed.NORMAL;

    private double dayAccumulator = 0.0; // for fractional days

    public GameState() {}

    public static GameState newGame(Country country) {
        GameState state = new GameState();
        state.country = country;
        state.currentDate = LocalDate.of(1992, 12, 1);
        state.population = country.getInitialPopulation();
        state.economyBillions = country.getInitialEconomyBillions();
        state.recalculateReserve();
        return state;
    }

    public void recalculateReserve() {
        this.militaryReserve = Math.round(this.population * 0.03);
    }

    public void applyGrowth(double days) {
        // Simple growth models (very rough):
        // population: ~1.2% per year; economy: ~3% per year
        double years = days / 365.0;
        double newPopulation = this.population * Math.pow(1.012, years);
        double newEconomy = this.economyBillions * Math.pow(1.03, years);
        this.population = Math.round(newPopulation);
        this.economyBillions = newEconomy;
        recalculateReserve();
    }

    public Country getCountry() { return country; }

    public void setCountry(Country country) { this.country = country; }

    public LocalDate getCurrentDate() { return currentDate; }

    public void setCurrentDate(LocalDate currentDate) { this.currentDate = currentDate; }

    public long getPopulation() { return population; }

    public void setPopulation(long population) { this.population = population; }

    public double getEconomyBillions() { return economyBillions; }

    public void setEconomyBillions(double economyBillions) { this.economyBillions = economyBillions; }

    public long getMilitaryReserve() { return militaryReserve; }

    public void setMilitaryReserve(long militaryReserve) { this.militaryReserve = militaryReserve; }

    public Speed getSpeed() { return speed; }

    public void setSpeed(Speed speed) { this.speed = speed; }

    public double getDayAccumulator() { return dayAccumulator; }

    public void setDayAccumulator(double dayAccumulator) { this.dayAccumulator = dayAccumulator; }
}
