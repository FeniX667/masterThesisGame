package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WinChart extends JFrame {
    public WinChart(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        PieDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(500, 270));

        setContentPane(chartPanel);
    }

    private DefaultPieDataset  createDataSet(List<GameStatistics> gameStatistics) {
        int firstWins = 0;
        int secondWins = 0;

        for(GameStatistics statistics : gameStatistics){
            if( statistics.winner == RoundState.Winner.firstPlayer)
                firstWins++;
            else
                secondWins++;
        }

        DefaultPieDataset result = new DefaultPieDataset ();
        result.setValue("First player", firstWins);
        result.setValue("Second player", secondWins);

        return result;
    }

    private JFreeChart createChart(PieDataset dataSet, String chartTitle) {
        JFreeChart chart = ChartFactory.createPieChart(chartTitle, dataSet);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDirection(Rotation.CLOCKWISE);
        return chart;
    }
}
