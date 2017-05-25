 

package org.apache.spark.examples.ml;

// $example on$
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
// $example off$

public class JavaMulticlassLogisticRegressionWithElasticNetExample {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaMulticlassLogisticRegressionWithElasticNetExample")
                .getOrCreate();

        // $example on$
        // Load training data
        Dataset<Row> training = spark.read().format("libsvm")
                .load("data/mllib/sample_multiclass_classification_data.txt");

        LogisticRegression lr = new LogisticRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0.8);

        // Fit the model
        LogisticRegressionModel lrModel = lr.fit(training);

        // Print the coefficients and intercept for multinomial logistic regression
        System.out.println("Coefficients: \n"
                + lrModel.coefficientMatrix() + " \nIntercept: " + lrModel.interceptVector());
        // $example off$

        spark.stop();
    }
}
