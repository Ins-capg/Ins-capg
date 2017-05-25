 

package org.apache.spark.examples.mllib;

import com.google.common.collect.ImmutableMap;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

// $example on$
import java.util.*;

import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
// $example off$

public class JavaStratifiedSamplingExample {
  public static void main(String[] args) {

    SparkConf conf = new SparkConf().setAppName("JavaStratifiedSamplingExample");
    JavaSparkContext jsc = new JavaSparkContext(conf);

    @SuppressWarnings("unchecked")
    // $example on$
    List<Tuple2<Integer, Character>> list = Arrays.asList(
        new Tuple2<>(1, 'a'),
        new Tuple2<>(1, 'b'),
        new Tuple2<>(2, 'c'),
        new Tuple2<>(2, 'd'),
        new Tuple2<>(2, 'e'),
        new Tuple2<>(3, 'f')
    );

    JavaPairRDD<Integer, Character> data = jsc.parallelizePairs(list);

    // specify the exact fraction desired from each key Map<K, Double>
    ImmutableMap<Integer, Double> fractions = ImmutableMap.of(1, 0.1, 2, 0.6, 3, 0.3);

    // Get an approximate sample from each stratum
    JavaPairRDD<Integer, Character> approxSample = data.sampleByKey(false, fractions);
    // Get an exact sample from each stratum
    JavaPairRDD<Integer, Character> exactSample = data.sampleByKeyExact(false, fractions);
    // $example off$

    System.out.println("approxSample size is " + approxSample.collect().size());
    for (Tuple2<Integer, Character> t : approxSample.collect()) {
      System.out.println(t._1() + " " + t._2());
    }

    System.out.println("exactSample size is " + exactSample.collect().size());
    for (Tuple2<Integer, Character> t : exactSample.collect()) {
      System.out.println(t._1() + " " + t._2());
    }

    jsc.stop();
  }
}
