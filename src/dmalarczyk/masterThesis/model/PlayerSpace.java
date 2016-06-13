package dmalarczyk.masterThesis.model;

import java.util.ArrayList;

public class PlayerSpace {

    public ArrayList<CardType> discardedDeck;
    public ArrayList<CardType> hand;
    public boolean safetyFlag;

    public PlayerSpace(){
        discardedDeck = new ArrayList<>();
        hand= new ArrayList<>();
        safetyFlag = false;
    }
}
