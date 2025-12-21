import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix {

    // ==== INSTANCE VARIABLES AND DECLARATIONS ====
    private record Pair(int x, int y) {
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    };
    private static final double TOLERANCE = 1e-6;
    private int rows;
    private int columns;
    private Pair order;
    private double[][] numbers;


    // ==== MATRIX CREATION METHODS ====
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;
        this.order = new Pair(rows, cols);
        this.numbers = new double[this.rows][this.columns];
    }

    public Matrix(double[][] grid) throws MatrixException {
        if (!isJaggedMatrix(grid)) {
            throw new MatrixException("Uneven number of entries in each row.");
        }
        this.numbers = grid;
    }

    public Matrix(List<List<Double>> grid) {
        this.rows = grid.size();
        this.columns = grid.get(0).size();
        this.order = new Pair(this.rows, this.columns);

        this.numbers = new double[grid.size()][grid.get(0).size()];
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                this.numbers[i][j] = grid.get(i).get(j);
            }
        }
    }

    //Constructor for square matrix
    public Matrix(int rows) {
        this.rows = rows;
        this.columns = rows;
        this.order = new Pair(rows, rows);
        this.numbers = new double[this.rows][this.rows];
    }

    public Matrix createConstantMatrix(int nrows, int ncols, double k) {
        double[][] res = new Matrix(nrows, ncols).getEntries();
        for (int r = 0; r < nrows; r++) {
            Arrays.fill(res[r], k);
        }
        return new Matrix(res);
    }

    public Matrix createNullMatrix(int nrows, int ncols) {
        return createConstantMatrix(nrows, ncols, 0.0);
    }

    public void nullMatrixInPlace() {
        fillInPlace(0.0);
    }

    public Matrix createIdentityMatrix(int nrows) {
        double[][] result = new double[nrows][nrows];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < nrows; j++) {
                if (i == j) {
                    result[i][j] = 1;
                } else {
                    result[i][j] = 0;
                }
            }
        }
        return new Matrix(result);
    }

    // ==== ACCESSOR AND MUTATOR METHODS ====
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Pair getOrder() { return this.order; }

    public double[][] getEntries() { return this.numbers; }

    public void setEntry(double value, int r, int c) {
        if (!isInBounds(r,c)) {
            throw new MatrixException("Matrix coordinates must be in bounds");
        }
        this.numbers[r][c] = value;
    }

    public void setRow(double[] row, int rowIndex) {
        if (row.length != columns) {
            throw new MatrixException("Row length does not match the number of columns in the matrix.");

        }

        if (!rowInRange(rowIndex)) {
            throw new MatrixException(String.format("Row index %d is out of bounds for order %s", rowIndex, this.order));
        }

        this.numbers[rowIndex] = row.clone();
    }

    public void setColumn(double[] col, int colIndex) {
        if (col.length != rows) {
            throw new MatrixException("Column length does not match the number of rows in the matrix.");

        }

        if (!colInRange(colIndex)) {
            throw new MatrixException(String.format("Column index %d is out of bounds for order %s", colIndex, this.order));
        }

        for (int i = 0; i < rows; i++) {
            this.numbers[i][colIndex] = col[i];
        }
    }

    // ==== IN-PLACE OPERATIONS ====
    public void multiplyByScalarInPlace(double k) {
        if (k == 1) {
            return;
        }

        if (k == 0) {
            this.nullMatrixInPlace();
            return;
        }

        else {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    numbers[i][j] *= k;
                }
            }
        }
    }

    public void addInPlace(Matrix other) throws MatrixException{
        if (!this.getOrder().equals(other.getOrder())) {
            throw new MatrixException("Orders of both matrices must be the same.");

        } else {
            for (int r = 0; r < this.rows; r++) {
                for (int c = 0; c < this.columns; c++) {
                    this.numbers[r][c] += other.getValue(r, c);
                }
            }
        }
    }

    public void subtractInPlace(Matrix other) {
        this.addInPlace(other.multiplyByScalar(-1)); //subtraction is the same as adding to -ve
    }

    public void transposeInPlace() {
        if (!this.isSquareMatrix()) {
            throw new MatrixException("Non-square matrices cannot be transposed in place");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = i+1; j < columns; j++) {
                swap(numbers[i][j], numbers[j][i]);
            }
        }
    }

    public void rotation(Matrix m, int kSteps) {
        //rotation = transposition + row reversal

        kSteps %= 4; //rotating 4 times results in same matrix

        for (int i = 0; i < kSteps; i++) {
            m = transpose(m);
            for (double[] r : m.getEntries()) {
                reverseRow(r);
            }
        }
    }

    // ==== OUT OF PLACE OPERATIONS ====
    public Matrix multiplyByScalar(double k) {
        if (k == 0) {
            return createNullMatrix(this.rows, this.columns);
        }

        if (k == 1) {
            return this;
        }

        else {
            double[][] result = new double[this.rows][this.columns];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    result[i][j] = this.getValue(i,j) * k;
                }
            }
            return new Matrix(result);
        }
    }

    public Matrix add(Matrix other) throws MatrixException {
        if (!this.getOrder().equals(other.getOrder())) {
            throw new MatrixException("Orders of both matrices must be the same.");

        } else {
            double[][] result = new double[other.rows][other.columns];
            for (int r = 0; r < result.length; r++) {
                for (int c = 0; c < result[0].length; c++) {
                    result[r][c] = this.getValue(r, c) + other.getValue(r, c);
                }
            }
            return new Matrix(result);
        }
    }

    public Matrix subtract(Matrix other) {
        return add(other.multiplyByScalar(-1));
    }

    public Matrix transpose(Matrix matrix) { //used for transposing rectangular matrices
        double[][] transposedGrid = new double[matrix.getColumns()][matrix.getRows()];
        double[][] original = matrix.getEntries();

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                transposedGrid[j][i] = original[i][j];
            }
        }
        return new Matrix(transposedGrid);
    }


    public Matrix multiply(Matrix other) {
        if (this.columns != other.rows) {
            throw new MatrixException("Product is not defined");
        }

        if (this.isNullMatrix() || other.isNullMatrix()) {
            return createNullMatrix(this.rows, other.columns);
        }

        double[][] product = new double[this.rows][other.columns];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                double[] jthColumn = getColumn(j);
                product[i][j] = dotProduct(this.numbers[i], jthColumn);
            }
        }

        return new Matrix(product);
    }

    public Matrix inverse(Matrix m) {
        if (isSingular(m)) {
            throw new MatrixException("Inverse of a singular Matrix is not defined.");
        }

        int nrows = m.getRows();
        int ncols = m.getColumns();
        double[][] adjoint = new double [nrows][ncols];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; ++j) {
                adjoint[j][i] = determinant(getCofactorMatrix(i, j));
            }
        }

        Matrix adj = new Matrix(adjoint);
        return adj.multiplyByScalar(Math.pow(determinant(m), -1));
    }

    // ==== NUMERICAL METHODS ====
    //TODO: Optimize using row reduction
    public double determinant(Matrix m) {
        if (!m.isSquareMatrix()) {
            return 0;
        }

        if (m.getOrder().equals(new Pair(1,1))) { //single element matrix
            return this.numbers[0][0];
        }

        if (m.getOrder().equals(new Pair(2,2))) { //2x2 square matrix
            return (m.getValue(0,0) * m.getValue(1,1)) - (m.getValue(0,1) * m.getValue(1,0));
        }

        double[] topRow = m.numbers[0];
        double det = 0;
        for (int j = 0; j < topRow.length; j++) {
            det += ((int)Math.pow(-1, j) * getValue(0,j) * determinant(m.getCofactorMatrix(0,j)));
            //determinant of a matrix is sum of products of elements in a row/column times that element's cofactor
            //cofactor = -1^(i+j) * determinant of the minor of
        }
        return det;
    }

    // ==== QUERY METHODS ====
    public boolean isSquareMatrix() {
        return this.rows == this.columns;
    }

    public boolean isIdentityMatrix() {
        if (!this.isSquareMatrix()) {
            return false;
        }

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                if (r == c) { //diagonal element
                    if (!almostEqual(numbers[r][c], 1.0)) {
                        return false;
                    }
                } else {
                    if (!almostEqual(numbers[r][c], 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public boolean isUpperTriangular() {
        for (Double x : this.getLowerTriangle()) {
            if ((!almostEqual(x, 0.0))) {
                return false;
            }
        }
        return true;
    }

    public boolean isLowerTriangular() {
        for (Double x : this.getUpperTriangle()) {
            if (!almostEqual(x,0.0)) {
                return false;
            }
        }
        return true;
    }

    public boolean isNullMatrix() {
        return this.areAllEqual(0.0);
    }

    public boolean isConstantMatrix() {
        return this.areAllEqual(this.getValue(0,0));
    }

    public boolean isSingular(Matrix m) {
        return almostEqual(determinant(m), 0.0);
    }

    public boolean preceeds(Matrix other) {
        if (this.getOrder().equals(other.getOrder())) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (this.numbers[i][j] > other.numbers[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean isSymmetric() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = i+1; j < this.columns; j++) {
                if (!almostEqual(numbers[i][j], numbers[j][i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSkewSymmetric() {
        if (!onlyZeroesInDiagonal()) {
            return false;
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = i+1; j < this.columns; j++) {
                if (!almostEqual(numbers[i][j], -numbers[j][i])) {
                    return false;
                }
            }
        }
        return true;
    }


    // ==== HELPER METHODS ====
    private boolean isInBounds(int r, int c) {
        return 0 <= r && r < this.rows && 0 <= c && c < this.columns;
    }

    private double getValue(int r, int c) {
        if (isInBounds(r, c)) {
            return this.numbers[r][c];
        }
        return -1;
    }

    private static boolean almostEqual(double a, double b) {
        return Math.abs(a-b) <= TOLERANCE;
    }

    private void swap(double a, double b) {
        double temp = a;
        a = b;
        b = temp;
    }

    private void reverseRow(double[] row) {
        int n = row.length;
        for (int i = 0; i < n / 2; i++) {
            swap(row[i], row[n - i - 1]);
        }
    }

    private void fillInPlace(double k) {
        for (int i = 0; i < this.rows; i++) {
            Arrays.fill(numbers[i], k);
        }
    }

    private ArrayList<Double> getLowerTriangle() {
        ArrayList<Double> result = new ArrayList<>();
        if (this.isSquareMatrix()) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    if (i <= j) {
                        result.add(numbers[i][j]);
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<Double> getUpperTriangle() {
        ArrayList<Double> result = new ArrayList<>(); //includes diagonal as well
        if (this.isSquareMatrix()) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    if (i >= j) {
                        result.add(numbers[i][j]);
                    }
                }
            }
        }
        return result;
    }

    private boolean areAllEqual(double k) {
        for (double[] row : this.numbers) {
            for (double n : row) {
                if (!almostEqual(n, k)) {
                    return false;
                }
            }
        }
        return true;
    }

    private double dotProduct(double[] arr1, double[] arr2) {
        assert arr1.length == arr2.length;

        double res = 0;
        for (int i = 0; i < arr1.length; i++) {
            res += (arr1[i] * arr2[i]);
        }
        return res;
    }

    private double[] getColumn(int n) {
        double[] col = new double[this.rows];
        for (int i = 0; i < this.rows; i++) {
            col[i] = this.getValue(i, n);
        }
        return col;
    }

    private boolean isJaggedMatrix(double[][] a) {
        int firstRowLength = a[0].length;
        for (int i = 1; i < a.length; i++) {
            if (firstRowLength != a[i].length) {
                return true;
            }
        }
        return false;
    }

    private Matrix getCofactorMatrix(int mthRow, int nthCol) {
        if (!isInBounds(mthRow, nthCol)) {
            throw new MatrixException("Value out of bounds.");
        }

        List<List<Double>> gridList = new ArrayList<>(); //convert to arraylist grid for easier deletion of row and column
        for (double[] row : this.numbers) {
            gridList.add(Arrays.stream(row).boxed().collect(Collectors.toList()));
        }

        gridList.forEach(rowList -> {
            rowList.remove(nthCol);
        });

        gridList.remove(mthRow);

        return new Matrix(gridList);
    }

    private double[][] populateJaggedMatrix(double[][] matrix) {
        double[] longestRow = {};
        for (double[] row : matrix) {
            if (longestRow.length < row.length) {
                longestRow = row;
            }
        }

        double[][] populatedMatrix = new double[matrix.length][longestRow.length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, populatedMatrix[i], 0, matrix[i].length);
        }

        return populatedMatrix;
    }

    private boolean onlyZeroesInDiagonal() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (i == j) {
                    if (!almostEqual(numbers[i][j], 0.0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean equalsMatrix(Matrix other) {
        if (this == other) return true;
        if (this == null || other == null || !this.getOrder().equals(other.getOrder())) return false;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (!almostEqual(this.numbers[i][j], other.numbers[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean rowInRange(int rowIndex) {
        return 0 <= rowIndex && rowIndex < this.rows;
    }

    private boolean colInRange(int colIndex) {
        return 0 <= colIndex && colIndex < this.columns;
    }

    // ==== OBJECT METHODS ====

    @Override
    public String toString() {
        String str = "";
        for (double[] row : this.numbers) {
            for (double val : row) {
                str += String.format("%.3f ", val);
            }
            str += "\n";
        }
        return str;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.numbers);
    }

    @Override
    public boolean equals(Object other) {
        boolean flag;
        switch (other) {
            case Matrix o -> flag = this.equalsMatrix(o);
            default -> {
                flag = false;
            }
        }
        return flag;
    }

}

