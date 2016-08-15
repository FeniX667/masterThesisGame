package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;

public abstract class Player {
    public PlayerSpace playerSpace;
    public String name;
    public String acronym;
    public boolean isPlayerSpaceSet;

    Player(){
        isPlayerSpaceSet = false;
    }

    public abstract DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList);

    public void setPlayerSpace(PlayerSpace playerSpace){
        this.playerSpace = playerSpace;
    }
}
