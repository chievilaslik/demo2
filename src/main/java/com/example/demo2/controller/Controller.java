package com.example.demo2.controller;

import com.example.demo2.series.processor.SeriesProcessor;
import com.example.demo2.limit.processor.LimitProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

/**
 * Контроллер для обработки действий пользователя и выполнения расчетов
 * суммы ряда и предела функции.
 */
public class Controller {
    @FXML
    private TextField functionInput;

    @FXML
    private Label executionTimeLabel;

    @FXML
    private TextField startPointInput;

    @FXML
    private TextField nTermsInput;

    @FXML
    private TextField limitXInput;

    @FXML
    private Label resultLabel;

    @FXML
    private Button calculateSeriesButton;

    @FXML
    private Button calculateLimitButton;

    /**
     * Обрабатывает клик по кнопке "Calculate Series" и выполняет расчет суммы ряда.
     */
    @FXML
    public void onCalculateSeriesButtonClick() {
        try {
            long startTime = System.nanoTime();

            String function = functionInput.getText();
            double startPoint = Double.parseDouble(startPointInput.getText());
            String nTermsText = nTermsInput.getText();
            int nTerms;
            if (nTermsText.equalsIgnoreCase("inf")) {
                nTerms = 1000000;
            } else {
                nTerms = Integer.parseInt(nTermsText);
            }

            double result = SeriesProcessor.processSeries(function, startPoint, nTerms);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;

            executionTimeLabel.setText("Execution time: " + duration + " ms");

            String formattedResult = String.format("%.10f", result);
            resultLabel.setText(formattedResult);

        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please check your input values.");
        } catch (RuntimeException e) {
            resultLabel.setText("An error occurred. Please try again.");
        } catch (Exception e) {
            resultLabel.setText("An error occurred. Please try again.");
        }
    }

    /**
     * Обрабатывает клик по кнопке "Calculate Limit" и выполняет расчет предела функции.
     */
    @FXML
    public void onCalculateLimitButtonClick() {
        try {

            long startTime = System.nanoTime();

            String function = functionInput.getText();
            String limit = limitXInput.getText();
            double epsilon = 1e-9;

            double limitValue;
            if (limit.equalsIgnoreCase("inf")) {
                limitValue = Double.POSITIVE_INFINITY;
            } else if (limit.equalsIgnoreCase("-inf")) {
                limitValue = Double.NEGATIVE_INFINITY;
            } else {
                limitValue = Double.parseDouble(limit);
            }

            double result = LimitProcessor.calculateLimit(function, limitValue, epsilon);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;

            executionTimeLabel.setText("Execution time: " + duration + " ms");

            if (Double.isInfinite(result)) {
                resultLabel.setText("Infinity");
            } else {
                String formattedResult = String.format("%.10f", result);
                resultLabel.setText(formattedResult);
            }

        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please check your input values.");
        } catch (RuntimeException e) {
            resultLabel.setText("An error occurred. Please try again.");
        } catch (Exception e) {
            resultLabel.setText("An error occurred. Please try again.");
        }
    }
}
