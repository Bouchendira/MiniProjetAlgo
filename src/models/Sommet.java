package models;

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
<<<<<<< Updated upstream
=======
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sommet sommet = (Sommet) obj;
        return i == sommet.i && j == sommet.j;
    }
    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
>>>>>>> Stashed changes
}
