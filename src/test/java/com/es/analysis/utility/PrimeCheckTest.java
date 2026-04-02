package com.es.analysis.utility;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrimeCheckTest {

    @Autowired
    private PrimeCheck primeCheck;

    @Test
    void isPrime() {
        Boolean result = primeCheck.isPrime(2);
        assertEquals(true, result);

        result = primeCheck.isPrime(4);
        assertEquals(false, result);
    }
}