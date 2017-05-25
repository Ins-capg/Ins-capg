 

package org.apache.spark.examples.ml;

// $example on$
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.ml.tuning.TrainValidationSplit;
import org.apache.spark.ml.tuning.TrainValidationSplitModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
// $example off$
import org.apache.spark.sql.SparkSession;

/**
 * Java example demonstrating model selection using TrainValidationSplit.
 *
 * Run with
 * {{{
 * bin/run-example ml.JavaModelSelectionViaTrainValidationSplitExample
 * }}}
 */
public class JavaModelSelectionViaTrainValidationSplitExample {
  public static void main(String[] args) {
    SparkSession spark = SparkSession
      .builder()
      .appName("JavaModelSelectionViaTrainValidationSplitExample")
      .getOrCreate();

    // $example on$
    Dataset<Row> data = spark.read().format("libsvm")
      .load("data/mllib/sample_linear_regression_data.txt");

    // Prepare training and test data.
    Dataset<Row>[] splits = data.randomSplit(new double[] {0.9, 0.1}, 12345);
    Dataset<Row> training = splits[0];
    Dataset<Row> test = splits[1];

    LinearRegression lr = new LinearRegression();

    // We use a ParamGridBuilder to construct a grid of parameters to search over.
    // TrainValidationSplit will try all combinations of values and determine best model using
    // the evaluator.
    ParamMap[] paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam(), new double[] {0.1, 0.01})
      .addGrid(lr.fitIntercept())
      .addGrid(lr.elasticNetParam(), new double[] {0.0, 0.5, 1.0})
      .build();

    // In this case the estimator is simply the linear regression.
    // A TrainValidationSplit requires an Estimator, a set of Estimator ParamMaps, and an Evaluator.
    TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
      .setEstimator(lr)
      .setEvaluator(new RegressionEvaluator())
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.8);  // 80% for training and the remaining 20% for validation

    // Run train validation split, and choose the best set of parameters.
    TrainValidationSplitModel model = trainValidationSplit.fit(training);

    // Make predictions on test data. model is the model with combination of parameters
    // that performed best.
    model.transform(test)
      .select("features", "label", "prediction")
      .show();
    // $example off$

    spark.stop();
  }
}
