package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.*;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class MinMaxAlgorithm extends Player {
    public MinMaxAlgorithm() {
        super();
        this.name = "MinMaxAlgorithm";
    }

    @Override
    public DecisionType makeDecision(RoundState uncertainRoundState, List<DecisionType> decisionList) {
        if( !uncertainRoundState.spaceOfFirstPlayer.hand.containsAll(this.playerSpace.hand) )
            throw new RoundCopyMismatchException();
        DecisionType finalDecision = null;


        for( DecisionType decision : decisionList ){
            RoundState roundState = new RoundState(uncertainRoundState, playerSpace);
            if( uncertainRoundState.deck.size() > 0) {
                Map<CardType, Double> probabilityMap = getProbabilityMap(roundState);
                if (maxValue(decision, probabilityMap) - minValue(decision, probabilityMap, roundState) >=
                        maxValue(finalDecision, probabilityMap) - minValue(decision, probabilityMap, roundState) )
                    finalDecision = decision;
            }
            else{
                Map<CardType, Double> probabilityMap = uncertainRoundState.getProbabilityMapForPlayer(this.playerSpace);
                if (maxValue(decision, probabilityMap) >= maxValue(finalDecision, probabilityMap))
                    finalDecision = decision;
            }
        }
        return finalDecision;
    }

    private Map<CardType, Double> getProbabilityMap(RoundState roundState){

        Map<CardType, Double> probabilityMap = roundState.getProbabilityMapForPlayer(this.playerSpace);
        Map<CardType, Double> updatedProbabilityMap =  new HashMap<>();

        ArrayList<CardType> discard = roundState.spaceOfSecondPlayer.discardedDeck;
        if( discard.size() > 0 && discard.get(discard.size() - 1) == CardType.countess
                && ( probabilityMap.get(CardType.prince) > 0 || probabilityMap.get(CardType.king) > 0 ) ){
            if( probabilityMap.get(CardType.prince) > 0 && probabilityMap.get(CardType.king) > 0){
                if( probabilityMap.get(CardType.prince) > probabilityMap.get(CardType.king) ) {
                    for (CardType cardType : EnumSet.allOf(CardType.class)) {
                        if (cardType == CardType.prince)
                            updatedProbabilityMap.put(cardType, 0.66);
                        else if (cardType == CardType.king)
                            updatedProbabilityMap.put(cardType, 0.33);
                        else
                            updatedProbabilityMap.put(cardType, 0.00);
                    }
                }
                else{
                    for (CardType cardType : EnumSet.allOf(CardType.class)) {
                        if (cardType == CardType.prince)
                            updatedProbabilityMap.put(cardType, 0.5);
                        else if (cardType == CardType.king)
                            updatedProbabilityMap.put(cardType, 0.5);
                        else
                            updatedProbabilityMap.put(cardType, 0.00);
                    }
                }
            }
            else if(probabilityMap.get(CardType.prince) > 0){
                for (CardType cardType : EnumSet.allOf(CardType.class)) {
                    if (cardType == CardType.prince)
                        updatedProbabilityMap.put(cardType, 1.0);
                    else
                        updatedProbabilityMap.put(cardType, 0.00);
                }
            }
            else if(probabilityMap.get(CardType.king) > 0){
                for (CardType cardType : EnumSet.allOf(CardType.class)) {
                    if (cardType == CardType.king)
                        updatedProbabilityMap.put(cardType, 1.0);
                    else
                        updatedProbabilityMap.put(cardType, 0.00);
                }
            }
            return updatedProbabilityMap;
        }

        return probabilityMap;
    }


    private double minValue(DecisionType decision, Map<CardType, Double> probabilityMap, RoundState roundState) {
        double minValue = 0.0;
        if( decision == null || roundState.deck.size() <= 1 )
            return minValue;

        switch (decision) {
            case guard_priest:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.priest)) * reactionLossChance(roundState, CardType.guard);
            case guard_baron:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.baron)) * reactionLossChance(roundState, CardType.guard);
            case guard_handmaid:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.handmaid)) * reactionLossChance(roundState, CardType.guard);
            case guard_prince:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.prince)) * reactionLossChance(roundState, CardType.guard);
            case guard_king:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.king)) * reactionLossChance(roundState, CardType.guard);
            case guard_countess:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.countess)) * reactionLossChance(roundState, CardType.guard);
            case guard_princess:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.guard);
                return (1.0 - probabilityMap.get(CardType.princess)) * reactionLossChance(roundState, CardType.guard);
            case priestPlay:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.priest);
                return reactionLossChance(roundState, CardType.priest);
            case baronPlay:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.baron);
                double drawChance = probabilityMap.get( this.playerSpace.getTheOtherCardInHand(CardType.baron) );
                return drawChance * reactionLossChance(roundState, CardType.baron);
            case handmaidPlay:
                return 0.0;
            case prince_onMyself:
                CardType secondCard = this.playerSpace.getTheOtherCardInHand(CardType.prince);
                if (secondCard == CardType.princess)
                    return 10.0;
                else{
                    roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, secondCard);
                    roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.prince);
                    roundState.drawCardForPlayerSpace(roundState.spaceOfFirstPlayer);
                    return reactionLossChance(roundState, CardType.prince);
                }
            case prince_onOpponent:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.prince);
                return (1.0 - probabilityMap.get(CardType.princess) * reactionLossChance(roundState, CardType.prince));
            case kingPlay:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.king);
                secondCard = roundState.spaceOfFirstPlayer.hand.remove(0);
                roundState.spaceOfFirstPlayer.hand.add(roundState.spaceOfSecondPlayer.hand.remove(0));
                roundState.spaceOfSecondPlayer.hand.add(secondCard);
                roundState.spaceOfFirstPlayer.knownEnemyCard = roundState.spaceOfSecondPlayer.hand.get(0);
                roundState.spaceOfSecondPlayer.knownEnemyCard = roundState.spaceOfFirstPlayer.hand.get(0);
                return reactionLossChance(roundState, CardType.king);
            case countess_withKingOrPrince:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.countess);
                return reactionLossChance(roundState, CardType.countess);
            case countess_defaultPlay:
                roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, CardType.countess);
                return reactionLossChance(roundState, CardType.countess);
            case princessPlay:
                return 10.0;
        }

        return minValue;
    }

    private double reactionLossChance(RoundState roundState, CardType droppedCard) {
        Map<Pair<CardType, CardType>, Double> opponentHandProbabilityMap = getOpponentHandProbabilityMap(roundState);
        Map<Pair<CardType, CardType>, Double> lossProbabilityMap = new HashMap<>();
        double reactionLossChance = 0.0;
        if( opponentHandProbabilityMap == null )
            return reactionLossChance;
        Map<Pair<CardType, CardType>, Pair<DecisionType, Double>> handDecisionLossChanceMap = new HashMap<>();

        for( Pair<CardType, CardType> opponentHand : opponentHandProbabilityMap.keySet()){
            List<CardType> cardsHiddenForEnemy = roundState.getHiddenCards();
            cardsHiddenForEnemy.remove(opponentHand.getKey());
            cardsHiddenForEnemy.remove(opponentHand.getValue());

            Map<CardType, Double> opponentProbabilityMap = RoundState.getProbabilityMap(cardsHiddenForEnemy);
            List<DecisionType> opponentDecisions = DecisionType.getDecisions(opponentHand.getKey(), opponentHand.getValue());

            Map<DecisionType, Double> decisionValueMap = new HashMap<>();
            DecisionType opponentDecision = null;
            for( DecisionType decision : opponentDecisions ){
                if ( maxValue(decision, opponentProbabilityMap) >= maxValue(opponentDecision, opponentProbabilityMap))
                    opponentDecision = decision;
                decisionValueMap.put(decision, maxValue(opponentDecision, opponentProbabilityMap) );
            }

            CardType mySecondCard = playerSpace.getTheOtherCardInHand(droppedCard);
            switch (opponentDecision){
                case guard_priest:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.priest ? 1 : 0)) );
                    break;
                case guard_baron:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.baron ? 1 : 0)) );
                    break;
                case guard_handmaid:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.handmaid ? 1 : 0)) );
                    break;
                case guard_prince:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.prince ? 1 : 0)) );
                    break;
                case guard_king:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.king ? 1 : 0)) );
                    break;
                case guard_countess:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.countess ? 1 : 0)) );
                    break;
                case guard_princess:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.princess ? 1 : 0)) );
                    break;
                case baronPlay:
                    CardType opponentSecondCard = (opponentHand.getKey() == CardType.baron ? opponentHand.getValue() : opponentHand.getKey());
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, CardType.getStrength(mySecondCard) < CardType.getStrength(opponentSecondCard) ? 1.0 : 0.0) );
                    break;
                case prince_onOpponent:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, (double) (mySecondCard == CardType.princess ? 1 : 0)) );
                    break;
                default:
                    handDecisionLossChanceMap.put(opponentHand, new Pair<>( opponentDecision, new Double(0.0) ));
                    break;

            }
        }

        for( Pair<CardType, CardType> opponentHand : opponentHandProbabilityMap.keySet()){
            lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * handDecisionLossChanceMap.get(opponentHand).getValue() );
        }

        for(Pair<CardType, CardType> hand : lossProbabilityMap.keySet()){
            reactionLossChance += lossProbabilityMap.get(hand);
        }

        return round(reactionLossChance);
    }

    private Map<Pair<CardType, CardType>, Double> getOpponentHandProbabilityMap(RoundState roundState){
        Map<Pair<CardType, CardType>, Double> opponentHandProbabilityMap = new HashMap<>();
        List<Pair<CardType, CardType>> opponentHandList = new ArrayList<>();

        List<CardType> hiddenCards = roundState.getAllHiddenCardsAndHands();
        playerSpace.hand.forEach(hiddenCards::remove);

        for(CardType firstCard : hiddenCards){
            List<CardType> possibleHiddenCards = new ArrayList<>(hiddenCards);
            possibleHiddenCards.remove(firstCard);
            for(CardType secondCard: possibleHiddenCards){
                Pair<CardType, CardType> probableHand = new Pair<>(firstCard, secondCard);
                opponentHandList.add( probableHand );
            }
        }

        Map<Pair<CardType, CardType>, Double> opponentHandCombinationFrequency = new HashMap<>();
        for( Pair<CardType,CardType> hand : new HashSet<>(opponentHandList)){
            int frequency = Collections.frequency(opponentHandList, hand);
            opponentHandCombinationFrequency.put(hand, (double) frequency);
        }
        Map<Pair<CardType, CardType>, Double> opponentUniqueHandCombinationFrequency = new HashMap<>();
        for( Pair<CardType,CardType> hand : opponentHandCombinationFrequency.keySet() ){
            if( !opponentUniqueHandCombinationFrequency.containsKey( new Pair<>(hand.getValue(), hand.getKey())) ){
                if( hand.getKey() != hand.getValue() )
                    opponentUniqueHandCombinationFrequency.put(hand, opponentHandCombinationFrequency.get(hand) *2 );
                else
                    opponentUniqueHandCombinationFrequency.put(hand, opponentHandCombinationFrequency.get(hand) );
            }
        }

        for( Pair<CardType,CardType> hand : opponentUniqueHandCombinationFrequency.keySet()){
            opponentHandProbabilityMap.put(hand, round(opponentUniqueHandCombinationFrequency.get(hand) / opponentHandList.size()));
        }

        return opponentHandProbabilityMap;
    }

    private double maxValue(DecisionType decision, Map<CardType, Double> probabilityMap){
        double value = 0.0;
        if( decision == null )
            return value;

        switch (decision){
            case guard_priest:
                return 1.0 + probabilityMap.get(CardType.priest) + 0.008;
            case guard_baron:
                return 1.0 + probabilityMap.get(CardType.baron) + 0.008;
            case guard_handmaid:
                return 1.0 + probabilityMap.get(CardType.handmaid) + 0.008;
            case guard_prince:
                return 1.0 + probabilityMap.get(CardType.prince) + 0.008;
            case guard_king:
                return 1.0 + probabilityMap.get(CardType.king) + 0.008;
            case guard_countess:
                return 1.0 + probabilityMap.get(CardType.countess) + 0.008;
            case guard_princess:
                return 1.0 + probabilityMap.get(CardType.princess) + 0.008;
            case priestPlay:
                return 1.0 + 0.007;
            case baronPlay:
                double lossChance = 0.0;
                double winChance = 0.0;
                for( CardType opponentCard : probabilityMap.keySet() ){
                    CardType secondCard = this.playerSpace.getTheOtherCardInHand(CardType.guard);
                    if( CardType.getStrength(secondCard) < CardType.getStrength(opponentCard) )
                        lossChance += probabilityMap.get(opponentCard);
                    else if( CardType.getStrength(secondCard) > CardType.getStrength(opponentCard) )
                        winChance += probabilityMap.get(opponentCard);
                }
                return 1 - lossChance + winChance + 0.006;
            case handmaidPlay:
                return 1 + 0.005;
            case prince_onMyself:
                CardType secondCard = this.playerSpace.getTheOtherCardInHand(CardType.prince);
                if( secondCard == CardType.princess )
                    return 0;
                else
                    return 1 + 0.004;
            case prince_onOpponent:
                return 1 + probabilityMap.get(CardType.princess) + 0.004;
            case kingPlay:
                return 1 + probabilityMap.get(CardType.princess) + probabilityMap.get(CardType.countess) + 0.003;
            case countess_withKingOrPrince:
                return 10 + 0.002;
            case countess_defaultPlay:
                return 1+ 0.002;
            case princessPlay:
                return 0;
        }

        return value;
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
