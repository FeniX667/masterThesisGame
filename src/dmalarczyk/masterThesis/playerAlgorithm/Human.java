package dmalarczyk.masterThesis.playerAlgorithm;

import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Human extends Player {

    Scanner user_input;

    public Human(){
        this.name = "Human";
        this.user_input = new Scanner( System.in );
    }

    @Override
    public DecisionType makeDecision(RoundState roundState, List<DecisionType> decisionList) {
        List<String> availableCommands = decisionList.stream().map(DecisionType::name).collect(Collectors.toList());
        System.out.println(" --- Opponent discarded deck: " + roundState.spaceOfSecondPlayer.discardedDeck + ". ");
        if( roundState.spaceOfFirstPlayer.knownEnemyCard != null )
            System.out.println(" --- You know enemy has " + roundState.spaceOfFirstPlayer.knownEnemyCard + " in his hand!");
        System.out.println(" --- Your cards: " +roundState.spaceOfFirstPlayer.hand+ ". Available decisions: " + availableCommands);
        String command;
        do{
            command = user_input.nextLine();
            if( availableCommands.contains(command) ){
                return DecisionType.valueOf(command);
            }
            else{
                System.out.println("Incorrect decision");
            }
        }while (true);
    }
}
