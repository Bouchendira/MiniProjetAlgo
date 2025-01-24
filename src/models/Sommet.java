package models;

class Sommet {
    int i,j;
    char c;

    Sommet (int i, int j,char c) {
        this.i = i;
        this.j = j;
        this.c = c;
    }



    public String toString() {
        return ("<" + i + ", " + j +", " + c + ">") ;
    }
}
