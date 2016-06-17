package dmalarczyk.masterThesis.gameModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class RoundState {

    public enum TurnState{
        notStarted, firstPlayer, secondPlayer, ended
    }

    public enum Winner{
        none, firstPlayer, secondPlayer
    }

    Random rnd;

    public ArrayList<CardType> deck;
    public PlayerSpace spaceOfFirstPlayer;
    public PlayerSpace spaceOfSecondPlayer;
    public ArrayList<CardType> openDiscardedCards;
    public CardType hiddenDiscardedCard;

    public TurnState turnState;
    public Winner winner;
    public boolean winByComparison;

    public RoundState(){
        initVariables();
        initDecks();
    }

    public RoundState(RoundState roundState, PlayerSpace knownPlayerSpace){
        List<CardType> allHiddenCards = roundState.getAllHiddenCardsAndHands();
        knownPlayerSpace.hand.forEach(allHiddenCards::remove);
        PlayerSpace opponentSpace;
        if (roundState.spaceOfFirstPlayer == knownPlayerSpace)
            opponentSpace = roundState.spaceOfSecondPlayer;
        else
            opponentSpace = roundState.spaceOfFirstPlayer;

        initVariables();

        deck.addAll(allHiddenCards);
        spaceOfFirstPlayer.hand.addAll( knownPlayerSpace.hand );
        spaceOfFirstPlayer.discardedDeck.addAll( knownPlayerSpace.discardedDeck );
        spaceOfFirstPlayer.isSafe = knownPlayerSpace.isSafe;
        spaceOfSecondPlayer.isSafe = opponentSpace.isSafe;
        spaceOfSecondPlayer.discardedDeck.addAll( opponentSpace.discardedDeck );
        for(int i=0 ; i<opponentSpace.hand.size() ; i++ ){
            drawCardForPlayerSpace(spaceOfSecondPlayer);
        }
        openDiscardedCards.addAll(roundState.openDiscardedCards);
        hiddenDiscardedCard = drawCardFromDeck();
        turnState = roundState.turnState;
        winner = roundState.winner;
        winByComparison = roundState.winByComparison;
    }

    private void initVariables(){
        deck = new ArrayList<>();
        spaceOfFirstPlayer = new PlayerSpace();
        spaceOfSecondPlayer = new PlayerSpace();
        openDiscardedCards = new ArrayList<>();
        turnState = TurnState.notStarted;
        rnd = new Random();
        winner = Winner.none;
        winByComparison = false;
    }

    private void initDecks(){
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

        hiddenDiscardedCard = drawCardFromDeck() ;
        for(int i=3 ; i!=0 ; i--){
            openDiscardedCards.add( drawCardFromDeck() );
        }

        spaceOfFirstPlayer.hand.add( drawCardFromDeck() );
        spaceOfSecondPlayer.hand.add( drawCardFromDeck() );
    }

    public void switchState(){
        if (turnState == TurnState.firstPlayer){
            turnState = TurnState.secondPlayer;
        }
        else if (turnState == TurnState.secondPlayer){
            turnState = TurnState.firstPlayer;
        }
        else if (turnState == TurnState.notStarted){
            turnState = TurnState.firstPlayer;
        }
    }

    public void drawCardForPlayerSpace(PlayerSpace playerSpace){
        boolean added = playerSpace == spaceOfFirstPlayer ? spaceOfFirstPlayer.hand.add(drawCardFromDeck()) : spaceOfSecondPlayer.hand.add(drawCardFromDeck());
    }

    private CardType drawCardFromDeck(){
        if( deck.size() == 0){
            CardType hiddenCard = hiddenDiscardedCard;
            hiddenDiscardedCard = null;
            turnState = TurnState.ended;
            return hiddenCard;
        }

        return deck.remove(rnd.nextInt(deck.size()));
    }

    public Map<CardType, Double> getProbabilityMapForPlayer(PlayerSpace playerSpace){
        List<CardType> allHiddenCards = getAllHiddenCardsAndHands();

        playerSpace.hand.forEach(allHiddenCards::remove);

        Double nrOfHiddenCards = new Double(allHiddenCards.size());
        HashMap<CardType, Double> probabilityMap = new HashMap<>();

        for(CardType cardType : EnumSet.allOf(CardType.class)){
            Double tmpCount = new Double(0.0);
            for(CardType card : allHiddenCards) {
                if (card == cardType) {
                    tmpCount+=1.0;
                }
            }
            probabilityMap.put(cardType, round(tmpCount / nrOfHiddenCards) );
        }

        return probabilityMap;
    }


    public static Map<CardType, Double> getProbabilityMap(List<CardType> cardList){
        Double nrOfHiddenCards = new Double(cardList.size());
        HashMap<CardType, Double> probabilityMap = new HashMap<>();

        for(CardType cardType : EnumSet.allOf(CardType.class)){
            Double tmpCount = new Double(0.0);
            for(CardType card : cardList) {
                if (card == cardType) {
                    tmpCount+=1.0;
                }
            }
            probabilityMap.put(cardType, round(tmpCount / nrOfHiddenCards) );
        }

        return probabilityMap;
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<CardType> getAllHiddenCardsAndHands(){
        List<CardType> allHiddenCards = new ArrayList<>();
        allHiddenCards.addAll( deck );
        allHiddenCards.addAll( spaceOfFirstPlayer.hand );
        allHiddenCards.addAll( spaceOfSecondPlayer.hand );
        allHiddenCards.add( hiddenDiscardedCard );
        Collections.shuffle(allHiddenCards);
        return allHiddenCards;
    }

    public List<CardType> getHiddenCards(){
        List<CardType> allHiddenCards = new ArrayList<>();
        allHiddenCards.addAll( deck );
        allHiddenCards.addAll( spaceOfFirstPlayer.hand );
        allHiddenCards.addAll( spaceOfSecondPlayer.hand );
        allHiddenCards.add( hiddenDiscardedCard );

        return  allHiddenCards;
    }


    public void discardPlayedCard(PlayerSpace space, CardType cardType){
        space.hand.remove(cardType);
        space.discardedDeck.add(cardType);
    }

}
