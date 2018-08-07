//Christopher Kilian
//CS 301 - Spring 2018
//Programming Project 3 - Interpolating Polynomials

package interpolation;

//Polynomial class designed to represent a simplified linear polynomial. Coefficient values are stored as an array, with each index value of the array
//representing the degree of that coefficient (so for index i, each coefficient is times x^i). The zero index is for the x^0 term and so on.
//This class supports addition, subtraction, and multiplication operations with other polynomials. It will also output a formatted string with the representation
//of the simplified polynomial.
public class Polynomial {
    private double[] coefficients;
    private int degree;
    
    //constructor which accepts an array of ordered coefficients, with index values corresponding to x^index and stored values the coefficients themselves
    public Polynomial(double[] coefficients){
        this.coefficients = new double[coefficients.length];
        for(int i = 0; i < coefficients.length; i++){
            this.coefficients[i] = coefficients[i];
        }
        findDegree();
    }
    
    //constructor which allows creation of a polynomial of the form ax^b, with "a" as the coefficient and "b" as the degree
    public Polynomial(Double coefficient, Integer degree){
        this.coefficients = new double[(degree + 1)];
        this.coefficients[degree] = coefficient;
        this.degree = degree;
    }
    
    //constuctor allowing the creation of first degree polynomials of the form (ax-b)
    public Polynomial(Double a, Double b){
        this.coefficients = new double[2];
        this.coefficients[0] = (-1 * b);
        this.coefficients[1] = a;
        this.degree = 1;
    }
    
    //Method which sets the stored degree value for the Polynomial by ignoring leading 0 values
    private void findDegree(){
        for(int i = (this.coefficients.length - 1); i >= 0; i--){
            if(this.coefficients[i] != 0){
                this.degree = i;
                break;
            }
        }
    }
    
    
    //Method which adds this polynomial to the provided polynomial and returns the resulting polynomial
    public Polynomial add(Polynomial polyToAdd){
        double[] otherCoeff = polyToAdd.getCoefficients();
        double[] addedVals;
        
        if(this.degree >= polyToAdd.getDegree()){
            addedVals = new double[(this.degree + 1)];//store new coeffecients for new polynomial
        }else{
            addedVals = new double[(polyToAdd.getDegree() + 1)];//store new coeffecients for new polynomial
        }
        
        //add "other" coefficient values to addedVals array for new polynomial
        for(int i = polyToAdd.getDegree(); i >= 0; i--){
            addedVals[i] += otherCoeff[i];
        }
        
        //add "this" poly's coefficient values
        for(int i = this.degree; i >= 0; i--){
            addedVals[i] += this.coefficients[i];
        }
        
        return (new Polynomial(addedVals));
    }
    
    
    //Method which subtracts the provided polynomial from this polynomial and returns the resulting polynomial
    public Polynomial subtract(Polynomial polyToAdd){
        double[] otherCoeff = polyToAdd.getCoefficients();
        double[] subVals;
        
        if(this.degree >= polyToAdd.getDegree()){
            subVals = new double[(this.degree + 1)];//store new coeffecients for new polynomial
        }else{
            subVals = new double[(polyToAdd.getDegree() + 1)];//store new coeffecients for new polynomial
        }
        
        //put "this" polynomials coefficients into the new array
        for(int i = this.degree; i >= 0; i--){
            subVals[i] += this.coefficients[i];
        }
        
        //subtract "other" coefficient values from addedVals array for new polynomial
        for(int i = polyToAdd.getDegree(); i >= 0; i--){
            subVals[i] -= otherCoeff[i];
        }
        
        return (new Polynomial(subVals));
    }
    
    
    //Method to multiply two polynomials and return the resulting polynomial
    public Polynomial multiply(Polynomial polyToMult){
        double[] multCoefficients = new double[(this.degree + polyToMult.getDegree() + 1)]; //degree of new polynomial will be addition of degrees of the two polys being multiplied
        double[] otherPolyCoef = polyToMult.getCoefficients();
        
        for (int i = 0; i <= this.degree; i++){
            for (int j = 0; j <= polyToMult.getDegree(); j++){
                multCoefficients[i+j] += (otherPolyCoef[j] * this.coefficients[i]);
            }
        }
        
        return (new Polynomial(multCoefficients));
    }
    
    
    //Method to multiply a polynomial by a constant value
    public Polynomial multiplyByConst(double val){
        double[] multCoefficients = new double[(this.degree + 1)]; //degree of new polynomial will be addition of degrees of the two polys being multiplied
        
        for (int i = 0; i <= this.degree; i++){
            multCoefficients[i] = (val * this.coefficients[i]);
        }
        
        return (new Polynomial(multCoefficients));
    }
    
    
    //Method to print the polynomial
    public String printPoly(){
        StringBuilder thePoly = new StringBuilder();
        
        if(this.degree == 0){
            thePoly.append(String.format("%.4f", this.coefficients[0]));
        }else if(this.degree == 1){
            thePoly.append(String.format("%.4f", this.coefficients[1])).append("x");
            if(this.coefficients[0] > 0){
                thePoly.append(" + ").append(String.format("%.4f", this.coefficients[0]));
            }else if(this.coefficients[0] < 0){
                thePoly.append(" - ").append(String.format("%.4f", Math.abs(this.coefficients[0])));
            }
            //note if second coefficient is equal to zero, don't add anything!
        }else{
            //anything greater than degree 1
            thePoly.append(String.format("%.4f", this.coefficients[this.degree])).append("x^").append(this.degree); //get first term
            for(int i = degree - 1; i >= 0; i--){
                //get next coefficient value
                if(this.coefficients[i] > 0){
                    thePoly.append(" + ").append(String.format("%.4f", this.coefficients[i]));
                }else if(this.coefficients[i] < 0){
                    thePoly.append(" - ").append(String.format("%.4f", Math.abs(this.coefficients[i])));
                }else{
                    continue; //skip zero value coefficients
                }
                
                if(i > 1){
                    thePoly.append("x^").append(i); //for degree > 1, add the power to the x value
                }else if(i == 1){
                    thePoly.append("x");
                }
                //no x added for i == 0
            }
        }
        
        return thePoly.toString();
    }
    
    //getter for coefficients list
    public double[] getCoefficients(){
        return this.coefficients;
    }
    
    //getter for the degree
    public int getDegree(){
        return this.degree;
    }
    
}
