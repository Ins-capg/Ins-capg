 

package org.apache.spark.examples.mllib;

// $example on$
import scala.Tuple2;

import org.apache.spark.api.java.*;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.apache.spark.mllib.evaluation.RegressionMetrics;
import org.apache.spark.SparkConf;
// $example off$

public class JavaRegressionMetricsExample {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("Java Regression Metrics Example");
    JavaSparkContext sc = new JavaSparkContext(conf);
    // $example on$
    // Load and parse the data
    String path = "data/mllib/sample_linear_regression_data.txt";
    JavaRDD<String> data = sc.textFile(path);
    JavaRDD<LabeledPoint> parsedData = data.map(line -> {
      String[] parts = line.split(" ");
      double[] v = new double[parts.length - 1];
      for (int i = 1; i < parts.length - 1; i++) {
        v[i - 1] = Double.parseDouble(parts[i].split(":")[1]);
      }
      return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
    });
    parsedData.cache();

    // Building the model
    int numIterations = 100;
    LinearRegressionModel model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData),
      numIterations);

    // Evaluate model on training examples and compute training error
    JavaPairRDD<Object, Object> valuesAndPreds = parsedData.mapToPair(point ->
      new Tuple2<>(model.predict(point.features()), point.label()));

    // Instantiate metrics object
    RegressionMetrics metrics = new RegressionMetrics(valuesAndPreds.rdd());

    // Squared error
    System.out.format("MSE = %f\n", metrics.meanSquaredError());
    System.out.format("RMSE = %f\n", metrics.rootMeanSquaredError());

    // R-squared
    System.out.format("R Squared = %f\n", metrics.r2());

    // Mean absolute error
    System.out.format("MAE = %f\n", metrics.meanAbsoluteError());

    // Explained variance
    System.out.format("Explained Variance = %f\n", metrics.explainedVariance());

    // Save and load model
    model.save(sc.sc(), "target/tmp/LogisticRegressionModel");
    LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(),
      "target/tmp/LogisticRegressionModel");
    // $example off$

    sc.stop();
  }
}
