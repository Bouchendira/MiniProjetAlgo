package models;

class ListeSommets {
    Sommet val;
    ListeSommets suivant;

    ListeSommets (Sommet val, ListeSommets suivant) {
        this.val = val;
        this.suivant = suivant;
    }

}