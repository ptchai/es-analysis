package com.es.analysis.services;

import com.es.analysis.utility.ParallelExtractor;
import com.es.analysis.utility.PrimeCheck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NumberServiceImplTest {

    @Value("${analysis.number-of-arrays}")
    private int numberOfArrays;

    @Value("${analysis.array-size}")
    private int arraySize;

    @Value("${analysis.range-of-numbers.min}")
    private int rangeMin;

    @Value("${analysis.range-of-numbers.max}")
    private int rangeMax;

    @Autowired
    private NumberServiceImpl numberService;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
    }

    private static Stream<Arguments> AvailableNumberData() {
        return Stream.of(
                Arguments.of(
                        new int[]{1, 7, 10, 14, 15, 19, 20, 23, 27, 28, 30, 32, 35, 37, 39, 41}, 41
                ),
                Arguments.of(
                        new int[]{0, 1, 4, 12, 15, 18, 20, 22, 25, 28, 32, 36, 40, 98, 100}, -1
                ),
                Arguments.of(
                        new int[]{0, 1, 4, 11, 12, 15, 17, 18, 19, 20, 22, 23, 25, 28, 29, 31, 40}, 31
                )
        );
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new int[] {9, 20, 17, 19, 0, 12, 15, 6, 5, 13},
                                new int[] {20, 3, 9, 4, 7, 16, 11, 6, 13, 12},
                                new int[] {0, 20, 19, 4, 14, 3, 10, 2, 6, 9},
                                new int[] {18, 11, 13, 16, 5, 8, 0, 14, 20, 19}),
                        0, 20, 1
                ),
                Arguments.of(
                        Arrays.asList(
                                new int[] {16, 13, 15, 12, 17, 9, 20, 11, 14, 18},
                                new int[] {14, 12, 18, 5, 16, 11, 20, 9, 19, 6},
                                new int[] {15, 5, 6, 8, 11, 14, 13, 20, 12, 9},
                                new int[] {17, 7, 9, 15, 11, 8, 20, 19, 14, 16}),
                        5, 20, 10
                )
        );
    }

    @Test
    void generateArrays() {
        List<int[]> arrays = numberService.generateArrays();

        assertEquals(arrays.size(), numberOfArrays);
        for (int[] array : arrays) {
            assertEquals(array.length, arraySize);
            int min = Arrays.stream(array).min().getAsInt();
            int max = Arrays.stream(array).max().getAsInt();
            assertTrue(min >= rangeMin);
            assertTrue(max <= rangeMax);
        }
    }

    @ParameterizedTest
    @MethodSource("AvailableNumberData")
    void getLargestPrimeNumber(int[] numbers, int expectedResult) {
        int maxPrime = numberService.getLargestPrimeNumber(numbers);
        assertEquals(expectedResult, maxPrime);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void extractNumbers(List<int[]> generatedArrays, int min, int max, int expectedResult) {
        int[] extractedNumbers = numberService.extractNumbers(generatedArrays, min, max);
        assertEquals(1, extractedNumbers.length);
        assertEquals(expectedResult, extractedNumbers[0]);
    }

    @Test
    void performTestNoPrimes() throws InterruptedException {
        NumberServiceImpl spyNumberService = Mockito.spy(numberService);

        List<int[]> arrays = Arrays.asList(
                new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29,31, 37, 41, 43, 47, 49});

        Mockito.doReturn(arrays).when(spyNumberService).generateArrays();

        spyNumberService.perform();

        String[] output = outputStreamCaptor.toString().trim().split("\r\n");
        String actual = output[output.length - 1];
        assertEquals("No primes found", actual);
    }

    @Test
    void performTestLargestPrimeNumber() throws InterruptedException {
        NumberServiceImpl spyNumberService = Mockito.spy(numberService);

        List<int[]> arrays = Arrays.asList(
                new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 41, 43, 47, 49});

        Mockito.doReturn(arrays).when(spyNumberService).generateArrays();

        spyNumberService.perform();

        String[] output = outputStreamCaptor.toString().trim().split("\r\n");
        String actual = output[output.length - 1];
        assertEquals("Largest prime number: 37", actual);
    }

    @Test
    void performTestParallelExtractorException() throws InterruptedException {
        ParallelExtractor mockedParallelExtractor = Mockito.mock(ParallelExtractor.class);
        PrimeCheck mockedPrimeCheck = Mockito.mock(PrimeCheck.class);

        when(mockedParallelExtractor.extractNumbersParallel(anyList(), anyInt(), anyInt()))
                .thenThrow(InterruptedException.class);

        NumberServiceImpl spyNumberService = Mockito.spy(new NumberServiceImpl(mockedParallelExtractor, mockedPrimeCheck));
        Mockito.doReturn(1).when(spyNumberService).getLargestPrimeNumber(new int[] {0});

        spyNumberService.perform();

        String[] output = outputStreamCaptor.toString().trim().split("\r\n");
        boolean exists = Arrays.asList(output).contains("parallel extractor interrupted.");
        assertTrue(exists);
    }
}