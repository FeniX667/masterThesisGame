package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RoundWinSpline extends JFrame {
    public RoundWinSpline(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        XYDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(500, 270));

        setContentPane(chartPanel);

        try {
            String firstPlayerName = gameStatistics.get(0).firstPlayerAcronym;
            String secondPlayerName = gameStatistics.get(0).secondPlayerAcronym;
            ChartUtilities.saveChartAsPNG(new File(firstPlayerName+ "Vs" +secondPlayerName+ "RoundWin.png"), chart, 1000, 540, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        for(GameStatistics statistics : gameStatistics) {
            if (statistics.winner == RoundState.Winner.firstPlayer) {
                firstPlayerWinsInTurn[statistics.endingRound]++;
                averageWinsInTurn[statistics.endingRound]++;
            } else if( statistics.winner == RoundState.Winner.secondPlayer){
                secondPlayerWinsInTurn[statistics.endingRound]++;
                averageWinsInTurn[statistics.endingRound]++;
            }
        }

        XYSeries secondSeries = new XYSeries(gameStatistics.get(0).secondPlayerName + " wins ");
        XYSeries averageSeries = new XYSeries("Average wins");
        XYSeries firstSeries = new XYSeries(gameStatistics.get(0).firstPlayerName + " wins");

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
        NumberAxis numberAxis = (NumberAxis)plot.getDomainAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }
}
