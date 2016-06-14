package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;

public class GreedyAlgorithm extends Player {
    GreedyAlgorithm(PlayerSpace playerSpace) {
        super(playerSpace);
    }

    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        return null;
    }
}
