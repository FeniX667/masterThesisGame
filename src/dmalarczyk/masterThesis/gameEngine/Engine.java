package dmalarczyk.masterThesis.gameEngine;


import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;
import dmalarczyk.masterThesis.playerAlgorithm.Player;

import java.io.*;
import java.util.Random;

public class Engine {
    public RoundState roundState;
    public Player firstPlayer;
    public Player secondPlayer;
    public Player currentPlayer, opponentPlayer;
    public GameStatistics statistics ;
    public int iteration;
    Random rnd;
    PrintWriter logger;

    public Engine(Player firstPlayer, Player secondPlayer){
        initVariables();
        setPlayers(firstPlayer, secondPlayer);
    }

    public void setPlayers(Player firstPlayer, Player secondPlayer){
        firstPlayer.setPlayerSpace(roundState.spaceOfFirstPlayer);
        secondPlayer.setPlayerSpace(roundState.spaceOfSecondPlayer);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    private void initVariables(){
        rnd = new Random();
        roundState = new RoundState();
        statistics = new GameStatistics();
        iteration = 1;

        try {
            logger = new PrintWriter(
                    (new FileOutputStream(new File("gameLog.txt"), true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void run(){
        roundState.switchState();
        logger.println(firstPlayer.name + " vs. " + secondPlayer.name);
        logger.println("Discarded cards: " + roundState.openDiscardedCards + ". Hidden card: " + roundState.hiddenDiscardedCard);
        do{
            iterate();
        }while(roundState.turnState != RoundState.TurnState.ended);

        logger.print(statistics.winner + " won in round " + statistics.endingRound + ". ");
        if( statistics.isWinByComparison )
            logger.println("Win by comparison.");
        else
            logger.println("Winning move: " + statistics.winningMove);

        logger.println("Cards remaining in deck: " + roundState.deck );
    }

    public void iterate(){
        DecisionType decision;

        if (roundState.turnState == RoundState.TurnState.firstPlayer){
            currentPlayer = firstPlayer;
            opponentPlayer = secondPlayer;
        }
        else if (roundState.turnState == RoundState.TurnState.secondPlayer){
            currentPlayer = secondPlayer;
            opponentPlayer = firstPlayer;
        }

        currentPlayer.playerSpace.isSafe = false;
        roundState.drawCardForPlayerSpace(currentPlayer.playerSpace);
        decision = currentPlayer.makeDecision(new RoundState(roundState, currentPlayer.playerSpace), DecisionType.getDecisions(currentPlayer.playerSpace.hand));

        logger.println(iteration + " " + roundState.spaceOfFirstPlayer.hand + " ; " + roundState.spaceOfSecondPlayer.hand + " ; " + roundState.turnState + ": " + decision);

        makeMove(decision);
        statistics.winningMove = decision;

        if( roundState.deck.size() == 0 && roundState.turnState != RoundState.TurnState.ended){
            endGame(null);
            return;
        }

        roundState.switchState();

        iteration++;
    }

    public void makeMove(DecisionType decision){
        PlayerSpace decisionMakerSpace, opponentSpace;
        if(currentPlayer == firstPlayer){
            decisionMakerSpace = roundState.spaceOfFirstPlayer;
            opponentSpace = roundState.spaceOfSecondPlayer;
        }else{
            decisionMakerSpace = roundState.spaceOfSecondPlayer;
            opponentSpace = roundState.spaceOfFirstPlayer;
        }

        switch(decision){
            case guard_priest:
                guardPlay(CardType.priest, decisionMakerSpace, opponentSpace);
                break;
            case guard_baron:
                guardPlay(CardType.baron, decisionMakerSpace, opponentSpace);
                break;
            case guard_handmaid:
                guardPlay(CardType.handmaid, decisionMakerSpace, opponentSpace);
                break;
            case guard_prince:
                guardPlay(CardType.prince, decisionMakerSpace, opponentSpace);
                break;
            case guard_king:
                guardPlay(CardType.king, decisionMakerSpace, opponentSpace);
                break;
            case guard_countess:
                guardPlay(CardType.countess, decisionMakerSpace, opponentSpace);
                break;
            case guard_princess:
                guardPlay(CardType.princess, decisionMakerSpace, opponentSpace);
                break;
            case priestPlay:
                priestPlay(decisionMakerSpace);
                break;
            case baronPlay:
                baronPlay(decisionMakerSpace, opponentSpace);
                break;
            case handmaidPlay:
                handmaidPlay(decisionMakerSpace);
                break;
            case prince_onMyself:
                princePlay(decisionMakerSpace, null);
                break;
            case prince_onOpponent:
                princePlay(decisionMakerSpace, opponentSpace);
                break;
            case kingPlay:
                kingPlay(decisionMakerSpace, opponentSpace);
                break;
            case countess_defaultPlay:
                countessPlay(decisionMakerSpace);
                break;
            case countess_withKingOrPrince:
                countessPlay(decisionMakerSpace);
                break;
            case princessPlay:
                princessPlay(decisionMakerSpace);
                break;
        }
    }

    private void guardPlay(CardType guardChoice, PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace){
        roundState.discardPlayedCard(decisionMakerSpace, CardType.guard);
        if( !opponentSpace.isSafe && opponentSpace.hand.contains(guardChoice))
                endGame(currentPlayer);
    }

    private void priestPlay(PlayerSpace decisionMakerSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.priest);
    }

    private void baronPlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.baron);

        if( !opponentSpace.isSafe)
            if ( CardType.getStrength(decisionMakerSpace.hand.get(0)) > CardType.getStrength(opponentSpace.hand.get(0)) )
                endGame(currentPlayer);
            else if ( CardType.getStrength(decisionMakerSpace.hand.get(0)) < CardType.getStrength(opponentSpace.hand.get(0)) )
                endGame(opponentPlayer);
    }

    private void handmaidPlay(PlayerSpace decisionMakerSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.handmaid);
        decisionMakerSpace.isSafe = true;
    }

    private void princePlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.prince);
        if(opponentSpace == null){
            CardType princedCard = decisionMakerSpace.hand.get(0);
            roundState.discardPlayedCard(decisionMakerSpace, princedCard);
            if( princedCard == CardType.princess )
                endGame(opponentPlayer);
            else{
                roundState.drawCardForPlayerSpace(decisionMakerSpace);
            }
        }else if( !opponentSpace.isSafe){
            CardType princedCard = opponentSpace.hand.get(0);
            roundState.discardPlayedCard(opponentSpace, princedCard);
            if( princedCard == CardType.princess )
                endGame(currentPlayer);
            else{
                roundState.drawCardForPlayerSpace(opponentSpace);
            }
        }
    }

    private void kingPlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.king);
        if( !opponentSpace.isSafe) {
            CardType secondCard = decisionMakerSpace.hand.remove(0);
            decisionMakerSpace.hand.add(opponentSpace.hand.remove(0));
            opponentSpace.hand.add(secondCard);
        }
    }

    private void countessPlay(PlayerSpace decisionMakerSpace ) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.countess);
    }

    private void princessPlay(PlayerSpace decisionMakerSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.princess);
        endGame(opponentPlayer);
    }

    private void endGame(Player winner) {
        roundState.turnState = RoundState.TurnState.ended;
        if( winner == null ){
            int playerAStrength = CardType.getStrength(roundState.spaceOfFirstPlayer.hand.get(0)) ;
            int playerBStrength = CardType.getStrength(roundState.spaceOfSecondPlayer.hand.get(0)) ;
            if( playerAStrength > playerBStrength){
                roundState.winner = RoundState.Winner.firstPlayer;
                roundState.winByComparison = true;
            }
            else if( playerAStrength < playerBStrength){
                roundState.winner = RoundState.Winner.secondPlayer;
                roundState.winByComparison = true;
            }
            else
                roundState.winner = RoundState.Winner.none;
        }

        else if( winner == firstPlayer)
            roundState.winner = RoundState.Winner.firstPlayer;
        else
            roundState.winner = RoundState.Winner.secondPlayer;

        statistics.endingRound = iteration;
        statistics.winner = roundState.winner;
    }

    public void printCurrentProbabilityMapForPlayer(PlayerSpace playerSpace) {
        logger.println(roundState.getProbabilityMapForPlayer(playerSpace));
    }

    public void closeGame(){
        logger.println("--------------------------------------------------------------");
        logger.close();
    }
}
