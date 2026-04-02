package com.es.analysis.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParallelExtractorTest {

    @Autowired
    private ParallelExtractor parallelExtractor;

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new int[] {9, 20, 19, 0, 12, 15, 6, 5, 13},
                                new int[] {20, 3, 9, 4, 7, 16, 11, 6, 13},
                                new int[] {0, 20, 19, 4, 14, 3, 10, 2, 9},
                                new int[] {18, 11, 13, 16, 5, 8, 0, 14, 19}),
                        0, 20, new int[] {1, 17}
                ),
                Arguments.of(
                        Arrays.asList(
                                new int[] {16, 15, 12, 17, 9, 20, 11, 18},
                                new int[] {14, 12, 18, 5, 16, 11, 20, 9},
                                new int[] {15, 5, 6, 8, 11, 14, 12, 9},
                                new int[] {17, 7, 9, 15, 11, 8, 19, 13}),
                        5, 20, new int[] {10}
                )
        );
    }

    // parallel stream
    @ParameterizedTest
    @MethodSource("testData")
    void extractNumbersByParallelStream(List<int[]> generatedArrays, int min, int max, int[] expectedResult) {
        int[] extractedNumbers = parallelExtractor.extractNumbersByParallelStream(generatedArrays, min, max);
        assertEquals(expectedResult.length, extractedNumbers.length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertEquals(expectedResult[i], extractedNumbers[i]);
        }
    }

    // ExecutorService
    @ParameterizedTest
    @MethodSource("testData")
    void extractNumbersParallel(List<int[]> generatedArrays, int min, int max, int[] expectedResult) throws InterruptedException {
        int[] extractedNumbers = parallelExtractor.extractNumbersParallel(generatedArrays, min, max);
        assertEquals(expectedResult.length, extractedNumbers.length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertEquals(expectedResult[i], extractedNumbers[i]);
        }
    }
}