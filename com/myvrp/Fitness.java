package com.myvrp;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Fitness {


    List<Integer> initializeRandomIndividual(){

        List<Integer> individual = new ArrayList<Integer>(customer_data.keySet());
        Collections.shuffle(individual);

        return individual;

    }


    List<Integer> random_individual = initializeRandomIndividual();


    VehicleData vehicleData = new VehicleData();

    double[][] distanceMatrix = PreprocessData.getdistanceMatrix("C101.txt");

    Map<Integer,int[]> customer_data = PreprocessData.getCustomerData("C101.txt");

    HashMap<Integer, Integer> vehicle_data = vehicleData.vehicleData(25, 40, 80);

    public ArrayList<ArrayList<Integer>> possibleRoutes(List<Integer> random_individual, Map<Integer,int[]> customer_data, HashMap<Integer,Integer> vehicle_data){

        ArrayList<ArrayList<Integer>> route = new ArrayList<ArrayList<Integer>>();

        ArrayList<Integer> subroute = new ArrayList<Integer>();

        int vehicle_load = 0;
        int prev_cust_id = 0;

        int current_vehicle_id = 0;

        for(Integer customer_id : random_individual){

            int demand = customer_data.get(customer_id)[2];
            int vehicle_capacity ; // Fetching capacity dynamically for each vehicle

            
            if (vehicle_data.containsKey(current_vehicle_id)) {

                vehicle_capacity = vehicle_data.get(current_vehicle_id);

            }

            else {
                vehicle_capacity = 0;
            }

            int updated_vehicle_load = vehicle_load + demand;

            if(updated_vehicle_load <= vehicle_capacity){
                subroute.add(customer_id); 
                vehicle_load = updated_vehicle_load;
            }

            else {
                route.add(new ArrayList<Integer>(subroute)); // Create a copy of the subroute
                subroute.clear();
                subroute.add(customer_id);
                vehicle_load = demand;
                current_vehicle_id += 1;  //for new sub_route new vehicle id
            }
            prev_cust_id = customer_id;
        }

        if(!subroute.isEmpty()) {
            route.add(subroute); // Adding the last subroute
        }

        return route;
    }


    public Map<Double,Double> fitness(List<Integer> random_individual, Map<Integer,int[]> customer_data){

        List<Double> result = new ArrayList<Double>();

        double transport_cost = 10.0;

        int vehicle_setup_cost = 0;

        ArrayList<ArrayList<Integer>> route_instance = possibleRoutes(random_individual, customer_data, vehicle_data);

        double total_cost = Double.MAX_VALUE;

        Map<Double,Double> fitness_map = new HashMap<Double,Double>();
        
        double fitness_value = 0;

        //maximum vehicle count
        int max_vehicle_count = vehicle_data.size();

        int length_of_route = route_instance.size();

        if(length_of_route<=max_vehicle_count){


           total_cost = 0;

            //traverse the route_instance
            for(int i=0;i<length_of_route;i++){

                int route_length = route_instance.get(i).size();

                double sub_route_distance=0;

                int prev_cutomer_id = 0;

                //traverse the route
                for(int j=0;j<route_length;j++){    //[ [c1,c2]]  //distancematrix[0][c1]

                    double distance = distanceMatrix[prev_cutomer_id][route_instance.get(i).get(j)];

                    sub_route_distance += distance;

                    prev_cutomer_id = route_instance.get(i).get(j);
                }

                //distance from last customer id to depot
                double distance_to_depot = distanceMatrix[prev_cutomer_id][0];

                sub_route_distance += distance_to_depot;

                //sub_route_transport_cost = sub_route_distance * transport_cost_per_km

                double sub_route_transport_cost = sub_route_distance *transport_cost + vehicle_setup_cost;

                total_cost += sub_route_transport_cost;

            }

            fitness_value =  100000.0 /total_cost;

            //add the fitness value and total cost in the map
           

            fitness_map.put(fitness_value,total_cost);

        }

        return fitness_map; //{fitness_value:total_cost}
    } 
}
