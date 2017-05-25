 

package org.apache.spark.examples.mllib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
// $example on$
import java.util.Arrays;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.mllib.stat.test.KolmogorovSmirnovTestResult;
// $example off$

public class JavaHypothesisTestingKolmogorovSmirnovTestExample {
  public static void main(String[] args) {

    SparkConf conf =
      new SparkConf().setAppName("JavaHypothesisTestingKolmogorovSmirnovTestExample");
    JavaSparkContext jsc = new JavaSparkContext(conf);

    // $example on$
    JavaDoubleRDD data = jsc.parallelizeDoubles(Arrays.asList(0.1, 0.15, 0.2, 0.3, 0.25));
    KolmogorovSmirnovTestResult testResult =
      Statistics.kolmogorovSmirnovTest(data, "norm", 0.0, 1.0);
    // summary of the test including the p-value, test statistic, and null hypothesis
    // if our p-value indicates significance, we can reject the null hypothesis
    System.out.println(testResult);
    // $example off$

    jsc.stop();
  }
}

