 

package org.apache.spark.examples.ml;

import org.apache.spark.sql.SparkSession;

// $example on$
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.Bucketizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
// $example off$

public class JavaBucketizerExample {
  public static void main(String[] args) {
    SparkSession spark = SparkSession
      .builder()
      .appName("JavaBucketizerExample")
      .getOrCreate();

    // $example on$
    double[] splits = {Double.NEGATIVE_INFINITY, -0.5, 0.0, 0.5, Double.POSITIVE_INFINITY};

    List<Row> data = Arrays.asList(
      RowFactory.create(-999.9),
      RowFactory.create(-0.5),
      RowFactory.create(-0.3),
      RowFactory.create(0.0),
      RowFactory.create(0.2),
      RowFactory.create(999.9)
    );
    StructType schema = new StructType(new StructField[]{
      new StructField("features", DataTypes.DoubleType, false, Metadata.empty())
    });
    Dataset<Row> dataFrame = spark.createDataFrame(data, schema);

    Bucketizer bucketizer = new Bucketizer()
      .setInputCol("features")
      .setOutputCol("bucketedFeatures")
      .setSplits(splits);

    // Transform original data into its bucket index.
    Dataset<Row> bucketedData = bucketizer.transform(dataFrame);

    System.out.println("Bucketizer output with " + (bucketizer.getSplits().length-1) + " buckets");
    bucketedData.show();
    // $example off$

    spark.stop();
  }
}


