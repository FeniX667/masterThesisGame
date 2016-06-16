package dmalarczyk.masterThesis.gameModel;

import java.util.ArrayList;

public class PlayerSpace {

    public ArrayList<CardType> discardedDeck;
    public ArrayList<CardType> hand;
    public boolean isSafe;

    public PlayerSpace(){
        discardedDeck = new ArrayList<>();
        hand= new ArrayList<>();
        isSafe = false;
    }

    public PlayerSpace(PlayerSpace playerSpace) {
        discardedDeck = new ArrayList<>(playerSpace.discardedDeck);
        hand = new ArrayList(playerSpace.hand);
        isSafe = playerSpace.isSafe;
    }

    public PlayerSpace hiddenClone(CardType randomCard, PlayerSpace playerSpace){
        PlayerSpace randomSpace = new PlayerSpace();
        randomSpace.discardedDeck = new ArrayList<>(playerSpace.discardedDeck);
        randomSpace.isSafe = playerSpace.isSafe;
        randomSpace.hand = new ArrayList<>();
        randomSpace.hand.add(randomCard);
        return randomSpace;
    }

    public CardType getTheOtherCardInHand(CardType firstCard){
        ArrayList<CardType> tmpHand = new ArrayList<>(hand);
        tmpHand.remove(firstCard);
        return tmpHand.get(0);
    }
}
