package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.DecisionType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WinningDecisionPerRoundBarChart extends JFrame {
    public WinningDecisionPerRoundBarChart(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        CategoryDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(700, 400));

        setContentPane(chartPanel);

        try {
            ChartUtilities.saveChartAsPNG(new File("decision.png"), chart, 1050, 600, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CategoryDataset createDataSet(List<GameStatistics> gameStatistics) {

        Map<Integer, Map<DecisionType, Integer>> decisionCountInRoundMap = new HashMap<>();

        Set<DecisionType> decisions = new HashSet<>();
        decisions.add(DecisionType.priestPlay);
        decisions.add(DecisionType.baronPlay);
        decisions.add(DecisionType.handmaidPlay);
        decisions.add(DecisionType.prince_onMyself);
        decisions.add(DecisionType.prince_onOpponent);
        decisions.add(DecisionType.kingPlay);
        decisions.add(DecisionType.countessPlay);
        decisions.add(DecisionType.countess_withKingOrPrince);
        decisions.add(DecisionType.princessPlay);

        for( int i = 1 ; i < 11 ; i++ ){
            Map<DecisionType, Integer> decisionCountMap = new HashMap<>();
            for( DecisionType decision : decisions ){
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
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());

        return chart;
    }
}
