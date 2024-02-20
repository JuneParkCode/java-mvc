package com.warmup.calculator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StringCalculatorTest {
    @Nested
    @DisplayName("method add")
    class Add {
        private StringCalculator sut;

        @BeforeEach
        void setup() {
            sut = new StringCalculator();
        }

        @AfterEach()
        void tearDown(){
        }

        @Test
        void empty_string_should_be_zero() {
            assertEquals(0, sut.add(""));
        }

        @Test
        void null_string_should_be_zero() {
            assertEquals(0, sut.add(null));
        }


        @ParameterizedTest
        @CsvSource({
                "'1,2,3', 6",
                "'1,2:3', 6",
                "'1:2:3', 6",
        })
        void normal_case_should_sum_of_values(String input, int expected) {
            assertEquals(expected, sut.add(input));
        }


        @ParameterizedTest
        @CsvSource({
                "' 1'",
                "' 1, 2'",
                "'1 ,2 :3 '",
                "' 1, 2: 3'",
        })
        void number_with_spaces_throws_error(String input) {
            assertThrows(RuntimeException.class, () -> sut.add(input));
        }

        @ParameterizedTest
        @CsvSource({
                "' '",
                "' ,'",
                "':'",
                "','",
                "',:'",
                "'1,,2",
                "'1,:"
        })
        void empty_number_throws_error(String input) {
            assertThrows(RuntimeException.class, () -> sut.add(input));
        }

        @ParameterizedTest
        @CsvSource({
                "'-1'",
                "'-1:2,3'",
                "'-1,-2,-3'"
        })
        void negative_number_should_throw_error(String input) {
            assertThrows(RuntimeException.class, () -> sut.add(input));
        }

        @ParameterizedTest
        @CsvSource({
                "'//+\n1+2', 3",
                "'// \n1 2 3', 6",
                "'//+\n1,2+3', 6"
        })
        void custom_delimiter_should_sum_of_values(String input, int expected) {
            assertEquals(expected, sut.add(input));
        }

    }
}
