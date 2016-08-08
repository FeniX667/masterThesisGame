package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.playerAlgorithm.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class CLI {

    String command;
    Scanner user_input;
    Player firstPlayer;
    Player secondPlayer;
    int roundCount;
    List<String> commandList;


    public CLI(){
        user_input = new Scanner( System.in );
        firstPlayer = new GreedyAlgorithm();
        secondPlayer = new RandomAlgorithm();
        roundCount = 1000;
        commandList = Arrays.asList("set player 1", "set player 2", "set round limit", "start simulation", "start single game", "end");
    }

    public void run(){
        System.out.println("Welcome to Love Letter simulator. Set players and wanted nr of plays");
        do{
            viewPanel();

            try {
                command = user_input.nextLine();
                switch (command) {
                    case "set player 1":
                        firstPlayer = setPlayer();
                        break;
                    case "set player 2":
                        secondPlayer = setPlayer();
                        break;
                    case "set round limit":
                        System.out.println("Set nr of rounds:");
                        roundCount = new Integer(user_input.nextLine());
                        break;
                    case "start simulation":
                        startSimulation();
                        break;
                    case "start single game":
                        startSingleGame();
                        break;
                    case "end":
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }catch (NumberFormatException e){
                System.out.println("Incorrect number");
            }catch (NoSuchElementException e){
                continue;
            }
        }while (!command.equals("end"));
    }

    private void viewPanel() {
        System.out.println("---------------------------------");
        System.out.println("| Player 1 is " + firstPlayer.name);
        System.out.println("| Player 2 is " + secondPlayer.name);
        System.out.println("| Nr of plays in simulation " + roundCount);
        System.out.println("| Type one of following commands: ");
        System.out.println("| " + commandList);
        System.out.println("---------------------------------");

    }

    private Player setPlayer() {
        System.out.println("Pick player: Random, Greedy, Minmax, Mcts, Human");
        String aiChoice = user_input.nextLine();
        switch (aiChoice) {
            case "Random":
                return new RandomAlgorithm();
            case "Greedy":
                return new GreedyAlgorithm();
            case "Minmax":
                return new MinMaxAlgorithm();
            case "Mcts":
                System.out.println("Set max delay in mcts iterations (in ms):");
                String mctsOption = user_input.nextLine();
                return new MctsAlgorithm(new Integer(mctsOption));
            case "Human":
                return new Human();
            default:
                System.out.println("Incorrect algorithm. Random is set.");
                return new RandomAlgorithm();
        }
    }

    private void startSimulation(){
        System.out.println("Game starts.");
        List<GameStatistics> statistics  = new ArrayList<>();
        PrintWriter writer;
        for( int i = roundCount ; i > 0 ; i--) {
            Engine engine = new Engine(firstPlayer, secondPlayer);

            engine.run();

            try {
                writer = new PrintWriter(
                        (new FileOutputStream(new File("gameLog.txt"), true)));
                writer.print( engine.log.toString() );
                writer.print("--------------------------------------------" + engine.log.eol);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            statistics.add(engine.statistics);
        }
        showResultsOnCharts(statistics);

        System.out.print("Game ended. Check simulationLog.txt for further informations.");
    }

    private void showResultsOnCharts(List<GameStatistics> statistics){
        String appTitle = firstPlayer.name + " vs. " + secondPlayer.name;
        WinChart winChart = new WinChart(appTitle + " ; win ratio", "Win ratio", statistics);
        winChart.pack();
        winChart.setVisible(true);

        RoundWinSpline roundWinSpline = new RoundWinSpline(appTitle + " ; wins per round", "Wins per round", statistics);
        roundWinSpline.setLocation(520, 0);
        roundWinSpline.pack();
        roundWinSpline.setVisible(true);

        WinningDecisionPerRoundBarChart winningDecisionPerRoundBarChartForFirstPlayer = new WinningDecisionPerRoundBarChart(
                appTitle + " ; winning decision per round", "Decision wins per round for " +firstPlayer.name, statistics);
        winningDecisionPerRoundBarChartForFirstPlayer.setLocation(0, 310);
        winningDecisionPerRoundBarChartForFirstPlayer.pack();
        winningDecisionPerRoundBarChartForFirstPlayer.setVisible(true);

        GuardDecisionPerRoundBarChart guardDecisionPerRoundBarChart = new GuardDecisionPerRoundBarChart(
                appTitle + " ; winning guard play per round", "Guard decision wins per round for " +firstPlayer.name, statistics);
        guardDecisionPerRoundBarChart.setLocation(720, 310);
        guardDecisionPerRoundBarChart.pack();
        guardDecisionPerRoundBarChart.setVisible(true);
    }

    private void startSingleGame() {
        System.out.println("#################### Game Started! ####################");
        Engine engine = new Engine(firstPlayer, secondPlayer);

        engine.setLogPrintToConsole(true);
        if( firstPlayer instanceof Human || secondPlayer instanceof Human)
            engine.fullLog = false;
        engine.run();
        System.out.println("##################### Game ended #####################");
    }

}
