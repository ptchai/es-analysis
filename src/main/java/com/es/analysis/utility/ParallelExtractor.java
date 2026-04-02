package com.es.analysis.utility;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ParallelExtractor {

    //  use parallel stream
    public int[] extractNumbersByParallelStream(List<int[]> arrays, int min, int max) {
        int[][] generatedArrays = arrays.toArray(new int[arrays.size()][]);

        Set<Integer> availableNumbers = Collections.synchronizedSet(new HashSet<>());

        Arrays.stream(generatedArrays).parallel().forEach(currentArray -> {
            Set<Integer> numbers = Arrays.stream(currentArray)
                    .boxed()
                    .collect(Collectors.toSet());
            availableNumbers.addAll(numbers);
        });

        Set<Integer> allNumbersSynchronized = Collections.synchronizedSet(new HashSet<>(
                IntStream.rangeClosed(min, max).boxed().collect(Collectors.toSet())
        ));

        availableNumbers.parallelStream().forEach(element ->
                allNumbersSynchronized.remove(element)
        );

        return allNumbersSynchronized.stream().mapToInt(Integer::intValue).toArray();
    }

    // use ExecutorService
    public int[] extractNumbersParallel(List<int[]> arrays, int min, int max) throws InterruptedException {
        Set<Integer> availableNumbers = Collections.synchronizedSet(new HashSet<>());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int[] a : arrays) {
                executor.submit(() -> {
                    Set<Integer> numbers = Arrays.stream(a)
                            .boxed()
                            .collect(Collectors.toSet());
                    availableNumbers.addAll(numbers);
                });
            }

            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }

        Set<Integer> allNumbersSynchronized = Collections.synchronizedSet(new HashSet<>(
                IntStream.rangeClosed(min, max).boxed().collect(Collectors.toSet())
        ));

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int element : availableNumbers) {
                executor.submit(() -> {
                    allNumbersSynchronized.remove(element);
                });
            }

            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }

        return allNumbersSynchronized.stream().mapToInt(Integer::intValue).toArray();
    }
}
