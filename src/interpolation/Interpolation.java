//Christopher Kilian
//CS 301 - Spring 2018
//Programming Project 3 - Interpolating Polynomials

package interpolation;


//Driver class for the interpolating polynomials project
public class Interpolation {

    public static void main(String[] args) {
        //NOTE: To change the test file, simply change the string for the name of the file here - enter a full file path
        //if necessary to reach the file
        double[][] table = DividedDifference.readFromFile("input.txt");

        double[][] dividedDiff = DividedDifference.solveDivided(table);

        //print divided difference table and the unsimplified polynomial produced from it
        DividedDifference.printDiffTable(dividedDiff);
        DividedDifference.printUnsimplifiedPoly(dividedDiff);
        //print simplified version of polynomial
        Polynomial simplified = DividedDifference.getSimplifiedPoly(dividedDiff);
        System.out.println("Simplified polynomial is:");
        System.out.println(simplified.printPoly());
        //print lagrange form of polynomial and the simplification of that
        System.out.println("Printing unsimplified Lagrangian Form:");
        System.out.println(Lagrange.printUnsimpLagrange(table));
        System.out.println("Printing simplified Lagrangian Form polynomial:");
        System.out.println(Lagrange.getSimplifiedLagrangePoly(table).printPoly());
        
    }
    
    
}
