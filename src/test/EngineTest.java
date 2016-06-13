import dmalarczyk.masterThesis.gameEngine.Engine;
import dmalarczyk.masterThesis.gameEngine.RandomAlgorithm;
import dmalarczyk.masterThesis.model.RoundState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EngineTest {

    @Test
    public void playGame(){
        Engine engine = new Engine();
        engine.setRoundForPlay();
        engine.playerA = new RandomAlgorithm();
        engine.playerB = new RandomAlgorithm();

        engine.run();

        assertTrue(engine.roundState.turnState == RoundState.TurnState.playerAWon || engine.roundState.turnState == RoundState.TurnState.playerBWon);
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
