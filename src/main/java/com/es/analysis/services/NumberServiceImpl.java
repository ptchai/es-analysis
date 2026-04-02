package com.es.analysis.services;

import com.es.analysis.utility.ParallelExtractor;
import com.es.analysis.utility.PrimeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class NumberServiceImpl implements NumberService {
    @Value("${analysis.number-of-arrays}")
    private int numberOfArrays;

    @Value("${analysis.array-size}")
    private int arraySize;

    @Value("${analysis.range-of-numbers.min}")
    private int rangeMin;

    @Value("${analysis.range-of-numbers.max}")
    private int rangeMax;

    private List<int[]> generatedArrays;

    @Autowired
    private ParallelExtractor parallelExtractor;

    @Autowired
    private PrimeCheck primeCheck;

    public NumberServiceImpl(ParallelExtractor parallelExtractor, PrimeCheck primeCheck) {
        this.parallelExtractor = parallelExtractor;
        this.primeCheck = primeCheck;
    }

    public List<int[]> generateArrays() {
        List<int[]> generated = new ArrayList<>();

        Random rand = new Random();
        IntStream.range(0, numberOfArrays).forEach(i -> {
            int[] array = rand.ints(rangeMin, rangeMax + 1)
                    .distinct()
                    .limit(arraySize)
                    .toArray();
            generated.add(array);
        });

        return generated;
    }

    public int[] extractNumbers(List<int[]> generatedArrays, int min, int max) {
        Set<Integer> availableNumbers = new HashSet<>();

        generatedArrays.forEach(currentArray -> {
            Set<Integer> numbers = Arrays.stream(currentArray)
                    .boxed()
                    .collect(Collectors.toSet());
            availableNumbers.addAll(numbers);
        });

        Set<Integer> allNumbers = IntStream.rangeClosed(min, max).boxed().collect(Collectors.toSet());
        availableNumbers.forEach(element ->
                allNumbers.remove(element)
        );

        return allNumbers.stream().mapToInt(Integer::intValue).toArray();
    }

    public int getLargestPrimeNumber(int[] numbers) {
        OptionalInt maxPrime = Arrays.stream(numbers)
                .filter(x -> primeCheck.isPrime((Integer) x))
                .max();

        return maxPrime.orElse(-1);
    }

    public void perform() {
        System.out.println("Advanced Number Availability & Prime Extraction Service...\n");
        System.out.println("Configuration: ");
        System.out.println("number of arrays: " + numberOfArrays);
        System.out.println("array size: " + arraySize);
        System.out.println("range min: " + rangeMin);
        System.out.println("range max: " + rangeMax);

        generatedArrays = generateArrays();
        System.out.println("\nGenerated arrays: ");
        for (int[] arr : generatedArrays) {
            System.out.println(Arrays.toString(arr));
        }

        int[] numbers = extractNumbers(generatedArrays, rangeMin, rangeMax);
        int[] numbersParallel = null;
        try {
            numbersParallel = parallelExtractor.extractNumbersParallel(generatedArrays, rangeMin, rangeMax);
        } catch (InterruptedException e) {
            System.out.println("parallel extractor interrupted.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\nAvailable numbers: ");
        System.out.println(Arrays.toString(numbers));
        System.out.println("\nAvailable numbers (parallel): ");
        System.out.println(Arrays.toString(numbersParallel) + "\n");

        int primeMax = getLargestPrimeNumber(numbers);
        if (primeMax > 0) {
            System.out.println("Largest prime number: " + primeMax);
        } else {
            System.out.println("No primes found");
        }
    }
}
