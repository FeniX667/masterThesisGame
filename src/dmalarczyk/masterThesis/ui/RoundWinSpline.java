package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Malar on 2016-06-20.
 */
public class RoundWinSpline extends JFrame {
    public RoundWinSpline(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        XYDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(500, 270));

        setContentPane(chartPanel);
    }

    private XYDataset createDataSet(List<GameStatistics> gameStatistics) {
        int[] averageWinsInTurn = new int[12];
        int[] firstPlayerWinsInTurn = new int[12];
        int[] secondPlayerWinsInTurn = new int[12];

        for(int i = 0; i < 12 ; i++){
            averageWinsInTurn[i] = 0;
            firstPlayerWinsInTurn[i] = 0;
            secondPlayerWinsInTurn[i] = 0;
        }

        Map<Integer, Integer> averageWinMap = new HashMap<>();
        Map<Integer, Integer> firstPlayerWinMap = new HashMap<>();
        Map<Integer, Integer> secondPlayerWinMap = new HashMap<>();

        for(GameStatistics statistics : gameStatistics) {
            if (statistics.winner == RoundState.Winner.firstPlayer) {
                firstPlayerWinsInTurn[statistics.endingRound]++;
                averageWinsInTurn[statistics.endingRound]++;
            } else if( statistics.winner == RoundState.Winner.secondPlayer){
                secondPlayerWinsInTurn[statistics.endingRound]++;
                averageWinsInTurn[statistics.endingRound]++;
            }
        }

        XYSeries averageSeries = new XYSeries("Average wins");
        XYSeries firstSeries = new XYSeries("First player wins");
        XYSeries secondSeries = new XYSeries("Second player wins");

        for(int i = 0 ; i < 12 ; i++){
            averageSeries.add(i, averageWinsInTurn[i]/2);
            firstSeries.add(i, firstPlayerWinsInTurn[i]);
            secondSeries.add(i, secondPlayerWinsInTurn[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(averageSeries);
        dataset.addSeries(firstSeries);
        dataset.addSeries(secondSeries);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataSet, String chartTitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Round", "Wins", dataSet, PlotOrientation.VERTICAL, true, true, false );

        XYPlot plot =  chart.getXYPlot();

        int seriesCount = plot.getSeriesCount();
        for (int i = 0; i < seriesCount; i++) {
            plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
        }

        return chart;
    }
}
