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
    private static final int THRESHOLD = 1000;

    public SeriesSumTask(String function, double startPoint, int startIndex, int endIndex, double epsilon) {
        this.function = function;
        this.startPoint = startPoint;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.epsilon = epsilon;
    }

    @Override
    protected Double compute() {
        int range = endIndex - startIndex;
        if (range <= THRESHOLD) {
            double sum = 0.0;
            double term;
            int n = startIndex;

            do {
                term = SeriesProcessor.calculateTerm(function, startPoint, n);
                sum += term;
                n++;

                if (n > endIndex) {
                    return sum;
                }
            } while (Math.abs(term) > epsilon);

            return sum;
        } else {
            int midIndex = (startIndex + endIndex) / 2;
            SeriesSumTask leftTask = new SeriesSumTask(function, startPoint, startIndex, midIndex, epsilon);
            SeriesSumTask rightTask = new SeriesSumTask(function, startPoint, midIndex + 1, endIndex, epsilon);

            leftTask.fork();
            double rightSum = rightTask.compute();
            double leftSum = leftTask.join();

            return leftSum + rightSum;
        }
    }
}
