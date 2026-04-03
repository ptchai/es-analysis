package com.es.analysis.services;

import java.util.List;

public interface NumberService {
    int[] extractNumbers(List<int[]> generatedArrays, int min, int max);
    int getLargestPrimeNumber(int[] numbers);
    void perform();
}
