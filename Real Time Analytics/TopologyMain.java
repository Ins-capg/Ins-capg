/*

Mine frequent itemsets, association rules or association hyperedges using the Apriori algorithm.
The Apriori algorithm employs level-wise search for frequent itemsets.

*/
package com.igate.iv3.realtime;
 
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
 
public class TopologyMain {
	
	public static void main(String[] args) throws InterruptedException {
         
        // Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader",new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter(),1).fieldsGrouping("word-normalizer", new Fields("word"));
		
        // Configuration
		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		conf.setDebug(false);
		
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("term-frequency-counter", conf, builder.createTopology());
		Thread.sleep(1000);
		cluster.shutdown();
	}
}
