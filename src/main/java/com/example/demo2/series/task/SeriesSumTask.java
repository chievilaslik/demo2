package com.example.demo2.series.task;

import com.example.demo2.series.processor.SeriesProcessor;
import java.util.concurrent.RecursiveTask;

/**
 * Задача для вычисления суммы числового ряда.
 */
public class SeriesSumTask extends RecursiveTask<Double> {
    private final String function;
    private final double startPoint;
    private final int startIndex;
    private final int endIndex;
    private final double epsilon;
    private final int threshold;

    public SeriesSumTask(String function, double startPoint, int startIndex, int endIndex, double epsilon, int numCores) {
        this.function = function;
        this.startPoint = startPoint;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.epsilon = epsilon;
        int range = endIndex - startIndex;
        this.threshold = computeThreshold(range, numCores);
    }

    @Override
    protected Double compute() {
        int range = endIndex - startIndex;
        if (range <= threshold) {
            double sum = 0.0;
            double term;
            int n = startIndex;

            while (true) {
                term = SeriesProcessor.calculateTerm(function, startPoint, n);
                if (Math.abs(term) <= epsilon || n > endIndex) {
                    break;
                }
                sum += term;
                n++;
            }

            return sum;
        } else {
            int midIndex = (startIndex + endIndex) / 2;
            SeriesSumTask leftTask = new SeriesSumTask(function, startPoint, startIndex, midIndex, epsilon, threshold);
            SeriesSumTask rightTask = new SeriesSumTask(function, startPoint, midIndex + 1, endIndex, epsilon, threshold);

            leftTask.fork();
            double rightSum = rightTask.compute();
            double leftSum = leftTask.join();

            return leftSum + rightSum;
        }
    }

    private static int computeThreshold(int range, int numCores) {
        int threshold = (int)Math.ceil(range / (double)(2 * numCores));
        int minThreshold = 100;
        int maxThreshold = range / 2;
        return Math.min(maxThreshold, Math.max(threshold, minThreshold));
    }
}
