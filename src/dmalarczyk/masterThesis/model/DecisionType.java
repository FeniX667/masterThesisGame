package dmalarczyk.masterThesis.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum DecisionType {
    guard_priest, guard_baron, guard_handmaid, guard_prince, guard_king, guard_countess, guard_princess,
    priestPlay,
    baronPlay,
    handmaidPlay,
    prince_onOpponent,
    prince_onMyself,
    kingPlay,
    countess_withKingOrPrince,
    countess_defaultPlay,
    princessPlay;

    public static ArrayList<DecisionType> getDecisions(ArrayList<CardType> hand){
        return getDecisions(hand.get(0), hand.get(1));
    }

    public static ArrayList<DecisionType> getDecisions(CardType firstCard, CardType secondCard){
        ArrayList<DecisionType>  decisionList = new ArrayList<>();

        decisionList.addAll( getPlays(firstCard, secondCard) );
        if (secondCard != firstCard)
            decisionList.addAll( getPlays(secondCard, firstCard) );
        return  decisionList;
    }

    private static ArrayList<DecisionType> getPlays(CardType firstCard, CardType secondCard){
        ArrayList<DecisionType>  decisionList = new ArrayList<>();
        switch(firstCard){
            case guard:
                decisionList.addAll(Arrays.asList(guard_priest, guard_baron,  guard_handmaid, guard_prince, guard_king, guard_countess, guard_princess));
                break;
            case priest:
                decisionList.add(priestPlay);
                break;
            case baron:
                decisionList.add(baronPlay);
                break;
            case handmaid:
                decisionList.add(handmaidPlay);
                break;
            case prince:
                decisionList.add(prince_onMyself);
                decisionList.add(prince_onOpponent);
                break;
            case king:
                decisionList.add(kingPlay);
                break;
            case countess:
                if( secondCard==CardType.king || secondCard==CardType.prince)
                    decisionList.add(countess_withKingOrPrince);
                else
                    decisionList.add(countess_defaultPlay);
                break;
            case princess:
                decisionList.add(princessPlay);
        }

        return  decisionList;
    }
}

