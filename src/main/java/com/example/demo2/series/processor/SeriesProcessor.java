package com.example.demo2.series.processor;

import com.example.demo2.series.task.SeriesSumTask;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.concurrent.ForkJoinPool;

public class SeriesProcessor {
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    public static double processSeries(String function, double startPoint, int nTerms) {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        int threshold = Math.max(nTerms / (2 * numProcessors), 100);
        double epsilon = 1e-9;
        SeriesSumTask task = new SeriesSumTask(function, startPoint, 1, nTerms, epsilon, threshold);
        return FORK_JOIN_POOL.invoke(task);
    }

    public static double calculateTerm(String function, double startPoint, int n) {
        Expression expression = new ExpressionBuilder(function)
                .variable("x")
                .variable("n")
                .build()
                .setVariable("x", startPoint)
                .setVariable("n", n);

        return expression.evaluate();
    }
}