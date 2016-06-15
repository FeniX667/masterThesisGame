package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;
import java.util.Map;

public class GreedyAlgorithm extends Player {
    public GreedyAlgorithm(PlayerSpace playerSpace) {
        super(playerSpace);
    }

    @Override
    public DecisionType makeDecision(RoundState uncertainRoundState, List<DecisionType> decisionList) {
        DecisionType finalDecision = null;
        Map<CardType, Double> probabilityMap = uncertainRoundState.getProbabilityMap();

        for( DecisionType decision : decisionList ){
            if ( decisionValue(decision, probabilityMap) > decisionValue(finalDecision, probabilityMap))
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
