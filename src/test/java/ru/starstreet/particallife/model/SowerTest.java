package ru.starstreet.particallife.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class SowerTest {

    private static Stream<Arguments> testArguments(){
        return Stream.of(
                Arguments.of(399, 400),
                Arguments.of(375, 400),
                Arguments.of(401, 441),
                Arguments.of(500, 529)

        );
    }


    @ParameterizedTest
    @MethodSource("testArguments")
    public void getGridRectanglesAmount(int amount, int expected) {
        Assertions.assertEquals(expected, Sower.GRID.getGridRectanglesAmount(amount));
    }

}