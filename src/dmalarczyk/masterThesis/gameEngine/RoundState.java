package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.model.CardType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RoundState {

    public ArrayList<CardType> deck;
    public ArrayList<CardType> discardedDeckPlayerA;
    public ArrayList<CardType> discardedDeckPlayerB;
    public ArrayList<CardType> handPlayerA;
    public ArrayList<CardType> handPlayerB;
    public ArrayList<CardType> openDiscardedCards;
    public CardType hiddenDiscardedCard;

    public boolean safetyFlagPlayerA;
    public boolean safetyFlagPlayerB;
    public boolean turnOrder;

    public Random rnd;

    public RoundState(){
        initVariables();
        initDeck();

    }

    private void initVariables(){
        rnd = new Random();
        deck = new ArrayList<>();
        discardedDeckPlayerA = new ArrayList<>();
        discardedDeckPlayerB = new ArrayList<>();
        openDiscardedCards = new ArrayList<>();
        handPlayerA = new ArrayList<>();
        handPlayerB = new ArrayList<>();
        safetyFlagPlayerA = false;
        safetyFlagPlayerB = false;
        turnOrder = false;
    }

    private void initDeck(){
        deck.addAll(
                Arrays.asList(CardType.guard, CardType.guard, CardType.guard, CardType.guard, CardType.guard,
                        CardType.priest, CardType.priest,
                        CardType.baron, CardType.baron,
                        CardType.handmaid, CardType.handmaid,
                        CardType.prince, CardType.prince,
                        CardType.king,
                        CardType.countess,
                        CardType.princess
                )
        );
        Collections.shuffle(deck);
    }

    public void setRoundForPlay(){
        hiddenDiscardedCard = deck.remove( rnd.nextInt(deck.size())) ;
        for(int i=3 ; i!=0 ; i--){
            openDiscardedCards.add( deck.remove(rnd.nextInt(deck.size())) );
        }

        handPlayerA.add( deck.remove(rnd.nextInt(deck.size())) );
        handPlayerB.add( deck.remove(rnd.nextInt(deck.size())) );
    }

//    public Round(Round _round){
//        initVariables();
//        deck =
//        discardedDeckPlayerA = _round.discardedDeckPlayerA.clone();
//        discardedDeckPlayerB = _round.discardedDeckPlayerB.clone();
//        discardedOpen = _round.discardedDeckPlayerB.clone();
//        safetyFlagPlayerA = _round.safetyFlagPlayerA;
//        safetyFlagPlayerB = _round.safetyFlagPlayerB;
//        handPlayerA = _round.handPlayerA;
//        handPlayerB = _round.handPlayerB;
//
//    }
}
