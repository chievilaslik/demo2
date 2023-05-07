package com.example.demo2.limit.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class LimitCalculator {

    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    public static double calculateLimit(Function<Double, Double> function, double argument, double epsilon) {
        double result = calculateLimitRecursive(FORK_JOIN_POOL, function, argument, epsilon);
        if (result == Double.POSITIVE_INFINITY) {
            System.out.println("Inf");
        }
        return result;
    }

    private static double calculateLimitRecursive(ForkJoinPool forkJoinPool, Function<Double, Double> function, double argument, double epsilon) {
        int n = Runtime.getRuntime().availableProcessors();
        LimitTask task = new LimitTask(function, argument, epsilon, 0, 1000, n);
        return forkJoinPool.invoke(task);
    }

    private static class LimitTask extends RecursiveTask<Double> {
        private final Function<Double, Double> function;
        private final double argument;
        private final double epsilon;
        private final int start;
        private final int end;
        private final int n;

        public LimitTask(Function<Double, Double> function, double argument, double epsilon, int start, int end, int n) {
            this.function = function;
            this.argument = argument;
            this.epsilon = epsilon;
            this.start = start;
            this.end = end;
            this.n = n;
        }

        @Override
        protected Double compute() {
            if (end - start <= 2 * n) {
                double sum = 0.0;
                int count = 0;
                for (int i = start + 1; i <= end; i++) {
                    double point = argument + epsilon * ((double) i / (end + 1));

                    double value = function.apply(point);

                    if (Double.isNaN(value)) {
                        return 0.0;
                    }

                    if (Double.isInfinite(value)) {
                        return Double.POSITIVE_INFINITY;
                    }

                    sum += value;
                    count++;
                }
                return sum / count;
            } else {
                int mid = (start + end) / 2;
                LimitTask leftTask = new LimitTask(function, argument, epsilon, start, mid, n);
                LimitTask rightTask = new LimitTask(function, argument, epsilon, mid, end, n);

                leftTask.fork();
                double rightResult = rightTask.compute();
                double leftResult = leftTask.join();

                if (Double.isInfinite(leftResult) || Double.isInfinite(rightResult)) {
                    return Double.POSITIVE_INFINITY;
                }

                return (leftResult + rightResult) / 2;
            }
        }
    }
}
