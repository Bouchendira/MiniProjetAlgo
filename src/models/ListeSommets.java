package models;

public class ListeSommets {
    private Sommet val;
    private ListeSommets suivant;

    public  Sommet getVal(){
        return  this.val;
    }
    public  ListeSommets getSuivant(){
        return  this.suivant;
    }
    public void setVal(Sommet val){
        this.val=val;
    }
    public void setSuivant(ListeSommets suivant){
        this.suivant=suivant;
    }

    ListeSommets (Sommet val, ListeSommets suivant) {
        this.val = val;
        this.suivant = suivant;
    }

}