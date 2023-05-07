package com.example.demo2.limit.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class LimitCalculator {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static double calculateLimit(Function<Double, Double> function, double argument, double epsilon) {
        return calculateLimitRecursive(forkJoinPool, function, argument, epsilon);
    }

    private static double calculateLimitRecursive(ForkJoinPool forkJoinPool, Function<Double, Double> function, double argument, double epsilon) {
        LimitTask task = new LimitTask(function, argument, epsilon, 0, 10);
        return forkJoinPool.invoke(task);
    }

    private static class LimitTask extends RecursiveTask<Double> {
        private final Function<Double, Double> function;
        private final double argument;
        private final double epsilon;
        private final int start;
        private final int end;

        public LimitTask(Function<Double, Double> function, double argument, double epsilon, int start, int end) {
            this.function = function;
            this.argument = argument;
            this.epsilon = epsilon;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if (end - start <= 2) {
                double sum = 0.0;
                for (int i = start + 1; i <= end; i++) {
                    double leftPoint = argument - epsilon * ((double) i / (end + 1));
                    double rightPoint = argument + epsilon * ((double) i / (end + 1));

                    double leftValue = function.apply(leftPoint);
                    double rightValue = function.apply(rightPoint);

                    // Если хотя бы одно из значений не является числом (NaN), вернуть 0.0
                    if (Double.isNaN(leftValue) || Double.isNaN(rightValue)) {
                        return 0.0;
                    }

                    sum += (leftValue + rightValue) / 2;
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                LimitTask leftTask = new LimitTask(function, argument, epsilon, start, mid);
                LimitTask rightTask = new LimitTask(function, argument, epsilon, mid, end);

                leftTask.fork();
                double rightResult = rightTask.compute();
                double leftResult = leftTask.join();

                return leftResult + rightResult;
            }
        }
    }
}
