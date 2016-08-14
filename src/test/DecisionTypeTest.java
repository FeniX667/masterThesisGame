import dmalarczyk.masterThesis.gameModel.CardType;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class DecisionTypeTest {

    private int expectedAmountOfPlays;
    private List<DecisionType> expectedDecisions;
    private CardType firstCard;
    private CardType secondCard;

    public DecisionTypeTest(int expectedAmountOfPlays, List<DecisionType> expectedDecisions, CardType firstCard, CardType secondCard){
        this.expectedAmountOfPlays = expectedAmountOfPlays;
        this.expectedDecisions = expectedDecisions;
        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    @Parameterized.Parameters(name = "{index}: There is {0} decisions for hand ({2}, {3})")
    public static Iterable<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {7, Arrays.asList(DecisionType.guard_priest, DecisionType.guard_baron, DecisionType.guard_handmaid,
                        DecisionType.guard_prince, DecisionType.guard_king, DecisionType.guard_countess, DecisionType.guard_princess),
                CardType.guard, CardType.guard },
                {2, Arrays.asList(DecisionType.countessPlay, DecisionType.baronPlay),
                        CardType.countess, CardType.baron },
                {3, Arrays.asList(DecisionType.prince_onMyself, DecisionType.prince_onOpponent, DecisionType.countess_withKingOrPrince),
                        CardType.prince, CardType.countess },
                {2, Arrays.asList(DecisionType.kingPlay, DecisionType.countess_withKingOrPrince),
                        CardType.countess, CardType.king },
                {1, Arrays.asList(DecisionType.priestPlay),
                        CardType.priest, CardType.priest },
                {2, Arrays.asList(DecisionType.handmaidPlay, DecisionType.princessPlay),
                        CardType.handmaid, CardType.princess },
        });
    }

    @Test
    public void getDecisionsTestValidSets(){
        ArrayList<DecisionType> decisionList = DecisionType.getDecisions(firstCard, secondCard);
        assertTrue(decisionList.containsAll(expectedDecisions));
        assertEquals(expectedAmountOfPlays, decisionList.size() );
    }
}
