package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.*;

public class MctsAlgorithm extends Player {

    public static double explorationParameter = Math.sqrt(2.0);
    public long timeRestriction;
    public int iterationRestriction;
    Random rnd;
    public MctsAlgorithm(){
        super();
        this.name = "MctsAlgorithm";
        this.timeRestriction = 1000;
        this.iterationRestriction = 2000;
        rnd = new Random();
    }

    class Node{
        DecisionType decision;
        RoundState roundState;
        double wins;
        double value;
        double visits;
        double utc;
        Node parent;
        List<Node> children;

        Node(RoundState roundState, DecisionType decision){
            this.decision = decision;
            this.roundState = roundState;
            wins = 0;
            value = 0.0;
            visits = 0;
            parent = null;
            children = new ArrayList<>();
        }

        public void addChild(Node child){
            children.add(child);
            child.parent=this;
        }

        public void update(double result){
            visits++;
            wins +=result;
            value = wins/visits;
            if( visits == 0 || parent == null )
                utc = -1;
            else
                utc =  value/visits + explorationParameter * Math.sqrt( Math.log( parent.visits ) / visits);
        }

        public DecisionType getMostProvenDecision(){

            Node mostProvenChild = children.get(0);
            for( Node child : children ){
                if( child.visits > mostProvenChild.visits)
                    mostProvenChild = child;
            }

            return mostProvenChild.decision;
        }

        private double getUCT() {
            if( visits == 0 )
                return 1000 + rnd.nextInt(20);
            return value/visits + explorationParameter * Math.sqrt( Math.log( parent.visits ) / visits);
        }
    }

    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        Node root = new Node( new RoundState(roundState, playerSpace), null );

        long startTime = System.currentTimeMillis();
        long elapsedTime;
        int iteration = 0;
        boolean shouldContinue = true;
        do{
            Node selectedNode = select(root);
            if( selectedNode.roundState.turnState != RoundState.TurnState.ended ) {
                List<DecisionType> selectionDecisionList = DecisionType.getDecisions(selectedNode.roundState.spaceOfFirstPlayer.hand);
                for (DecisionType decision : selectionDecisionList) {
                    selectedNode.addChild(new Node(new RoundState(roundState, roundState.spaceOfFirstPlayer), decision));
                }
                Node child = selectedNode.children.get(rnd.nextInt(selectedNode.children.size()));
                double result = simulate(child);
                do {
                    child.update(result);
                    child = child.parent;
                } while (child != null);
            }
            elapsedTime = System.currentTimeMillis() - startTime;
            iteration++;
            shouldContinue = timePredicate(elapsedTime); // && simCountPredicate(iteration);
            if (!shouldContinue)
                System.out.print("");
        }while (shouldContinue);

        DecisionType mostProvenDecision = root.getMostProvenDecision();
        return mostProvenDecision;
    }

    private boolean timePredicate(double elapsedTime){
        if (elapsedTime < timeRestriction)
            return true;
        return false;
    }

    private boolean simCountPredicate(int iteration){
        if (iteration < iterationRestriction )
            return true;
        return false;
    }

    private Node select(Node node) {
        if( node.children.size() == 0  ){
            return node;
        }

        Node selection = node.children.get(0);
        Map<DecisionType, Double> utcMap = new HashMap<>();
        for( Node child : node.children ){
            if( child.getUCT() > selection.getUCT() || selection.getUCT() == 0 )
                selection = child;
            utcMap.put(child.decision, child.getUCT());
        }
        return select(selection);
    }

    private double simulate(Node selection) {
        Engine shortEngine = new Engine(new GreedyAlgorithm(), new GreedyAlgorithm(), selection.roundState);

        shortEngine.setCurrentAndOpponentSpace();
        shortEngine.makeMove(selection.decision);

        shortEngine.statistics.winningMove = selection.decision;
        if( shortEngine.roundState.deck.size() == 0 && shortEngine.roundState.turnState != RoundState.TurnState.ended){
            shortEngine.endGame(null);
        }
        shortEngine.roundState.switchState();
        shortEngine.iteration++;

        if( selection.roundState.turnState != RoundState.TurnState.ended){
            Player me = new GreedyAlgorithm();
            Player opponent = new GreedyAlgorithm();
            Engine engine = new Engine(me, opponent, new RoundState(selection.roundState, selection.roundState.spaceOfFirstPlayer));
            engine.run();

            shortEngine.iterate();
            shortEngine.setCurrentAndOpponentSpace();
            shortEngine.roundState.drawCardForPlayerSpace(shortEngine.currentPlayer.playerSpace);
            if( engine.roundState.winner == RoundState.Winner.firstPlayer )
                return 1.0;
            else
                return 0.0;
        }
        else if( selection.roundState.winner == RoundState.Winner.firstPlayer )
            return 1.0;
        else
            return 0.0;
    }
}
