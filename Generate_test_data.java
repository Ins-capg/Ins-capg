/* This program generates random data for test purposes. This can be modified as per the requirements */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
public class Generate_test_data {
	  
public static void main(String... aArgs) throws Exception{
	PrintWriter pw = new PrintWriter("C:\\Users\\uadhikar\\Documents\\new.txt");
	long tNo = 1000000001l;
	for(int kkk = 0; kkk<1000000; kkk++){
	// Random Account Number  
	long START = 1000000000l;
	long  END = 9999999999l;
	Random random = new Random();
	//generate
	showRandomInteger(START, END, random,pw);
	
	//Sequential Transaction Number
	
	//generate
	//System.out.print(","+tNo+",");
	pw.print(","+tNo+",");
	tNo++;
	
	//Random State
	String states[] = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
	String cities[] = {"Montgomery","Juneau","Phoenix","Little Rock","Sacramento","Denver","Hartford","Dover","Tallahassee","Atlanta","Honolulu","Boise City","Springfield","Indianapolis","Des Moines","Topeka","Frankfort","Baton Rouge","Augusta","Annapolis","Boston","Lansing","St. Paul","Jackson","Jefferson City","Helena","Lincoln","Carson City","Concord","Trenton","Santa Fe","Albany","Raleigh","Bismarck","Columbus","Oklahoma City","Salem","Harrisburg","Providence","Columbia","Pierre","Nashville","Austin","Salt Lake City","Montpelier","Richmond","Olympia","Charleston","Madison","Cheyenne"};
	int rr_state = random.nextInt(states.length);
	//System.out.println(states.length);
	//System.out.println(cities.length);
	//state
	//System.out.print(states[rr_state]+",");
	pw.print(states[rr_state]+",");
	//city
	//System.out.print(cities[rr_state]+",");
	pw.print(cities[rr_state]+",");
	//country
	//System.out.print("US,");		
	pw.print("US,");
	String bank[] = {"FBOP","ING","HSBC","RBC","STB"};
	int bank_code[] = {32,92,120,234,999};
	int rr_bank = random.nextInt(bank.length);
	//bank name
	//System.out.print(bank[rr_bank]+",");
	pw.print(bank[rr_bank]+",");
	//bank code
	//System.out.print(bank_code[rr_bank]+",");
	pw.print(bank_code[rr_bank]+",");
	int trans_type[] = {0,1,2};
	int rr_trans_type = random.nextInt(trans_type.length);
	//transaction type
	//System.out.print(trans_type[rr_trans_type]+",");
	pw.print(trans_type[rr_trans_type]+",");
	int trans_status[] = {0,1};
	
	//transaction status
	if(rr_trans_type == 0){
		//System.out.print("0,");
		pw.print(0+",");
	}
	else {
		int rr_trans_stat = random.nextInt(trans_status.length);
		//System.out.print(trans_status[rr_trans_stat]+",");
		pw.print(trans_status[rr_trans_stat]+",");
	}
	
	long offset = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
	long end = Timestamp.valueOf("2017-01-01 00:00:00").getTime();
	long diff = end - offset + 1;
	Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
	//System.out.print(rand+",");
	pw.print(rand+",");
	
	long START1 = 100l;
	long  END1 = 9999l;
	//generate
	showRandomInteger(START1, END1, random, pw);
	pw.println();
	}
	pw.flush();
}
	  
public static void showRandomInteger(long aStart, long aEnd, Random aRandom, PrintWriter pw) throws Exception{
	if (aStart > aEnd) {
      throw new IllegalArgumentException("Start cannot exceed End.");
    }
    long range = (long)aEnd - (long)aStart + 1;
    long fraction = (long)(range * aRandom.nextDouble());
    long randomNumber =  (long)(fraction + aStart);
    //System.out.print(randomNumber);
    pw.print(randomNumber);
  }
  
  private static void log(String aMessage){
    System.out.print(aMessage);
  }
} 
