package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomAlgorithm extends Player {
    public RandomAlgorithm(){
        super();
        this.name = "RandomAlgorithm";
    }


    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        decisionList.remove( DecisionType.princessPlay );
        if( playerSpace.hand.containsAll(Arrays.asList(CardType.prince, CardType.princess)) );
            decisionList.remove( DecisionType.prince_onMyself );

        Collections.shuffle(decisionList);
        return decisionList.get(0);
    }
}
