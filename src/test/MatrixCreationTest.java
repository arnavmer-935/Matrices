package test;

import main.Matrix;
import main.MatrixException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixCreationTest {

    private static final double TOLERANCE = 1e-6;

    @Test
    @DisplayName("Row/column constructor creates zero-filled matrix with correct dimensions")
    void constructorWithRowsAndColsCreatesZeroMatrix() {
        Matrix A = new Matrix(2,3);

        assertEquals(2, A.getRows());
        assertEquals(3, A.getColumns());

        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < A.getColumns(); j++) {
                assertEquals(0.0, A.getEntry(i,j), TOLERANCE);
            }
        }
    }

    @Test
    @DisplayName("Square constructor creates matrix with equal row and column count")
    void squareConstructorCreatesCorrectDimensions() {

        Matrix squareMatrix = new Matrix(3);
        assertEquals(squareMatrix.getColumns(), squareMatrix.getRows());
    }

    @Test
    @DisplayName("Constructor rejects negative or zero dimensions")
    void constructorRejectsInvalidDimensions() {

        assertThrows(MatrixException.class, () -> new Matrix(-2,-3));
        assertThrows(MatrixException.class, () -> new Matrix(-5));
        assertThrows(MatrixException.class, () -> new Matrix(0));

    }

    @Test
    @DisplayName("Constructor from 2D array performs deep copy")
    void constructorFrom2DArrayPerformsDeepCopy() {

        double[][] matrixArray = {{1,2,3}, {4,5,6}, {7,8,9}};
        Matrix matrixFrom2dArray = new Matrix(matrixArray);

        matrixArray[0][0] = 67;
        assertEquals(1.0, matrixFrom2dArray.getEntry(0,0), TOLERANCE);
    }

    @Test
    @DisplayName("Constructor rejects null arrays")
    void constructorRejectsNullArrays() {
        double[][] nullGrid = null;
        assertThrows(IllegalArgumentException.class, () -> new Matrix(nullGrid));
    }

    @Test
    @DisplayName("Constructor rejects jagged 2D arrays")
    void constructorRejectsJaggedArrays() {

        double[][] jaggedGrid = {{1,2},{3,4},{5,6,7},{4,5}};
        assertThrows(MatrixException.class, () -> new Matrix(jaggedGrid));
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
