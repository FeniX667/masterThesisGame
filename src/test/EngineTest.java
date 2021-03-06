import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.RoundState;
import dmalarczyk.masterThesis.playerAlgorithm.*;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EngineTest {

    @Test
    public void playGame(){
        Player firstPlayer = new GreedyAlgorithm();
        Player secondPlayer = new RandomAlgorithm();

        Engine engine = new Engine(firstPlayer, secondPlayer);
        engine.run();

        try {
            PrintWriter writer = new PrintWriter(
                    (new FileOutputStream(new File("gameLog.txt"), true)));
            writer.print( engine.log.toString() );
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(engine.roundState.turnState == RoundState.TurnState.ended);
        assertTrue(engine.roundState.winner == RoundState.Winner.firstPlayer
                || engine.roundState.winner == RoundState.Winner.secondPlayer
                || engine.roundState.winner == RoundState.Winner.none);
    }

    @Test
    public void guardPlayTest() {
        Player firstPlayer = new MinMaxAlgorithm();
        Player secondPlayer = new GreedyAlgorithm();

        Engine engine = new Engine(firstPlayer, secondPlayer);

        engine.roundState.spaceOfFirstPlayer.hand.clear();
        engine.roundState.spaceOfFirstPlayer.hand.add(CardType.handmaid);

        engine.roundState.spaceOfSecondPlayer.hand.clear();
        engine.roundState.spaceOfSecondPlayer.hand.add(CardType.king);
        engine.roundState.spaceOfSecondPlayer.hand.add(CardType.guard);

        engine.currentPlayer = engine.secondPlayer;

        engine.makeMove(DecisionType.guard_king);

        assertEquals(RoundState.Winner.none, engine.roundState.winner);
    }

    @Test
    public void playGameThousand() throws FileNotFoundException {
        PrintWriter logger = new PrintWriter(
                (new FileOutputStream( new File("simulationLog.txt"), true)));

        int firstPlayerWon = 0;
        int firstPlayerByComparison = 0;
        int secondPlayerWon = 0;
        int secondPlayerByComparison = 0;
        int draws = 0;
        Player firstPlayer = new RandomAlgorithm();
        Player secondPlayer = new RandomAlgorithm();

        for( int i = 50000 ; i > 0 ; i--) {
            Engine engine = new Engine(firstPlayer, secondPlayer);

            engine.run();
            RoundState.Winner winner = engine.roundState.winner;

            if( winner == RoundState.Winner.firstPlayer){
                firstPlayerWon++;
                firstPlayerByComparison += engine.roundState.winByComparison ? 1 : 0;
            }
            else if( winner == RoundState.Winner.secondPlayer ){
                secondPlayerWon++;
                secondPlayerByComparison += engine.roundState.winByComparison ? 1 : 0;
            }
            else if( winner == RoundState.Winner.none)
                draws++;


            Engine engine2 = new Engine(secondPlayer, firstPlayer);

            engine2.run();
            RoundState.Winner winner2 = engine2.roundState.winner;

            if( winner2 == RoundState.Winner.secondPlayer){
                firstPlayerWon++;
                firstPlayerByComparison += engine2.roundState.winByComparison ? 1 : 0;
            }
            else if( winner2 == RoundState.Winner.firstPlayer ){
                secondPlayerWon++;
                secondPlayerByComparison += engine2.roundState.winByComparison ? 1 : 0;
            }
            else if( winner2 == RoundState.Winner.none)
                draws++;
        }

        logger.println(firstPlayer.name + " vs. " + secondPlayer.name + " = " + firstPlayerWon + "/" + secondPlayerWon + "; Wins by comparison (included) " + firstPlayerByComparison + "/" + secondPlayerByComparison + "; Draws: " + draws);
        logger.println();
        logger.close();

        // assertTrue(firstPlayerWon > secondPlayerWon);
    }

    @Test
    public void setRoundForPlay_isValid(){
        Player firstPlayer = new RandomAlgorithm();
        Player secondPlayer = new RandomAlgorithm();
        Engine engine = new Engine(firstPlayer, secondPlayer);
        RoundState roundState = engine.roundState;

        assertEquals(0, roundState.spaceOfFirstPlayer.discardedDeck.size());
        assertEquals(0, roundState.spaceOfSecondPlayer.discardedDeck.size());
        assertEquals(3, roundState.openDiscardedCards.size());
        assertEquals(1, roundState.spaceOfFirstPlayer.hand.size());
        assertEquals(1, roundState.spaceOfSecondPlayer.hand.size());
        assertEquals(10, roundState.deck.size());
        assertTrue(roundState.hiddenDiscardedCard != null);
    }

    @Test
    public void princessPlay_endsGame(){

    }

    @Test
    public void guardPlay_hit_endsGame(){

    }

    @Test
    public void guardPlay_miss_endsGame_False(){

    }

    @Test
    public void baron_draw_endsGame_False(){

    }

    @Test
    public void baron_win_endsGame(){

    }

    @Test
    public void baron_lose_endsGame(){

    }

    @Test
    public void emptyDeck_higherHandWinsGame(){

    }

    @Test
    public void emptyDeck_lowerHandWinsGame_False(){

    }

    @Test
    public void emptyDeck_sameHandWinsGame_False(){

    }

    @Test
    public void handmaidPlay_grantsImmunity(){

    }

}
