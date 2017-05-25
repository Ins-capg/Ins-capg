 

package org.apache.spark.examples.mllib;

// $example on$
import java.util.Arrays;
import java.util.List;

import scala.Tuple2;

import org.apache.spark.api.java.*;
import org.apache.spark.mllib.evaluation.MultilabelMetrics;
import org.apache.spark.SparkConf;
// $example off$

public class JavaMultiLabelClassificationMetricsExample {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("Multilabel Classification Metrics Example");
    JavaSparkContext sc = new JavaSparkContext(conf);
    // $example on$
    List<Tuple2<double[], double[]>> data = Arrays.asList(
      new Tuple2<>(new double[]{0.0, 1.0}, new double[]{0.0, 2.0}),
      new Tuple2<>(new double[]{0.0, 2.0}, new double[]{0.0, 1.0}),
      new Tuple2<>(new double[]{}, new double[]{0.0}),
      new Tuple2<>(new double[]{2.0}, new double[]{2.0}),
      new Tuple2<>(new double[]{2.0, 0.0}, new double[]{2.0, 0.0}),
      new Tuple2<>(new double[]{0.0, 1.0, 2.0}, new double[]{0.0, 1.0}),
      new Tuple2<>(new double[]{1.0}, new double[]{1.0, 2.0})
    );
    JavaRDD<Tuple2<double[], double[]>> scoreAndLabels = sc.parallelize(data);

    // Instantiate metrics object
    MultilabelMetrics metrics = new MultilabelMetrics(scoreAndLabels.rdd());

    // Summary stats
    System.out.format("Recall = %f\n", metrics.recall());
    System.out.format("Precision = %f\n", metrics.precision());
    System.out.format("F1 measure = %f\n", metrics.f1Measure());
    System.out.format("Accuracy = %f\n", metrics.accuracy());

    // Stats by labels
    for (int i = 0; i < metrics.labels().length - 1; i++) {
      System.out.format("Class %1.1f precision = %f\n", metrics.labels()[i], metrics.precision(
        metrics.labels()[i]));
      System.out.format("Class %1.1f recall = %f\n", metrics.labels()[i], metrics.recall(
        metrics.labels()[i]));
      System.out.format("Class %1.1f F1 score = %f\n", metrics.labels()[i], metrics.f1Measure(
        metrics.labels()[i]));
    }

    // Micro stats
    System.out.format("Micro recall = %f\n", metrics.microRecall());
    System.out.format("Micro precision = %f\n", metrics.microPrecision());
    System.out.format("Micro F1 measure = %f\n", metrics.microF1Measure());

    // Hamming loss
    System.out.format("Hamming loss = %f\n", metrics.hammingLoss());

    // Subset accuracy
    System.out.format("Subset accuracy = %f\n", metrics.subsetAccuracy());
    // $example off$

    sc.stop();
  }
}
