import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import dmalarczyk.masterThesis.gameModel.PlayerSpace;
import dmalarczyk.masterThesis.playerAlgorithm.GreedyAlgorithm;
import dmalarczyk.masterThesis.playerAlgorithm.RandomAlgorithm;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EngineTest {

    @Test
    public void playGame(){
        Engine engine = new Engine();
        engine.firstPlayer = new RandomAlgorithm( engine.roundState.spaceOfFirstPlayer);
        engine.secondPlayer = new RandomAlgorithm( engine.roundState.spaceOfSecondPlayer);

        engine.run();
        engine.closeGame();
        // engine.printCurrentProbabilityMap();

        assertTrue(engine.roundState.turnState == RoundState.TurnState.ended );
    }

    @Test
    public void guardPlayTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Engine engine = new Engine();
        engine.firstPlayer = new RandomAlgorithm( engine.roundState.spaceOfFirstPlayer);
        engine.secondPlayer = new RandomAlgorithm( engine.roundState.spaceOfSecondPlayer);

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
    public void playGameGreedyVsRandom(){
        Engine engine = new Engine();
        engine.firstPlayer = new RandomAlgorithm( engine.roundState.spaceOfFirstPlayer);
        engine.secondPlayer = new GreedyAlgorithm( engine.roundState.spaceOfSecondPlayer);

        engine.run();
        engine.closeGame();
        // engine.printCurrentProbabilityMap();

        assertEquals(RoundState.TurnState.ended, engine.roundState.turnState);
    }

    @Test
    public void playGameThousand() throws FileNotFoundException {
        PrintWriter logger = new PrintWriter(
                (new FileOutputStream( new File("simulationLog.txt"), true)));

        int firstPlayerWon = 0;
        int firstPlayerByComparison = 0;
        int secondPlayerWon = 0;
        int secondPlayerByComparison = 0;
        int wtfs = 0;
        int draws = 0;

        for( int i = 1000 ; i > 0 ; i--) {
            Engine engine = new Engine();

            engine.firstPlayer = new GreedyAlgorithm(engine.roundState.spaceOfFirstPlayer);
            engine.secondPlayer = new RandomAlgorithm(engine.roundState.spaceOfSecondPlayer);


            engine.run();
            engine.closeGame();
            // engine.printCurrentProbabilityMap();
            RoundState.Winner winner = engine.roundState.winner;

            if( winner == RoundState.Winner.firstPlayer){
                firstPlayerWon++;
                firstPlayerByComparison += engine.roundState.winByComparison ? 1 : 0;
            }
            else if( winner == RoundState.Winner.firstPlayer ){
                secondPlayerWon++;
                secondPlayerByComparison += engine.roundState.winByComparison ? 1 : 0;
            }
            else if( winner == RoundState.Winner.none)
                draws++;
            else
                wtfs++;
        }

        logger.println("firstPlayerWon" + firstPlayerWon +"/"+firstPlayerByComparison+ "; GreedyAlgorithm " + secondPlayerWon+"/"+ secondPlayerByComparison + "; Draws: " + draws + "; Wtfs: " + wtfs);
        logger.close();

        assertTrue(firstPlayerWon > secondPlayerWon);
    }

    @Test
    public void setRoundForPlay_isValid(){
        Engine engine = new Engine();
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
