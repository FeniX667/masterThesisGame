package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;
import javafx.util.Pair;

import java.util.*;


public class MinMaxAlgorithm extends Player {
    public MinMaxAlgorithm(PlayerSpace playerSpace) {
        super(playerSpace);
    }

    @Override
    public DecisionType makeDecision(RoundState uncertainRoundState, List<DecisionType> decisionList) {
        DecisionType finalDecision = null;
        Map<CardType, Double> probabilityMap = uncertainRoundState.getProbabilityMapForPlayer(this.playerSpace);

        for( DecisionType decision : decisionList ){
            if ( maxValue(decision, probabilityMap) - minValue(decision, probabilityMap, uncertainRoundState) >
                    maxValue(finalDecision, probabilityMap) - minValue(decision, probabilityMap, uncertainRoundState))
                finalDecision = decision;
        }

        return finalDecision;
    }

    private double minValue(DecisionType decision, Map<CardType, Double> probabilityMap, RoundState roundState) {
        double minValue = 0.0;
        if( decision == null )
            return minValue;

        switch (decision) {
            case guard_priest:
                return (1.0 - probabilityMap.get(CardType.priest)) * reactionLossChance(roundState, CardType.guard);
            case guard_baron:
                return (1.0 - probabilityMap.get(CardType.baron)) * reactionLossChance(roundState, CardType.guard);
            case guard_handmaid:
                return (1.0 - probabilityMap.get(CardType.handmaid)) * reactionLossChance(roundState, CardType.guard);
            case guard_prince:
                return (1.0 - probabilityMap.get(CardType.prince)) * reactionLossChance(roundState, CardType.guard);
            case guard_king:
                return (1.0 - probabilityMap.get(CardType.king)) * reactionLossChance(roundState, CardType.guard);
            case guard_countess:
                return (1.0 - probabilityMap.get(CardType.countess)) * reactionLossChance(roundState, CardType.guard);
            case guard_princess:
                return (1.0 - probabilityMap.get(CardType.princess)) * reactionLossChance(roundState, CardType.guard);
            case priestPlay:
                return reactionLossChance(roundState, CardType.priest);
            case baronPlay:
                double drawChance = probabilityMap.get( this.playerSpace.getSecondCardInHand(CardType.baron) );
                return drawChance * reactionLossChance(roundState, CardType.baron);
            case handmaidPlay:
                return 0;
            case prince_onMyself:
                CardType secondCard = this.playerSpace.getSecondCardInHand(CardType.prince);
                if (secondCard == CardType.princess)
                    return 0;
                else
                    return reactionLossChance(roundState, CardType.prince);
            case prince_onOpponent:
                return (1 -probabilityMap.get(CardType.princess) * reactionLossChance(roundState, CardType.prince));
            case kingPlay:
                return reactionLossChance(roundState, CardType.king);
            case countess_withKingOrPrince:
                return reactionLossChance(roundState, CardType.countess);
            case countess_defaultPlay:
                return reactionLossChance(roundState, CardType.countess);
            case princessPlay:
                return 0;
        }

        return minValue;
    }

    private double reactionLossChance(RoundState roundState, CardType droppedCard) {
        Map<Pair<CardType, CardType>, Double> opponentHandProbabilityMap = getOpponentHandProbabilityMap(roundState);
        Map<DecisionType, Double> lossProbabilityMap = new HashMap<>();
        if( opponentHandProbabilityMap == null || opponentHandProbabilityMap.isEmpty() )
            return reactionLossChance;

        List<DecisionType> greedyReactions = new ArrayList<>();

        for( Pair<CardType, CardType> opponentHand : opponentHandProbabilityMap.keySet()){
            List<DecisionType> opponentDecisions = DecisionType.getDecisions(opponentHand.getKey(), opponentHand.getValue());
            CardType mySecondCard = playerSpace.getSecondCardInHand(droppedCard);

            List<CardType> cardsHiddenForEnemy = roundState.getHiddenCards();
            cardsHiddenForEnemy.remove(opponentHand.getKey());
            cardsHiddenForEnemy.remove(opponentHand.getValue());
            cardsHiddenForEnemy.remove(mySecondCard);
            Map<CardType, Double> opponentProbabilityMap = RoundState.getProbabilityMap(cardsHiddenForEnemy);

            DecisionType opponentDecision = null;

            for( DecisionType decision : opponentDecisions ){
                if ( maxValue(decision, opponentProbabilityMap) > maxValue(opponentDecision, opponentProbabilityMap))
                    opponentDecision = decision;
            }
            greedyReactions.add( opponentDecision );
        }

        for( DecisionType decision: greedyReactions  ){
            // od 6 punktu greedyReactions.add
        }

        double reactionLossChance = 0.0;
        return reactionLossChance;
    }

    private Map<Pair<CardType, CardType>, Double> getOpponentHandProbabilityMap(RoundState roundState){
        Map<Pair<CardType, CardType>, Double> opponentHandProbabilityMap = new HashMap<>();
        List<Pair<CardType, CardType>> opponentHandList = new ArrayList<>();

        List<CardType> hiddenCards = roundState.getAllHiddenCards(this.playerSpace);

        for(CardType firstCard : hiddenCards){
            List<CardType> possibleHiddenCards = new ArrayList<>(hiddenCards);
            possibleHiddenCards.remove(firstCard);
            for(CardType secondCard: hiddenCards){
                Pair<CardType, CardType> probableHand = new Pair<>(firstCard, secondCard);
                opponentHandList.add( probableHand );
            }
        }

        for( Pair<CardType,CardType> hand : new HashSet<>(opponentHandList)){
            opponentHandProbabilityMap.put(hand, new Double(Collections.frequency(opponentHandList, hand)));
        }

        return opponentHandProbabilityMap;
    }

    private double maxValue(DecisionType decision, Map<CardType, Double> probabilityMap){
        double value = 0.0;
        if( decision == null )
            return value;

        switch (decision){
            case guard_priest:
                return 1.0 + probabilityMap.get(CardType.priest) + 0.8;
            case guard_baron:
                return 1.0 + probabilityMap.get(CardType.baron) + 0.8;
            case guard_handmaid:
                return 1.0 + probabilityMap.get(CardType.handmaid) + 0.8;
            case guard_prince:
                return 1.0 + probabilityMap.get(CardType.prince) + 0.8;
            case guard_king:
                return 1.0 + probabilityMap.get(CardType.king) + 0.8;
            case guard_countess:
                return 1.0 + probabilityMap.get(CardType.countess) + 0.8;
            case guard_princess:
                return 1.0 + probabilityMap.get(CardType.princess) + 0.8;
            case priestPlay:
                return 1.0 + 0.7;
            case baronPlay:
                double lossChance = 0.0;
                double winChance = 0.0;
                for( CardType opponentCard : probabilityMap.keySet() ){
                    CardType secondCard = this.playerSpace.getSecondCardInHand(CardType.guard);
                    if( CardType.getStrength(secondCard) < CardType.getStrength(opponentCard) )
                        lossChance += probabilityMap.get(opponentCard);
                    else if( CardType.getStrength(secondCard) > CardType.getStrength(opponentCard) )
                        winChance += probabilityMap.get(opponentCard);
                }
                return 1 - lossChance + winChance + 0.06;
            case handmaidPlay:
                return 1 + 0.05;
            case prince_onMyself:
                CardType secondCard = this.playerSpace.getSecondCardInHand(CardType.prince);
                if( secondCard == CardType.princess )
                    return 0;
                else
                    return 1 + 0.04;
            case prince_onOpponent:
                return 1 + probabilityMap.get(CardType.princess) + 0.04;
            case kingPlay:
                return 1 + probabilityMap.get(CardType.princess) + probabilityMap.get(CardType.countess) + 0.03;
            case countess_withKingOrPrince:
                return 10 + 0.02;
            case countess_defaultPlay:
                return 1+ 0.02;
            case princessPlay:
                return 0;
        }

        return value;
    }
}
