package com.warmup.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator sut;

    @BeforeEach
    void setUp() {
        sut = new Calculator();
    }
    @Test
    void add_number() {
        assertEquals(4, sut.add(2, 2));
    }

    @Test
    void subtract_number() {
        assertEquals(0, sut.subtract(2, 2));
    }

    @Test
    void multiply_number() {
        assertEquals(4, sut.multiply(2, 2));
    }

    @Test
    void divide_number() {
        assertEquals(1, sut.divide(2, 2));
    }

    @Test
    void divide_by_zero_throws_exception() {
        assertThrows(ArithmeticException.class, () -> sut.divide(2, 0));
    }
}