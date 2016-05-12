package dmalarczyk.masterThesis.gameEngine;

import org.junit.Test;

import static org.junit.Assert.*;

public class EngineTest {

    @Test
    public void deck_contains16Cards_true(){
        Engine engine = new Engine();

        assertEquals(16, engine.deck.length);
    }

    @Test
    public void deck_contains16Cards_false(){
        Engine engine = new Engine();

        assertFalse(15 == engine.deck.length);
    }

    @Test
    public void testFoo() throws Exception {
        Engine engine = new Engine();

        assertEquals(10, engine.foo());
    }
}