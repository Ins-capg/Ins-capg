/*

Mine frequent itemsets, association rules or association hyperedges using the Apriori algorithm.
The Apriori algorithm employs level-wise search for frequent itemsets.

*/

package com.igate.iv3.realtime;
 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
 
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
 
@SuppressWarnings("serial")
public class WordReader extends BaseRichSpout {
 
	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	
	public void ack(Object msgId) {
		
		System.out.println("OK:"+msgId);
	}
	
	public void close() {}
	
	public void fail(Object msgId) {
		
		System.out.println("FAIL:"+msgId);
	}
 
	public void nextTuple() {
 
		if(completed){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return;
		}
		String str;
		BufferedReader reader = new BufferedReader(fileReader);
		try{
			while((str = reader.readLine()) != null){
				this.collector.emit(new Values(str),str);
			}
		} catch(Exception e) {
			throw new RuntimeException("Error reading tuple",e);
		} finally {
			completed = true;
		}
	}
 
	@SuppressWarnings("unchecked")
	public void open(Map conf, TopologyContext context,	SpoutOutputCollector collector) {
		
		try {
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error reading file ["+conf.get("wordFile")+"]");
		}
		this.collector = collector;
	}
 
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		declarer.declare(new Fields("line"));
	}
}

