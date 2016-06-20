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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinningDecisionBarChart extends JFrame {
    public WinningDecisionBarChart(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        CategoryDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(500, 270));

        setContentPane(chartPanel);
    }

    private CategoryDataset createDataSet(List<GameStatistics> gameStatistics) {

        Map<Integer, Map<DecisionType, Integer>> decisionCountInRoundMap = new HashMap<>();


        for( int i = 0 ; i < 12 ; i++ ){
            Map<DecisionType, Integer> decisionCountMap = new HashMap<>();
            for( DecisionType decision : EnumSet.allOf(DecisionType.class) ){
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
        for( int i = 0 ; i < 12 ; i++ ) {
            for (DecisionType decision : EnumSet.allOf(DecisionType.class)) {
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
