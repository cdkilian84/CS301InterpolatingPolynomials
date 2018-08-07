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

//Class designed to handle the Lagrange form of output for the interpolation project
public class Lagrange {
    
    //Method which reads in a text file of the given file name in the expected format for an interpolating polynomial
    //(first line x values, second line y values) and produces a 2D array of the expected form needed for other methods of this class.
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
    
    
    //Method which prints the unsimplified Lagrange form for the interpolating polynomial. Requires a properly formatted input table of the form generated
    //by the "readFromFile" method of this class. Outputs a string representation of the unsimplified Lagrangian interpolating polynomial.
    public static String printUnsimpLagrange(double[][] valTable){
        StringBuilder output = new StringBuilder();
        StringBuilder lagrangeVal;
        
        for(int i = 0; i < valTable.length; i++){
            lagrangeVal = new StringBuilder();
            if(i == 0){
                lagrangeVal.append(String.format("%.4f", valTable[i][1])).append(" * ( "); //get the functional value the lagrangian value is multiplied by
            }else{
                lagrangeVal.append(String.format("%.4f", Math.abs(valTable[i][1]))).append(" * ( ");
            }
            for(int j = 0; j < valTable.length; j++){ //build numerator of lagrange value
                if(i != j){
                    lagrangeVal.append("(x");
                    if(valTable[j][0] > 0){
                        lagrangeVal.append(" - ").append(String.format("%.4f", valTable[j][0])).append(")");
                    }else if(valTable[j][0] < 0){
                        lagrangeVal.append(" + ").append(String.format("%.4f", Math.abs(valTable[j][0]))).append(")");
                    }else{
                        lagrangeVal.append(")"); //if value is zero, just close parenthesis
                    }
                }
            }
            lagrangeVal.append(" / ");
            for(int j = 0; j < valTable.length; j++){ //build denominator of lagrange value
                if(i != j){
                    lagrangeVal.append("(").append(String.format("%.4f", valTable[i][0]));
                    if(valTable[j][0] > 0){
                        lagrangeVal.append(" - ").append(String.format("%.4f", valTable[j][0])).append(")");
                    }else if(valTable[j][0] < 0){
                        lagrangeVal.append(" + ").append(String.format("%.4f", Math.abs(valTable[j][0]))).append(")");
                    }else{
                        lagrangeVal.append(")"); //if value is zero, just close parenthesis
                    }
                }
            }
            lagrangeVal.append(" )");
            
            output.append(lagrangeVal.toString());
            if((i+1) != valTable.length){ //check for end of loop
                if(valTable[(i+1)][1] >= 0){
                    output.append(" + ");
                }else{
                    output.append(" - ");
                }
            }
        }
        
        return output.toString();
    }
    
    
    //Method which returns a Polynomial object which holds the simplified value of the Lagrangian polynomial representation
    public static Polynomial getSimplifiedLagrangePoly(double[][] valTable){
        Polynomial simplified = null;
        
        for(int i = 0; i < valTable.length; i++){
            Polynomial lagrange = null;
            double multiplier = valTable[i][1]; //get f(xi) value as starting val for multiplier
            for(int j = 0; j < valTable.length; j++){ //"numerator" of lagrange polynomial is the actual polynomial value (later to be multiplied by functional value and "denominator"
                if(i != j){
                    if(lagrange == null){
                        lagrange = new Polynomial(1.0, valTable[j][0]);
                    }else{
                        Polynomial nextTerm = new Polynomial(1.0, valTable[j][0]);
                        lagrange = lagrange.multiply(nextTerm);
                    }
                    multiplier = multiplier * (1.0 / (valTable[i][0] - valTable[j][0])); //multiplier for polynomial is "denominator" of lagrange times the functional value
                }
            }
            lagrange = lagrange.multiplyByConst(multiplier);
            if(simplified == null){ //first lagrangian term
                simplified = new Polynomial(lagrange.getCoefficients());
            }else{
                simplified = simplified.add(lagrange);
            }
        }
        
        return simplified;
    }
    
}
