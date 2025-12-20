import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix {

    private record Pair(int x, int y) {};

    private int rows;
    private int columns;
    private Pair order;
    private double[][] numbers;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;
        this.order = new Pair(rows, cols);
        this.numbers = new double[this.rows][this.columns];
    }

    public Matrix(double[][] grid) {
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


    //Consturcotr for square matrix
    public Matrix(int rows) {
        this.rows = rows;
        this.columns = rows;
        this.order = new Pair(rows, rows);
        this.numbers = new double[this.rows][this.rows];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Pair getOrder() { return this.order; }

    public double[][] getEntries() { return this.numbers; }

    private boolean isInBounds(int r, int c) {
        return 0 <= r && r < this.rows && 0 <= c && c < this.columns;
    }

    private double getValue(int r, int c) {
        if (isInBounds(r, c)) {
            return this.numbers[r][c];
        }
        return -1;
    }

    private void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }

    private void swap(double a, double b) {
        double temp = a;
        a = b;
        b = temp;
    }

    private void reverseRow(double[] row) {
        for (int i = 0; i < row.length; i++) {
            swap(row[i], row[row.length - i - 1]);
        }
    }

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
                    if (this.getEntries()[r][c] != 1) {
                        return false;
                    }
                } else {
                    if (this.numbers[r][c] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //includes diagonal as well
    private ArrayList<Double> getUpperTriangle() {
        ArrayList<Double> result = new ArrayList<>();
        if (this.isSquareMatrix()) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    if (i >= j) {
                        result.add(this.getEntries()[i][j]);
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<Double> getLowerTriangle() {
        ArrayList<Double> result = new ArrayList<>();
        if (this.isSquareMatrix()) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    if (i <= j) {
                        result.add(this.getEntries()[i][j]);
                    }
                }
            }
        }
        return result;
    }

    public boolean isUpperTriangular() {
        for (Double x : this.getLowerTriangle()) {
            if (x != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isLowerTriangular() {
        for (Double x : this.getUpperTriangle()) {
            if (x != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllEqual(double k) {
        for (double[] row : this.numbers) {
            for (double n : row) {
                if (n != k) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isNullMatrix() {
        return this.areAllEqual(0);
    }

    private boolean isConstantMatrix() {
        return this.areAllEqual(this.getValue(0,0));
    }

    private static Matrix createNullMatrix(int nrows, int ncols) {
        double[][] result = new double[nrows][ncols];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                result[i][j] = 0;
            }
        }
        return new Matrix(result);
    }

    private static Matrix createIdentityMatrix(int nrows) {
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

    public Matrix multiplyByScalar(double k) {
        if (k == 0) {
            return createNullMatrix(this.getRows(), this.getColumns());
        }

        if (k == 1) {
            return this;
        }

        else {
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < this.getColumns(); j++) {
                    this.numbers[i][j] *= k;
                }
            }
        }
        return this;
    }

    public void add(Matrix other) throws MatrixException{
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

    public void subtract(Matrix other) {
        this.add(other.multiplyByScalar(-1)); //subtraction is the same as adding to -ve
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

    private boolean equalsMatrix(Matrix other) {
        if (this == other) return true;
        if (this == null || other == null || !this.getOrder().equals(other.getOrder())) return false;

        for (int r = 0; r < this.getRows(); r++) {
            for (int c = 0; c < this.getColumns(); c++) {
                if (this.numbers[r][c] != other.numbers[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean preceeds(Matrix other) {
        if (this.getOrder().equals(other.getOrder())) {
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < this.getColumns(); j++) {
                    if (this.numbers[i][j] > other.numbers[i][j]) {
                        return false;
                    }
                }
            }
            return true;
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

    public Matrix transpose() {
        double[][] result = new double[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                result[j][i] = this.getEntries()[i][j];
            }
        }
        return new Matrix(result);
    }

    public void rotation(Matrix m, int kSteps) {
        //rotation = transposition + row reversal

        kSteps %= 4; //rotating 4 times results in same matrix

        for (int i = 0; i < kSteps; i++) {
            m = m.transpose();
            for (double[] r : m.getEntries()) {
                reverseRow(r);
            }
        }
    }

    public boolean isSingular(Matrix m) {
        return determinant(m) == 0;
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

    public double[][] populateJaggedMatrix(double[][] matrix) {
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

    private static boolean isJaggedMatrix(int[][] a) {
        int firstRowLength = a[0].length;
        for (int i = 1; i < a.length; i++) {
            if (firstRowLength != a[i].length) {
                return true;
            }
        }
        return false;
    }
}

