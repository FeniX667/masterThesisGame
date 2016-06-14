import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.playerAlgorithm.RandomAlgorithm;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EngineTest {

    @Test
    public void playGame(){
        Engine engine = new Engine();
        engine.setRoundForPlay();
        engine.playerA = new RandomAlgorithm( engine.roundState.spaceOfPlayerA );
        engine.playerB = new RandomAlgorithm( engine.roundState.spaceOfPlayerB );

        engine.run();
        engine.closeGame();
        // engine.printCurrentProbabilityMap();

        assertTrue(engine.roundState.turnState != RoundState.TurnState.playerA && engine.roundState.turnState != RoundState.TurnState.playerB );
    }

    @Test
    public void playGameThousand(){
        for( int i = 1000 ; i > 0 ; i--) {
            Engine engine = new Engine();
            engine.setRoundForPlay();
            engine.playerA = new RandomAlgorithm(engine.roundState.spaceOfPlayerA);
            engine.playerB = new RandomAlgorithm(engine.roundState.spaceOfPlayerB);

            engine.run();
            engine.closeGame();
            // engine.printCurrentProbabilityMap();

            assertTrue(engine.roundState.turnState != RoundState.TurnState.playerA && engine.roundState.turnState != RoundState.TurnState.playerB);
        }
    }

    @Test
    public void setRoundForPlay_isValid(){
        Engine engine = new Engine();
        engine.setRoundForPlay();
        RoundState roundState = engine.roundState;

        assertEquals(0, roundState.spaceOfPlayerA.discardedDeck.size());
        assertEquals(0, roundState.spaceOfPlayerB.discardedDeck.size());
        assertEquals(3, roundState.openDiscardedCards.size());
        assertEquals(1, roundState.spaceOfPlayerA.hand.size());
        assertEquals(1, roundState.spaceOfPlayerB.hand.size());
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
