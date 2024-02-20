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
            // Given
            final String text = "";

            // When
            final int result = sut.add(text);

            // Then
            assertEquals(0, result);
        }

        @Test
        void null_string_should_be_zero() {
            // Given
            final String text = null;

            // When
            final int result = sut.add(text);

            // Then
            assertEquals(0, result);
        }


        @ParameterizedTest
        @CsvSource({
                "'1,2,3', 6",
                "'1,2:3', 6",
                "'1:2:3', 6",
        })
        void normal_case_should_sum_of_values(String input, int expected) {
            // Given
            final String text = input;

            // When
            final int result = sut.add(text);

            // Then
            assertEquals(expected, result);
        }


        @ParameterizedTest
        @CsvSource({
                "' 1'",
                "' 1, 2'",
                "'1 ,2 :3 '",
                "' 1, 2: 3'",
        })
        void number_with_spaces_throws_error(String input) {
            // Given
            final String text = input;

            // Then
            assertThrows(RuntimeException.class, () -> sut.add(text));
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
            // Given
            final String text = input;

            // Then
            assertThrows(RuntimeException.class, () -> sut.add(text));
        }

        @ParameterizedTest
        @CsvSource({
                "'-1'",
                "'-1:2,3'",
                "'-1,-2,-3'"
        })
        void negative_number_should_throw_error(String input) {
            // Given
            final String text = input;

            // Then
            assertThrows(RuntimeException.class, () -> sut.add(text));
        }

        @ParameterizedTest
        @CsvSource({
                "'//+\n1+2', 3",
                "'// \n1 2 3', 6",
                "'//+\n1,2+3', 6"
        })
        void custom_delimiter_should_sum_of_values(String input, int expected) {
            // Given
            final String text = input;

            // When
            final int result = sut.add(text);

            // Then
            assertEquals(expected, result);
        }

    }
}
