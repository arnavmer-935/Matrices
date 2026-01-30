package main;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MatrixTest {

    @Nested
    @DisplayName("Creation & Factory Methods")
    class CreationAndFactoryTest {

        @Test
        @DisplayName("Row/column constructor creates zero-filled matrix with correct dimensions")
        void constructorWithRowsAndColsCreatesZeroMatrix() {

        }

        @Test
        @DisplayName("Square constructor creates matrix with equal row and column count")
        void squareConstructorCreatesCorrectDimensions() {

        }

        @Test
        @DisplayName("Constructor rejects negative or zero dimensions")
        void constructorRejectsInvalidDimensions() {

        }

        @Test
        @DisplayName("Constructor from 2D array performs deep copy")
        void constructorFrom2DArrayPerformsDeepCopy() {

        }

        @Test
        @DisplayName("Constructor rejects jagged 2D arrays")
        void constructorRejectsJaggedArrays() {

        }

        @Test
        @DisplayName("Constant factory fills matrix with the given value")
        void constantFactoryFillsMatrixWithValue() {

        }

        @Test
        @DisplayName("Scalar factory creates 1x1 matrix with the given value")
        void scalarFactoryCreatesUnitMatrix() {

        }

        @Test
        @DisplayName("ofRows method creates matrix correctly.")
        void ofRowsFactoryMethodCreatesCorrectMatrix() {

        }

        @Test
        @DisplayName("ofColumns method creates matrix correctly.")
        void ofColumnsFactoryMethodCreatesCorrectMatrix() {

        }
    }


    @Nested
    @DisplayName("Matrix Accessors and Mutators.")
    class GetterSetterTest {

    }

    @Nested
    @DisplayName("In-place Matrix Methods.")
    class InPlaceOperationsTest {

    }

    @Nested
    @DisplayName("Out of place Matrix Methods.")
    class OutOfPlaceOperationsTest {

    }

    @Nested
    @DisplayName("Numerical Matrix Methods.")
    class NumericalMethodsTest {

    }

    @Nested
    @DisplayName("Matrix Query Methods.")
    class QueryMethodsTest {

    }

    @Nested
    @DisplayName("Overridden Methods of Object class.")
    class ObjectMethodsTest {

    }
}
