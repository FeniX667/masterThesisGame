package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;

public class MctsAlgorithm extends Player {

    MctsAlgorithm(){
        super();
        this.name = "MctsAlgorithm";
    }

    class Node(){

    }

    class Edge(){

    }

    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        return null;
    }
}
