package com.example.demo2.limit.processor;

import com.example.demo2.limit.task.LimitCalculator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LimitProcessor {

    public static double calculateLimit(String function, double argument, double epsilon) {
        Expression expression = new ExpressionBuilder(function)
                .variable("x")
                .build();

        return LimitCalculator.calculateLimit(x -> {
            expression.setVariable("x", x);
            return expression.evaluate();
        }, argument, epsilon);
    }
}
