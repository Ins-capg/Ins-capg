 

package org.apache.spark.examples.mllib;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

// $example on$
import scala.Tuple2;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
// $example off$

/**
 * Example for SVMWithSGD.
 */
public class JavaSVMWithSGDExample {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("JavaSVMWithSGDExample");
    SparkContext sc = new SparkContext(conf);
    // $example on$
    String path = "data/mllib/sample_libsvm_data.txt";
    JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();

    // Split initial RDD into two... [60% training data, 40% testing data].
    JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11L);
    training.cache();
    JavaRDD<LabeledPoint> test = data.subtract(training);

    // Run training algorithm to build the model.
    int numIterations = 100;
    SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

    // Clear the default threshold.
    model.clearThreshold();

    // Compute raw scores on the test set.
    JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(p ->
      new Tuple2<>(model.predict(p.features()), p.label()));

    // Get evaluation metrics.
    BinaryClassificationMetrics metrics =
      new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
    double auROC = metrics.areaUnderROC();

    System.out.println("Area under ROC = " + auROC);

    // Save and load model
    model.save(sc, "target/tmp/javaSVMWithSGDModel");
    SVMModel sameModel = SVMModel.load(sc, "target/tmp/javaSVMWithSGDModel");
    // $example off$

    sc.stop();
  }
}
