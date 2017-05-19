/*

Mine frequent itemsets, association rules or association hyperedges using the Apriori algorithm.
The Apriori algorithm employs level-wise search for frequent itemsets.

*/
package com.igate.iv3.realtime;
 
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
 
public class WordNormalizer extends BaseBasicBolt {
 
	private static final long serialVersionUID = 751105854423231360L;
 
	public void cleanup() {}
 
	public void execute(Tuple input, BasicOutputCollector collector) {
		
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for(String word : words){
            word = word.trim();
            if(!word.isEmpty()){
                word = word.toLowerCase();
                collector.emit(new Values(word));
            }
        }
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		declarer.declare(new Fields("word"));
	}
}

