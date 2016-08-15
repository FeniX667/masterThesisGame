package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

public class GameStatistics {
    public String firstPlayerName;
    public String secondPlayerName;
    public String firstPlayerAcronym;
    public String secondPlayerAcronym;
    public int endingRound;
    public RoundState.Winner winner;
    public boolean isWinByComparison;
    public DecisionType winningMove;
}
