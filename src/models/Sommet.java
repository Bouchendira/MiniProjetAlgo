package models;

import java.util.Objects;

public class Sommet {
    private int i,j;
    private char c;
    public  int getI(){
        return  this.i;
    }
    public  int getJ(){
        return  this.j;
    }
    public  char getC(){
        return  this.c;
    }
    public void setI(int i){
        this.i=i;
    }
    public void setJ(int j){
        this.j = j;
    }
    public void setC(char c){
        this.c = c;
    }

    public Sommet (int i, int j,char c) {
        this.i = i;
        this.j = j;
        this.c = c;
    }



    public String toString() {
        return ("<" + i + ", " + j +", " + c + ">") ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
