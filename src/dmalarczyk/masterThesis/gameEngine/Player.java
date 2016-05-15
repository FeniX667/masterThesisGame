package dmalarczyk.masterThesis.gameEngine;

import dmalarczyk.masterThesis.model.DecisionType;

import java.util.List;

public interface Player {
    DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList);
}
