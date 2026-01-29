public class MatrixDriver {

    public static void main(String[] args) {
        //tests ofRows method and toString
        Matrix A = Matrix.ofRows(new double[]{1,2,3}, new double[]{4,5,6}, new double[]{7,8,9});

        //tests ofColumns method and indirectly transpose method as well
        double[] c1 = {1.3435, 2, 4.249, 7.838};
        double[] c2 = {1.245, 58.982, 92, 188.338382};
        Matrix B = Matrix.ofColumns(c1, c2);

        //tests toString()
        System.out.println(A);
        System.out.println(B);

        Matrix C = new Matrix(3,3);
        System.out.println(C);

        Matrix I3 = Matrix.createIdentityMatrix(3);
        System.out.println(I3);

        Matrix S = Matrix.constant(4,5,1.294);
        System.out.println(S);

        Matrix Sc = Matrix.createScalarMatrix(3, 67676);
        System.out.println(Sc);

        //tests grid constructor
        double[][] grid = {{1,2,3,4}, {1,32,2,0}, {1,3,5,2}, {111199999, 8848484, 22222292, 676767676}};
        System.out.println(new Matrix(grid));

        //tests fillInPlace and nullMatrixInPlace methods
        double[][] grid2 = {{1,2,3,4}, {1,32,2,0}, {3.535, -199.03, -101, 6.0}};
        Matrix G2 = new Matrix(grid2);
        System.out.println("before fillInPlace:\n" + G2);
        G2.nullMatrixInPlace();
        System.out.println("after fillInPlace:\n" + G2);

        //tests setEntry methods
        Matrix SE = new Matrix(10,20);
        for (int i = 0; i < SE.getRows(); i++) {
            for (int j = 0; j < SE.getColumns(); j++) {
                SE.setEntry(i * i + j * j, i, j);
            }
        }

        System.out.println(SE);

        //tests matrix addition


        //tests handling of jagged input
        double[][] jaggedGrid = {{1,2,3,4}, {1,32,2,0}, {1,3,5,2,7}, {111199999, 8848484, 22222292, 676767676}};
        Matrix X = new Matrix(jaggedGrid);
        System.out.println(X);
    }
}
