package com.example.demo2.series.processor;

import com.example.demo2.series.task.SeriesSumTask;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.concurrent.ForkJoinPool;

public class SeriesProcessor {
    public static double processSeries(String function, double startPoint, int nTerms) {
        double epsilon = 1e-6;
        SeriesSumTask task = new SeriesSumTask(function, startPoint, 1, nTerms, epsilon);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(task);
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