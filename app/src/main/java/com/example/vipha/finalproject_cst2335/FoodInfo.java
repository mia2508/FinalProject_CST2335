package com.example.vipha.finalproject_cst2335;

import java.util.Objects;

public class FoodInfo {

    private final String food;
    private final double cals;

    public FoodInfo(String food, double cals){
        this.food=food;
        this.cals=cals;

    }

    public String getFood() {
        return food;
    }

    public double getCals() {
        return cals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodInfo foodInfo = (FoodInfo) o;
        return Double.compare(foodInfo.getCals(), getCals()) == 0 &&
                Objects.equals(getFood(), foodInfo.getFood());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFood(), getCals());
    }
}
