package com.example.vipha.finalproject_cst2335;

import android.support.annotation.NonNull;
/**
 * This is model class BusRoute
 * **/

public class BusRoute {

    private String routeNum;
    private String directionID;
    private String direction;
    private String dirHeading;

    public BusRoute() {
        this("", "", "", "");
    }

    public BusRoute(String routeNum, String directionID, String direction, String routeHeading) {
        this.routeNum = routeNum;
        this.directionID = directionID;
        this.direction = direction;
        this.dirHeading = routeHeading;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getDirectionID() {
        return directionID;
    }

    public void setDirectionID(String directionID) {
        this.directionID = directionID;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getdirHeading() {
        return dirHeading;
    }

    public void setdireHeading(String routeHeading) {
        this.dirHeading = dirHeading;
    }

    public String toString() {
        return "Route: " +
                routeNum +
                System.lineSeparator() +
                "Direction: " +
                direction +
                System.lineSeparator() +
                "Route Heading: " +
                dirHeading;
    }
}