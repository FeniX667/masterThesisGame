package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;

public abstract class Player {
    Player(PlayerSpace playerSpace){
        this.playerSpace = playerSpace;
    }

    public abstract String name();

    public abstract DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList);
    public PlayerSpace playerSpace;
}
