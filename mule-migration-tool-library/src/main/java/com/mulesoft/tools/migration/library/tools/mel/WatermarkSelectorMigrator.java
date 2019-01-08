/*
 * Copyright (c) 2017 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master Subscription
 * Agreement (or other master license agreement) separately entered into in writing between
 * you and MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.tools.migration.library.tools.mel;

import com.mulesoft.tools.migration.step.category.MigrationReport;
import com.mulesoft.tools.migration.util.ExpressionMigrator;
import org.jdom2.Element;

/**
 * Resolver for watermark selector expressions
 *
 * @author Mulesoft Inc.
 * @since 1.0.0
 */
public class WatermarkSelectorMigrator {

  public String migrateSelector(String expression, String selector, Element element, MigrationReport report,
                                ExpressionMigrator expressionMigrator) {
    expression = expression.trim();
    String migratedExpression = expressionMigrator.unwrap(expressionMigrator.migrateExpression(expression, true, element));

    if (migratedExpression.startsWith("mel:")) {
      report.report("watermark.expression", element, element);
    } else {
      switch (selector) {
        case "min":
          migratedExpression = splitSelectorExpression(migratedExpression, "min");
          break;
        case "max":
          migratedExpression = splitSelectorExpression(migratedExpression, "max");
          break;
        case "first":
          migratedExpression = "#[" + migratedExpression + "[0]]";
          break;
        case "last":
          migratedExpression = "#[" + migratedExpression + "[-1]]";
          break;
        default: {
          report.report("watermark.expression", element, element);
          break;
        }
      }
    }
    return migratedExpression;
  }

  private String splitSelectorExpression(String expression, String selector) {
    String leftExpression;
    String rightExpression;

    Integer lastIndDot = expression.lastIndexOf(".");
    if (lastIndDot != -1 && !expression.substring(lastIndDot + 1).endsWith("]")) {
      leftExpression = expression.substring(0, lastIndDot);
      rightExpression = expression.substring(lastIndDot + 1);
    } else {
      leftExpression = expression;
      rightExpression = "";
    }

    return "#[" + selector + "(" + leftExpression + " map $ " + rightExpression + ")]";
  }
}
