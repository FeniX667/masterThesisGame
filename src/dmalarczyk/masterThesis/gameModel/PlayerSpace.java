package dmalarczyk.masterThesis.gameModel;

import java.util.ArrayList;

public class PlayerSpace {

    public ArrayList<CardType> discardedDeck;
    public ArrayList<CardType> hand;
    public boolean isSafe;
    public CardType knownEnemyCard;

    public PlayerSpace(){
        discardedDeck = new ArrayList<>();
        hand= new ArrayList<>();
        isSafe = false;
        knownEnemyCard = null;
    }

    public PlayerSpace clone(){
        PlayerSpace randomSpace = new PlayerSpace();
        randomSpace.discardedDeck = new ArrayList<>();
        randomSpace.discardedDeck.addAll(this.discardedDeck);
        randomSpace.isSafe = this.isSafe;
        randomSpace.knownEnemyCard = this.knownEnemyCard;
        randomSpace.hand = new ArrayList<>();
        randomSpace.hand.addAll(this.hand);
        return randomSpace;
    }

    public CardType getTheOtherCardInHand(CardType firstCard){
        ArrayList<CardType> tmpHand = new ArrayList<>(hand);
        tmpHand.remove(firstCard);
        return tmpHand.get(0);
    }
}
