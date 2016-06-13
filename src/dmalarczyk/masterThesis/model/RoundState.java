package dmalarczyk.masterThesis.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RoundState {

    public enum TurnState{
        notStarted, playerA, playerB, playerAWon, playerBWon, draw
    }

    public ArrayList<CardType> deck;
    public PlayerSpace spaceOfPlayerA;
    public PlayerSpace spaceOfPlayerB;
    public ArrayList<CardType> openDiscardedCards;
    public CardType hiddenDiscardedCard;

    public TurnState turnState;

    public RoundState(){
        initVariables();
        initDeck();
    }

    private void initVariables(){
        deck = new ArrayList<>();
        spaceOfPlayerA = new PlayerSpace();
        spaceOfPlayerB = new PlayerSpace();
        openDiscardedCards = new ArrayList<>();
        turnState = TurnState.notStarted;
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

    public void switchState(){
        if (turnState == TurnState.playerA){
            turnState = RoundState.TurnState.playerB;
        }
        else if (turnState == TurnState.playerB){
            turnState = RoundState.TurnState.playerA;
        }
        else if (turnState == TurnState.notStarted){
            turnState = RoundState.TurnState.playerA;
        }
    }
}
