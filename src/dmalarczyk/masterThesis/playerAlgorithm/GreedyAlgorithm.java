package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyAlgorithm extends Player {
    public GreedyAlgorithm() {
        super();
        this.name = "GreedyAlgorithm";
        this.acronym = "G";
    }

    @Override
    public DecisionType makeDecision(RoundState uncertainRoundState, List<DecisionType> decisionList) {
        DecisionType finalDecision = null;
        Map<CardType, Double> probabilityMap = uncertainRoundState.getProbabilityMapForPlayer(this.playerSpace);
        Map<DecisionType, Double> decisionValueMap = new HashMap<>();
        for( DecisionType decision : decisionList ){
            decisionValueMap.put(decision, decisionValue(decision, probabilityMap));
            if ( decisionValue(decision, probabilityMap) >= decisionValue(finalDecision, probabilityMap))
                finalDecision = decision;
        }

        return finalDecision;
    }

    private double decisionValue(DecisionType decision, Map<CardType, Double> probabilityMap){
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
            case countessPlay:
                return 1+ 0.002;
            case princessPlay:
                return 0;
        }

        return value;
    }
}
