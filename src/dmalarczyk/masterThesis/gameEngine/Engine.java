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
    public Player playerA;
    public Player playerB;
    public Player currentPlayer, opponentPlayer;
    public GameStatistics statistics ;
    public int iteration;
    Random rnd;
    PrintWriter logger;

    public Engine(){
        rnd = new Random();
        roundState = new RoundState();
        statistics = new GameStatistics();
        iteration = 1;

        try {
            logger = new PrintWriter(
                    (new FileOutputStream( new File("gameLog.txt"), true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRoundForPlay(){
        roundState.hiddenDiscardedCard = drawRandomCardFromDeck() ;
        for(int i=3 ; i!=0 ; i--){
            roundState.openDiscardedCards.add( drawRandomCardFromDeck() );
        }

        roundState.spaceOfPlayerA.hand.add( drawRandomCardFromDeck() );
        roundState.spaceOfPlayerB.hand.add( drawRandomCardFromDeck() );
    }

    public void run(){
        roundState.switchState();
        do{
            iterate();
        }while(roundState.turnState == RoundState.TurnState.playerA || roundState.turnState == RoundState.TurnState.playerB);
        logger.println(statistics.howGameEnded + " in round " + statistics.endingRound + " with " + statistics.winningMove);
    }

    public void closeGame(){
        logger.println("--------------------------------------------------------------");
        logger.close();
    }

    public void iterate(){
        DecisionType decision;



        if (roundState.turnState == RoundState.TurnState.playerA){
            currentPlayer = playerA;
            opponentPlayer = playerB;
            roundState.spaceOfPlayerA.hand.add(drawRandomCardFromDeck());
            decision = currentPlayer.makeDecision(roundState, DecisionType.getDecisions(roundState.spaceOfPlayerA.hand));
        }
        else{
            currentPlayer = playerB;
            opponentPlayer = playerA;
            roundState.spaceOfPlayerB.hand.add(drawRandomCardFromDeck());
            decision = currentPlayer.makeDecision(roundState, DecisionType.getDecisions(roundState.spaceOfPlayerB.hand));
        }
        logger.println(roundState.spaceOfPlayerA.hand + " ; " + roundState.spaceOfPlayerB.hand + " ; " + roundState.turnState + ": " + decision);
        statistics.winningMove = decision;

        currentPlayer.playerSpace.safetyFlag = false;
        makeMove(decision);

        if( roundState.deck.size() == 0 ){
            endGame( null );
            return;
        }

        roundState.switchState();

        iteration++;
    }

    public void makeMove(DecisionType decision){
        PlayerSpace decisionMakerSpace, opponentSpace;
        if(currentPlayer == playerA){
            decisionMakerSpace = roundState.spaceOfPlayerA;
            opponentSpace = roundState.spaceOfPlayerB;
        }else{
            decisionMakerSpace = roundState.spaceOfPlayerB;
            opponentSpace = roundState.spaceOfPlayerA;
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
        discardPlayedCard(decisionMakerSpace, CardType.guard);
        if( !opponentSpace.safetyFlag)
            if( opponentSpace.hand.contains(guardChoice)){
                endGame(currentPlayer);
            }
    }

    private void priestPlay(PlayerSpace decisionMakerSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.priest);
    }

    private void baronPlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.baron);

        if( !opponentSpace.safetyFlag)
            if ( CardType.getStrength(decisionMakerSpace.hand.get(0)) > CardType.getStrength(opponentSpace.hand.get(0)) )
                endGame(currentPlayer);
            else if ( CardType.getStrength(decisionMakerSpace.hand.get(0)) < CardType.getStrength(opponentSpace.hand.get(0)) )
                endGame(opponentPlayer);
    }

    private void handmaidPlay(PlayerSpace decisionMakerSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.handmaid);
        decisionMakerSpace.safetyFlag=true;
    }

    private void princePlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.prince);
        if(opponentSpace == null){
            CardType princedCard = decisionMakerSpace.hand.get(0);
            discardPlayedCard(decisionMakerSpace, princedCard);
            if( princedCard == CardType.princess )
                endGame(opponentPlayer);
            else{
                if( roundState.deck.size() == 0 ){
                    endGame( null );
                }
                else
                    decisionMakerSpace.hand.add(drawRandomCardFromDeck());
            }
        }else if( !opponentSpace.safetyFlag){
            CardType princedCard = opponentSpace.hand.get(0);
            discardPlayedCard(opponentSpace, princedCard);
            if( princedCard == CardType.princess )
                endGame(currentPlayer);
            else{
                if( roundState.deck.size() == 0 ){
                    endGame( null );
                }
                else
                    opponentSpace.hand.add(drawRandomCardFromDeck());
            }
        }
    }

    private void kingPlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.king);
        if( !opponentSpace.safetyFlag) {
            CardType secondCard = decisionMakerSpace.hand.remove(0);
            decisionMakerSpace.hand.add(opponentSpace.hand.remove(0));
            opponentSpace.hand.add(secondCard);
        }
    }

    private void countessPlay(PlayerSpace decisionMakerSpace ) {
        discardPlayedCard(decisionMakerSpace, CardType.countess);
    }

    private void princessPlay(PlayerSpace decisionMakerSpace) {
        discardPlayedCard(decisionMakerSpace, CardType.princess);
        endGame(opponentPlayer);
    }

    private void endGame(Player winner) {
        if( winner == null ){
            int playerAStrength = roundState.spaceOfPlayerA.hand.size() == 0 ? 0 : CardType.getStrength(roundState.spaceOfPlayerA.hand.get(0));
            int playerBStrength = roundState.spaceOfPlayerB.hand.size() == 0 ? 0 : CardType.getStrength(roundState.spaceOfPlayerB.hand.get(0));
            if( playerAStrength > playerBStrength)
                roundState.turnState = RoundState.TurnState.playerAWonByCompare;
            else if( playerAStrength < playerBStrength)
                roundState.turnState = RoundState.TurnState.playerBWonByCompare;
            else
                roundState.turnState = RoundState.TurnState.draw;
        }

        else if( winner == playerA )
            roundState.turnState = RoundState.TurnState.playerAWon;
        else
            roundState.turnState = RoundState.TurnState.playerBWon;

        statistics.endingRound = iteration;
        statistics.howGameEnded = roundState.turnState;
    }

    private void discardPlayedCard(PlayerSpace space, CardType cardType){
        space.hand.remove(cardType);
        space.discardedDeck.add(cardType);
    }

    private CardType drawRandomCardFromDeck(){
        if( roundState.deck.size() == 0 ){
            endGame( null );
        }

        return roundState.deck.remove(rnd.nextInt(roundState.deck.size()));
    }

    public void printCurrentProbabilityMapForPlayer(PlayerSpace playerSpace) {
        logger.println(roundState.getProbabilityMapForPlayer(playerSpace));
    }
}
