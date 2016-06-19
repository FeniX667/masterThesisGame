package dmalarczyk.masterThesis;

import dmalarczyk.masterThesis.playerAlgorithm.GreedyAlgorithm;
import dmalarczyk.masterThesis.playerAlgorithm.Player;
import dmalarczyk.masterThesis.playerAlgorithm.RandomAlgorithm;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String command;
        Scanner user_input = new Scanner( System.in );
        Player firstPlayer = new RandomAlgorithm();
        Player secondPlayer = new RandomAlgorithm();
        int mctsDelay = 1000;
        do{
            command = user_input.next( );

            switch (command){
                case "set player 1":
                    System.out.println("Wybierz 1 gracza: Random, Greedy, Minmax, Mcts");
                    String aiChoice = user_input.next();
                    switch(aiChoice){
                        case "Random":
                            firstPlayer = new RandomAlgorithm();
                            break;
                        case "Greedy":
                            firstPlayer = new GreedyAlgorithm();
                            break;
                        case "Minmax":
                            firstPlayer = new GreedyAlgorithm();
                            break;
                        case "Mcts":
                            firstPlayer = new GreedyAlgorithm();
                            break;
                    }

            }
            System.out.print(command);
        }while (!command.equals("end"));
    }
}
