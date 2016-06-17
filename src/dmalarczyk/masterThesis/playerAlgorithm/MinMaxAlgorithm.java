package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;
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
        DecisionType finalDecision = null;
        Map<DecisionType, Pair<Double, Double>> decisionValueMap = new HashMap<>();

        for( DecisionType decision : decisionList ){
            RoundState roundState = new RoundState(uncertainRoundState, playerSpace);
            Map<CardType, Double> probabilityMap = roundState.getProbabilityMapForPlayer(this.playerSpace);
            if ( maxValue(decision, probabilityMap) - minValue(decision, probabilityMap, uncertainRoundState) >
                    maxValue(finalDecision, probabilityMap) - minValue(decision, probabilityMap, roundState))
                finalDecision = decision;
            decisionValueMap.put(decision, new Pair<>(maxValue(decision, probabilityMap), minValue(decision, probabilityMap, uncertainRoundState)));
        }

        return finalDecision;
    }

    private double minValue(DecisionType decision, Map<CardType, Double> probabilityMap, RoundState roundState) {
        double minValue = 0.0;
        if( decision == null || roundState.deck.size() <= 1 )
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
                double drawChance = probabilityMap.get( this.playerSpace.getTheOtherCardInHand(CardType.baron) );
                return drawChance * reactionLossChance(roundState, CardType.baron);
            case handmaidPlay:
                return 0.0;
            case prince_onMyself:
                CardType secondCard = this.playerSpace.getTheOtherCardInHand(CardType.prince);
                if (secondCard == CardType.princess)
                    return 0.0;
                else{
                    roundState.discardPlayedCard(roundState.spaceOfFirstPlayer, roundState.spaceOfFirstPlayer.getTheOtherCardInHand(CardType.prince));
                    roundState.drawCardForPlayerSpace(roundState.spaceOfFirstPlayer);
                    return reactionLossChance(roundState, CardType.prince);
                }
            case prince_onOpponent:
                return (1.0 - probabilityMap.get(CardType.princess) * reactionLossChance(roundState, CardType.prince));
            case kingPlay:
                return reactionLossChance(roundState, CardType.king);
            case countess_withKingOrPrince:
                return reactionLossChance(roundState, CardType.countess);
            case countess_defaultPlay:
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
        if( opponentHandProbabilityMap == null || opponentHandProbabilityMap.isEmpty() )
            return reactionLossChance;


        for( Pair<CardType, CardType> opponentHand : opponentHandProbabilityMap.keySet()){
            List<CardType> cardsHiddenForEnemy = roundState.getHiddenCards();
            CardType mySecondCard = playerSpace.getTheOtherCardInHand(droppedCard);
            cardsHiddenForEnemy.remove(opponentHand.getKey());
            cardsHiddenForEnemy.remove(opponentHand.getValue());
            cardsHiddenForEnemy.remove(mySecondCard);

            Map<CardType, Double> opponentProbabilityMap = RoundState.getProbabilityMap(cardsHiddenForEnemy);
            List<DecisionType> opponentDecisions = DecisionType.getDecisions(opponentHand.getKey(), opponentHand.getValue());

            Map<DecisionType, Double> decisionValueMap = new HashMap<>();
            DecisionType opponentDecision = null;
            for( DecisionType decision : opponentDecisions ){
                if ( maxValue(decision, opponentProbabilityMap) >= maxValue(opponentDecision, opponentProbabilityMap))
                    opponentDecision = decision;
                decisionValueMap.put(decision, maxValue(opponentDecision, opponentProbabilityMap) );
            }

            switch (opponentDecision){
                case guard_priest:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.priest ? 1 : 0 ) );
                    break;
                case guard_baron:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.baron ? 1 : 0 )  );
                    break;
                case guard_handmaid:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.handmaid ? 1 : 0 )  );
                    break;
                case guard_prince:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.prince ? 1 : 0 )  );
                    break;
                case guard_king:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.king ? 1 : 0 )  );
                    break;
                case guard_countess:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.countess ? 1 : 0 )  );
                    break;
                case guard_princess:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.princess ? 1 : 0 )  );
                    break;
                case baronPlay:
                    CardType opponentSecondCard = opponentHand.getKey() == CardType.baron ? opponentHand.getKey() : opponentHand.getValue();
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (CardType.getStrength(mySecondCard) > CardType.getStrength(opponentSecondCard) ? 1.0 : 0.0) );
                    break;
                case prince_onOpponent:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * (mySecondCard == CardType.princess ? 1.0 : 0.0) );
                    break;
                default:
                    lossProbabilityMap.put(opponentHand, opponentHandProbabilityMap.get(opponentHand) * 0 );
                    break;

            }
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
                return 1 + 0.05;
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
