package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.model.DecisionType;
import dmalarczyk.masterThesis.model.RoundState;

import java.util.List;

public interface Player {
    DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList);
}
