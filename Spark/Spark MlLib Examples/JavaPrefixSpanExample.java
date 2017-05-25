 

package org.apache.spark.examples.mllib;

// $example on$
import java.util.Arrays;
import java.util.List;
// $example off$
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
// $example on$
import org.apache.spark.mllib.fpm.PrefixSpan;
import org.apache.spark.mllib.fpm.PrefixSpanModel;
// $example off$
import org.apache.spark.SparkConf;

public class JavaPrefixSpanExample {

  public static void main(String[] args) {

    SparkConf sparkConf = new SparkConf().setAppName("JavaPrefixSpanExample");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);

    // $example on$
    JavaRDD<List<List<Integer>>> sequences = sc.parallelize(Arrays.asList(
      Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3)),
      Arrays.asList(Arrays.asList(1), Arrays.asList(3, 2), Arrays.asList(1, 2)),
      Arrays.asList(Arrays.asList(1, 2), Arrays.asList(5)),
      Arrays.asList(Arrays.asList(6))
    ), 2);
    PrefixSpan prefixSpan = new PrefixSpan()
      .setMinSupport(0.5)
      .setMaxPatternLength(5);
    PrefixSpanModel<Integer> model = prefixSpan.run(sequences);
    for (PrefixSpan.FreqSequence<Integer> freqSeq: model.freqSequences().toJavaRDD().collect()) {
      System.out.println(freqSeq.javaSequence() + ", " + freqSeq.freq());
    }
    // $example off$

    sc.stop();
  }
}
