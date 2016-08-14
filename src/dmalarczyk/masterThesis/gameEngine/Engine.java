package dmalarczyk.masterThesis.gameEngine;


import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.gameModel.RoundState;
import dmalarczyk.masterThesis.playerAlgorithm.Human;
import dmalarczyk.masterThesis.playerAlgorithm.Player;

public class Engine {
    public RoundState roundState;
    public Player firstPlayer;
    public Player secondPlayer;
    public Player currentPlayer, opponentPlayer;
    public GameStatistics statistics ;
    public int iteration;
    public SimpleLogger log;
    public boolean fullLog;

    public Engine(Player firstPlayer, Player secondPlayer){
        roundState = new RoundState();
        initVariables();
        setPlayers(firstPlayer, secondPlayer);
    }

    public Engine(Player firstPlayer, Player secondPlayer, RoundState roundState){
        this.roundState = roundState;
        initVariables();
        setPlayers(firstPlayer, secondPlayer);
    }

    private void setPlayers(Player firstPlayer, Player secondPlayer){
        firstPlayer.setPlayerSpace(roundState.spaceOfFirstPlayer);
        secondPlayer.setPlayerSpace(roundState.spaceOfSecondPlayer);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    private void initVariables(){
        statistics = new GameStatistics();
        iteration = 1;
        log = new SimpleLogger();
        fullLog = true;
    }

    public void run(){
        log.appendln(firstPlayer.name + " vs. " + secondPlayer.name);
        log.append("Discarded cards: " + roundState.openDiscardedCards);
        if( fullLog )
            log.append(". Hidden card: " + roundState.hiddenDiscardedCard);
        log.appendln("");
        if( firstPlayer instanceof Human)
            log.appendln("Human player starts with: " + firstPlayer.playerSpace.hand);
        else if( secondPlayer instanceof Human)
            log.appendln("Human player starts with: " + secondPlayer.playerSpace.hand);

        pause( 1000 );

        do{
            iterate();
            pause( 800 );
        }while(roundState.turnState != RoundState.TurnState.ended);

        logResult();
    }


    public void iterate(){
        DecisionType decision;

        setCurrentAndOpponentSpace();

        roundState.drawCardForPlayerSpace(currentPlayer.playerSpace);

        decision = getCurrentDecisionFromPlayer();


        log.append(iteration + " ");
        if(fullLog){
           log.append(roundState.spaceOfFirstPlayer.hand + " ; " + roundState.spaceOfSecondPlayer.hand + " ; ");
        }
        log.append(roundState.turnState + ": " + decision + ".");
        if( opponentPlayer.playerSpace.isSafe )
            log.append(" Opponent is safe.");

        makeMove(decision);
        log.appendln("");

        statistics.winningMove = decision;
        if( roundState.deck.size() == 0 && roundState.turnState != RoundState.TurnState.ended){
            endGame(null);
            return;
        }
        roundState.switchState();
        iteration++;
    }

    public void setCurrentAndOpponentSpace(){
        if( roundState.turnState == RoundState.TurnState.notStarted )
            roundState.switchState();
        if (roundState.turnState == RoundState.TurnState.firstPlayer){
            currentPlayer = firstPlayer;
            opponentPlayer = secondPlayer;
        }
        else if (roundState.turnState == RoundState.TurnState.secondPlayer){
            currentPlayer = secondPlayer;
            opponentPlayer = firstPlayer;
        }
        currentPlayer.playerSpace.isSafe = false;
    }

    public DecisionType getCurrentDecisionFromPlayer(){
        return currentPlayer.makeDecision(new RoundState(roundState, currentPlayer.playerSpace), DecisionType.getDecisions(currentPlayer.playerSpace.hand));
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
                priestPlay(decisionMakerSpace, opponentSpace);
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
            case countessPlay:
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

    private void priestPlay(PlayerSpace decisionMakerSpace, PlayerSpace opponentSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.priest);
        if( !opponentSpace.isSafe){
            decisionMakerSpace.knownEnemyCard = opponentSpace.hand.get(0);
            if( !fullLog )
                log.append(" Opponent has " + decisionMakerSpace.knownEnemyCard + " in his hand");
        }
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
            decisionMakerSpace.knownEnemyCard = opponentSpace.hand.get(0);
            opponentSpace.knownEnemyCard = decisionMakerSpace.hand.get(0);

            if( !fullLog )
                log.append(" Players switched " + decisionMakerSpace.knownEnemyCard + " for " + opponentSpace.knownEnemyCard + ".");
        }
    }

    private void countessPlay(PlayerSpace decisionMakerSpace ) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.countess);
    }

    private void princessPlay(PlayerSpace decisionMakerSpace) {
        roundState.discardPlayedCard(decisionMakerSpace, CardType.princess);
        endGame(opponentPlayer);
    }

    public void endGame(Player winner) {
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

    private void logResult() {
        if( statistics.winner == RoundState.Winner.none)
            log.append("Draw in round " + statistics.endingRound + ". ");
        else{
            if(statistics.winner == RoundState.Winner.firstPlayer)
                log.append(firstPlayer.name + "(1st player) won the game. ");
            else
                log.append(secondPlayer.name + "(2nd player) won the game. ");
            if( statistics.isWinByComparison)
                log.appendln("Win by comparison.");
            else
                log.appendln("Ending move: " + statistics.winningMove);
        }

        log.appendln("");
        log.appendln("Cards remaining in deck: " + roundState.deck);
        log.appendln("First player discarded: " + firstPlayer.playerSpace.discardedDeck);
        log.appendln("Second player discarded: " + secondPlayer.playerSpace.discardedDeck);
    }

    public void setLogPrintToConsole(boolean flag){
        log.printToConsole = flag;
    }

    public void pause(int miliseconds){
        if( !fullLog )
            try {
                Thread.sleep(miliseconds);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
    }
}
