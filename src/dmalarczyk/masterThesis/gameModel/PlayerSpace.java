package dmalarczyk.masterThesis.gameModel;

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

    public PlayerSpace(PlayerSpace spaceOfPlayerA) {
        discardedDeck = new ArrayList<>(spaceOfPlayerA.discardedDeck);
        hand = new ArrayList<>(spaceOfPlayerA.hand);
        safetyFlag = spaceOfPlayerA.safetyFlag;
    }

    public PlayerSpace hiddenClone(CardType randomCard, PlayerSpace playerSpace){
        PlayerSpace randomSpace = new PlayerSpace();
        randomSpace.discardedDeck = new ArrayList<>(playerSpace.discardedDeck);
        randomSpace.safetyFlag = playerSpace.safetyFlag;
        randomSpace.hand = new ArrayList<>();
        randomSpace.hand.add(randomCard);
        return randomSpace;
    }
}
