package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GuardDecisionPerRoundBarChart extends JFrame {
    public GuardDecisionPerRoundBarChart(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        CategoryDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(700, 400));

        setContentPane(chartPanel);
    }

    private CategoryDataset createDataSet(List<GameStatistics> gameStatistics) {

        Map<Integer, Map<DecisionType, Integer>> decisionCountInRoundMap = new HashMap<>();

        Set<DecisionType> decisions = new HashSet<>();
        decisions.add(DecisionType.guard_priest);
        decisions.add(DecisionType.guard_baron);
        decisions.add(DecisionType.guard_handmaid);
        decisions.add(DecisionType.guard_prince);
        decisions.add(DecisionType.guard_king);
        decisions.add(DecisionType.guard_countess);
        decisions.add(DecisionType.guard_princess);

        for( int i = 1 ; i < 11 ; i++ ){
            Map<DecisionType, Integer> decisionCountMap = new HashMap<>();
            for( DecisionType decision :decisions){
                int count = 0;
                for(GameStatistics statistics : gameStatistics) {
                    if (statistics.winningMove == decision  && statistics.endingRound == i)
                        count++;
                }
                decisionCountMap.put(decision, count);
            }
            decisionCountInRoundMap.put(i, decisionCountMap);
        }

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for( int i = 1 ; i < 11 ; i++ ) {
            for (DecisionType decision : decisions) {
                dataSet.addValue( (Number) decisionCountInRoundMap.get(i).get(decision), decision.name() , i);
            }
        }

        return dataSet;
    }

    private JFreeChart createChart(CategoryDataset dataSet, String chartTitle) {
        JFreeChart chart = ChartFactory.createBarChart(
                chartTitle, "Round", "Wins", dataSet, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot =  chart.getCategoryPlot();

//        int seriesCount = plot.getDatasetCount();
//        for (int i = 0; i < seriesCount; i++) {
//            plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
//        }

        return chart;
    }
}
