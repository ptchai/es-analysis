package com.es.analysis.utility;

import org.springframework.stereotype.Component;

@Component
public class PrimeCheck {

    public boolean isPrime(Integer n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        } 

        return true;
    }
}
