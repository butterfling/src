package com.myvrp;

import java.util.HashMap;

public class VehicleData {

    HashMap<Integer, Integer> vehicleData = new HashMap<Integer, Integer>();

    //assign random capactity to each vehicle between some range , and insert it to this map
    public  HashMap<Integer, Integer> vehicleData(int numberOfVehicles, int minCapacity, int maxCapacity) {
        for (int i = 0; i < numberOfVehicles; i++) {
            int capacity = (int) (Math.random() * (maxCapacity - minCapacity + 1) + minCapacity);
            vehicleData.put(i, capacity);
        }

        return vehicleData;
        
    }

    
}
