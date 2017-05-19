/*

Mine frequent itemsets, association rules or association hyperedges using the Apriori algorithm.
The Apriori algorithm employs level-wise search for frequent itemsets.

*/
ipackage com.igate.iv3.realtime;
 
import java.util.HashMap;
import java.util.Map;
 
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
 
@SuppressWarnings("serial")
public class WordCounter extends BaseBasicBolt {
 
	Integer id;
	String name;
	Map<String, Integer> counters;
 
	@Override
	public void cleanup() {
		
		System.out.println("-- Word Counter ["+name+"-"+id+"] --");
		for(Map.Entry<String, Integer> entry : counters.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
	}
 
	@SuppressWarnings("unchecked")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.counters = new HashMap<String, Integer>();
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}
 
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {}
 
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		String str = input.getString(0);
		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
	}
}



