package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.model.DecisionType;
import dmalarczyk.masterThesis.model.RoundState;

import java.util.Collections;
import java.util.List;

public class RandomAlgorithm implements Player {
    public RandomAlgorithm(){
    }

    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        decisionList.remove( DecisionType.princessPlay );
        Collections.shuffle(decisionList);
        return decisionList.get(0);
    }
}
