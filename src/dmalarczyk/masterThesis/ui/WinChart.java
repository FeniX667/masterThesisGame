package dmalarczyk.masterThesis.ui;

import dmalarczyk.masterThesis.gameEngine.GameStatistics;
import dmalarczyk.masterThesis.gameModel.RoundState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class WinChart extends JFrame {
    public WinChart(String applicationTitle, String chartTitle, List<GameStatistics> gameStatistics){
        super(applicationTitle);

        PieDataset dataSet = createDataSet(gameStatistics);

        JFreeChart chart = createChart(dataSet, chartTitle);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize( new Dimension(500, 270));

        setContentPane(chartPanel);

        try {
            ChartUtilities.saveChartAsPNG(new File("win.png"), chart, 1000, 540, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DefaultPieDataset  createDataSet(List<GameStatistics> gameStatistics) {
        int firstWins = 0;
        int secondWins = 0;
        int draws = 0;

        for(GameStatistics statistics : gameStatistics){
            if( statistics.winner == RoundState.Winner.firstPlayer)
                firstWins++;
            else if( statistics.winner == RoundState.Winner.secondPlayer)
                secondWins++;
            else
                draws++;
        }

        DefaultPieDataset result = new DefaultPieDataset ();
        result.setValue("Second player", secondWins);
        result.setValue("Draws", draws);
        result.setValue("First player", firstWins);
        return result;
    }

    private JFreeChart createChart(PieDataset dataSet, String chartTitle) {
        JFreeChart chart = ChartFactory.createPieChart(chartTitle, dataSet);
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(gen);
        plot.setDirection(Rotation.CLOCKWISE);
        return chart;
    }
}
