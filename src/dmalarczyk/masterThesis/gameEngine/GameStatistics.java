package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

public class GameStatistics {
    public int endingRound;
    public RoundState.Winner winner;
    public boolean isWinByComparison;
    public DecisionType winningMove;
}
