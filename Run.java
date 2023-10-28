

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.stream.IntStream;

import com.myvrp.PreprocessData.*;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;

public class Run {

    private double[][] distanceMatrix;

    // PreprocessData preprocessData = new PreprocessData();

    Particle particle;

    public Run(String filePath) throws Exception {
        this.distanceMatrix = com.myvrp.PreprocessData.getdistanceMatrix(filePath);
        this.particle = new Particle(distanceMatrix.length);
    }
    

    public double computeTotalDistance(int[] route) {
        double totalDistance = 0.0;
        int prevCustomer = 0;  // starting from depot
        for (int customer : route) {
            totalDistance += distanceMatrix[prevCustomer][customer];
            prevCustomer = customer;
        }
        totalDistance += distanceMatrix[prevCustomer][0];  // returning to depot
        return totalDistance;
    }

    public static void main(String[] args) throws Exception {
        Run calculator = new Run("com\\myvrp\\C101.txt");
        
        List<int[]> randomSequences = calculator.particle.generateMultipleRandomPositions(10);  // Generate 10 random sequences

        for (int[] sequence : randomSequences) {
            System.out.println("Random Sequence: " + Arrays.toString(sequence));
            double totalDistance = calculator.computeTotalDistance(sequence);
            System.out.println("Total Distance: " + totalDistance);
            System.out.println("----------------------------");
        }
    }
}





class Particle {

    int[] position;
    double fitness;
    int[] pBestPosition;
    double pBestFitness;

    public Particle(int numberOfCustomers) {
        this.position = fillPosition(new int[numberOfCustomers]);
        this.fitness = Double.MAX_VALUE;  // Initialize to a large value
        this.pBestPosition = position.clone();
        this.pBestFitness = fitness;
    }

    //shuffle position of particle with random sequences
    int[] fillPosition(int[] position) {
        Random rand = new Random();
        List<Integer> list = IntStream.rangeClosed(1, position.length-1).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        return list.stream().mapToInt(i -> i).toArray();
    }

    // Generate multiple random positions
    public List<int[]> generateMultipleRandomPositions(int count) {
        return IntStream.range(0, count)
                        .mapToObj(i -> fillPosition(new int[position.length]))
                        .collect(Collectors.toList());
    }
}
