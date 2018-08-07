//Christopher Kilian
//CS 301 - Spring 2018
//Programming Project 3 - Interpolating Polynomials

package interpolation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Class designed to handle the divided difference method of the interpolation project. This class can produce the divided difference table and un-simplified form
//of the interpolating polynomial for some provided set of "x" and "y" values
public class DividedDifference {
    private static final int MIN_FIELD_WIDTH = 10; //minimum field width for printing purposes - field size can grow from this for larger tables (for printing divided difference)
    
    
    //Method which reads in a text file of the given file name in the expected format for an interpolating polynomial
    //(first line x values, second line y values) and produces a 2D array of the expected form needed for other methods of this class (namely
    //the "solveDivided" method).
    public static double[][] readFromFile(String fileName){
        double[][] theTable = new double[0][0];
        List<Double> xVals = new ArrayList<>();
        List<Double> functionalVals = new ArrayList<>();
        
        File check = new File(fileName);
        if(!check.exists()){
            System.out.println("\nFile not found - please check that it exists and try again.");
        }else{
            try {
                FileReader fileReader = new FileReader(fileName);
                BufferedReader reader = new BufferedReader(fileReader);
                String line = null;
                
                for(int i = 0; i < 2; i++){ //file should only contain 2 lines - one for "x" values, and one for "f(x)" values
                    line = reader.readLine();
                    //System.out.println("line reads: " + line);
                    if(line != null){
                        line = line.trim();
                        String[] values = line.split("\\s+");
                        for(String val : values){
                            //System.out.println("current examined val is: " + val);
                            try{
                                if(i == 0){
                                    xVals.add(Double.parseDouble(val));
                                }else{
                                    functionalVals.add(Double.parseDouble(val));
                                }
                            }catch(Exception e){
                                System.out.println("Unable to parse numeric value from file! Skipping value! Unparsable value was: " + val);
                            }
                        } 
                    }else{
                        System.out.println("Error reading from file - format should include 2 lines/rows of data!");
                    }
                }
                
                reader.close();
                fileReader.close();
                
                //place the read-in list values into the table for returning - store column-wise, so "x" and "f(x)" vals stored 1 per row
                if(xVals.size() == functionalVals.size()){
                    //System.out.println("xVals size is " + xVals.size());
                    theTable = new double[xVals.size()][2];
                    for(int i = 0; i < xVals.size(); i++){
                        theTable[i][0] = xVals.get(i);
                        theTable[i][1] = functionalVals.get(i);
                    }
                }else{
                    System.out.println("Problem reading file. Un-equal number of x values and functional values. Returning empty table.");
                } 
            }catch(IOException x){
                System.err.format("IOException: %s%n", x);
            }
        }
        
        return theTable;
    }
    
    
    //Method which handles the processing of a provided set of x/y coordinates into a divided difference table
    public static double[][] solveDivided(double[][] valTable){
        //difference table has as many rows as number of "x" terms, and as many columns as "x" terms plus 1
        double[][] dividedDiffTable = new double[valTable.length][valTable.length + 1]; 
        
        //copy values from value table into divided difference table:
        for(int i = 0; i < valTable.length; i++){
            dividedDiffTable[i][0] = valTable[i][0];
            dividedDiffTable[i][1] = valTable[i][1];
        }
        
        for(int j = 2; j < dividedDiffTable[0].length; j++){
            for(int i = 0; i < (dividedDiffTable[0].length - j); i++){
                dividedDiffTable[i][j] = (dividedDiffTable[i+1][j-1] - dividedDiffTable[i][j-1]) / (dividedDiffTable[i+(j-1)][0] - dividedDiffTable[i][0]);
            }
        }
        
        return dividedDiffTable;
    }
    
    
    //Method to help with printing of contents of a divided difference table - convert numeric table to formatted table of strings.
    //By converting to table of strings, formatting for printing can be handled by row/column position (offset by 2 per row/column)
    //return string table for printing by caller
    public static void printDiffTable(double[][] diffTable){
        String[][] tableToPrint = new String[(diffTable.length * 2)][diffTable[0].length]; //double number of rows to allow for "in-between" values as part of table printing
        
        //initialize empty strings as necessary in table (for ease of printing later)
        int width = MIN_FIELD_WIDTH;
        if(tableToPrint[0].length > MIN_FIELD_WIDTH){
            width = tableToPrint[0].length + 3; //+3 to add buffer space
        }
        for(int i = 0; i < tableToPrint.length; i++){
            for(int j = 0; j < tableToPrint[0].length; j++){
                tableToPrint[i][j] = String.format(("%" + width + "s"), " ");
            }
        }
        
        
        //initialize first two columns with "x" and "f(x)" values
        for(int i = 0; i < diffTable.length; i++){
            tableToPrint[(i*2)][0] = String.format("%-" + width + ".5f", diffTable[i][0]);
            tableToPrint[(i*2)][1] = String.format("%-" + width + ".5f", diffTable[i][1]);
        }
        
        //handle remaining row/column values
        for(int i = 2; i < diffTable[0].length; i++){ //column values - every column iterated over
            //row values - note that as column value increases, rows needing values input decrease by factor of (total # columns) - (current column #)
            //starting row = current column # - 1
            int rowCount = (i - 1);
            for(int j = 0; j < (diffTable[0].length - i); j++){
                tableToPrint[rowCount][i] = String.format("%-" + width + ".5f", diffTable[j][i]);
                rowCount = rowCount + 2;
            } 
        }
        
        outputDividedTable(tableToPrint);
    }
    
    
    //Method which actually prints to command line the result of "printDiffTable" - reads the formatted table and outputs the values appropriately
    private static void outputDividedTable(String[][] dividedDifference){
        //print the top level headers...
        int width = MIN_FIELD_WIDTH;
        if(dividedDifference[0].length > MIN_FIELD_WIDTH){
            width = dividedDifference[0].length + 3; // +3 to add buffer space
        }
        System.out.print(String.format("%-" + width + "s", "x"));
        System.out.print(String.format("%-" + width + "s", "f[]"));
        
        for(int i = 2; i < dividedDifference[0].length; i++){
            StringBuilder label = new StringBuilder();
            label.append("f[");
            for(int j = 0; j <= (i - 2); j++){
                label.append(",");
            }
            label.append("]");
            System.out.print(String.format("%-" + width + "s", label.toString()));
        }
        System.out.print("\n");
        
        //output remainder of table
        for(int i = 0; i < dividedDifference.length; i++){
            for(int j = 0; j < dividedDifference[0].length; j++){
                System.out.print(dividedDifference[i][j]);
            }
            System.out.print("\n");
        }
    }
    
    
    //Method which takes a solved divided difference table and prints out the unsimplified form of the polynomial
    public static void printUnsimplifiedPoly(double[][] diffTable){
        StringBuilder poly = new StringBuilder();
        
        //handle first term
        poly.append(String.format("%.4f", diffTable[0][1]));
        if(diffTable[0].length > 2){
            if(diffTable[0][2] > 0){
                poly.append(" + ");
            }else{
                poly.append(" - ");
            }
        }
        
        for(int i = 1; i < diffTable.length; i++){
            poly.append(String.format("%.4f", Math.abs(diffTable[0][(i+1)])));
            for(int j = 0; j <= (i-1); j++){
                poly.append("(x");
                if(diffTable[j][0] > 0){
                    poly.append(" - ");
                }else if(diffTable[j][0] < 0){
                    poly.append(" + ");
                }
                
                if(diffTable[j][0] != 0){
                    poly.append(Math.abs(diffTable[j][0]));
                }
                poly.append(")");
            }
            
            if((i+1) != diffTable.length){ //check for end of string (no need to add + or - to end of polynomial)
                if(diffTable[0][(i+2)] > 0){
                        poly.append(" + ");
                }else{
                        poly.append(" - ");
                }
            }
        }
        
        System.out.println("Interpolating polynomial is: ");
        System.out.println(poly.toString());
    }
    
    
    //Method which returns the simplified polynomial of the provided solved divided difference table - uses the Polynomial class to accomplish simplification,
    //and returns a Polynomial object for the final simplified result
    public static Polynomial getSimplifiedPoly(double[][] diffTable){
        List<Polynomial> termList = new ArrayList<>(); //each "term" is the next (x-a) multiple - ie, termList[0] is (x-a), termList[1] is (x-a)(x-b), etc (no coefficient multiplication yet)
        double[] coefficients = new double[(diffTable[0].length - 1)];
        double[] xVals = new double[(diffTable.length)];
        
        //extract coefficients and x-vals for ease of access later
        for(int i = 1; i < diffTable[0].length; i++){
            coefficients[(i-1)] = diffTable[0][i]; //first row of divided difference table (starting index 1) holds coefficients for polynomial
        }
        
        for(int i = 0; i < diffTable.length; i++){
            xVals[i] = diffTable[i][0]; //first column of divided difference table holds "x" vals
        }
        
        Polynomial polyBuilder = new Polynomial(1.0, xVals[0]); //build initial polynomial using constructor which creates poly of form (x - a)
        termList.add(new Polynomial(polyBuilder.getCoefficients()));
        for(int i = 1; i < (xVals.length - 1); i++){ //form sub-polynomials by multiplying each previous by (x - xVals[i])
            Polynomial polyToMult = new Polynomial(1.0, xVals[i]); //next term multiplying into polyBuilder
            polyBuilder = polyBuilder.multiply(polyToMult); //perform multiplication
            termList.add(new Polynomial(polyBuilder.getCoefficients())); //add new term to the list
        }
        
        Polynomial result = new Polynomial(coefficients[0], 0); //start building final term as a polynomial of only the x^0 term
        for(int i = 1; i < coefficients.length; i++){
            Polynomial term = termList.get((i-1));
            term = term.multiplyByConst(coefficients[i]);
            result = result.add(term);
        }
        
        return result;
    }
    
    
}
