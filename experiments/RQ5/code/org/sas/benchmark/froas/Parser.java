package org.sas.benchmark.froas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * 
 * 
 * d values
 * == APACHEAll == 100=10,10
 * 500, 1000, 1500, 2000, 2800
 * == BDBCAll == 1500=50,30
 * 0.1, 0.8, 5, 15, 40
 * == BDBJAll == 200=20,10, 0.8, 0.3
 * 2000, 3300, 5000, 10000, 17000
 * == LLVM == 500=10,50
 * 180, 220, 230, 250, 280
 * == SQL == 100000=100,1000, 0.8, 0.3
 * 11, 13, 14.5, 15.5, 18
 * == X264 == 500=10,50
 * 230, 400, 600, 800, 1000
 * 
 * @author tao
 *
 */
public class Parser {
 
	
	private static int[] APACHE_MAN = new int[]{0};
	private static String[] APACHE_XOR = new String[]{};
	
	private static int[] BDBC_MAN_XOR = new int[]{7,8}; // the new index
	private static int[] BDBC_MAN = new int[]{};//7,13
	private static String[] BDBC_XOR = new String[]{"7:5","13:4"};//20 AND 21 REQUIRE
	
	private static int[] BDBJ_MAN_XOR = new int[]{0,4}; 
	private static int[] BDBJ_MAN = new int[]{0,1,13,14,16,17,18};//0,1,2,10,13,14,16,17,18
	private static String[] BDBJ_XOR = new String[]{"2:2","6:2","10:2","22:2"} ;// 4 AND 5 REQUIRE
	
	private static int[] LLVM_MAN = new int[]{0};
	private static String[] LLVM_XOR = new String[]{};
	
	
	private static int[] SQL_MAN = new int[]{};
	private static String[] SQL_XOR = new String[]{"2:4","24:2","27:3","31:2","34:4"};//23 AND 24 REQUIRE
	
	private static int[] X264_MAN_XOR = new int[]{7,8}; 
	private static int[] X264_MAN = new int[]{0};//0,8,12
	private static String[] X264_XOR = new String[]{"8:3","12:3"};
	
	
	//public static String[] keepZero = {"BDBCAll","BDBJAll","X264All"};
	public static HashMap<String, Double> map = new HashMap<String, Double>();
	public static String selected = "SQLAll";
	//x264 Best 244.23Worst 821.963
	// sql Best 12.513Worst 16.851
    public static void main( String[] args )
    {
    	//X264All.csv
    	//validate();
        read(selected, SQL_MAN,SQL_XOR, null);
    	//read(selected,SQL_MAN,SQL_XOR);
    	
    	//normalize();
    	//run_normalize();
    }
    
    public static void read(String name, int[] MAN, String[] XOR, int[] MAN_XOR){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/Users/tao/research/experiments-data/fuzzy-requirement/single-objective-dataset/"+name+".csv"));
			String line = null; 
			
			
			while ((line = reader.readLine()) != null) {
				
				if(line.startsWith("$")) {
					continue;
				}
				String r = "";
				String[] data = line.split(",");
				
				for(int i = 0; i < data.length - 1; i++) {
					String n = "";
					boolean included = true;
					for(int j = 0; j < MAN.length; j++) {
						if(MAN[j] == i) {
							included = false;
							break;
						}
					}
					//System.out.print(included+"***s\n");
					for(int j = 0; j < XOR.length; j++) {
						
						int start = Integer.parseInt(XOR[j].split(":")[0]);
					    int l = Integer.parseInt(XOR[j].split(":")[1]);
					   // int count = 0;
						for(int k = 0; k <= l/*need this*/; k++) {
							
							if(i == start && data[i].equals("1")) {
								
								for(int m = 0; m < l/*need this*/; m++) {
									if(data[m+1+start].equals("1")) {
										int f = m+1;
										n = String.valueOf(f);
										break;
									}
								}
								if("".equals(n)) {
									n = "0";
								}
								
								break;
							} else if((start+k) == i && k != 0) {
								included = false;
								break;
							}
						}
						
						if(!included || !n.equals("")) {
							
							break;
						}
					}
					//System.out.print(n+"***\n");
					if(included) {
				
						
						r += n.equals("")? data[i] : n; 
						
					}
					
				}
				
				// start fixing the mandatory alt to start from 0;
				if(MAN_XOR != null) {
					for (int i = 0; i < MAN_XOR.length;i++) {
						int n = Character.getNumericValue(r.charAt(MAN_XOR[i]));
						//System.out.print("before " + n+"\n");
						n = n -1;
						//System.out.print("after " + (char)n+"\n");
						StringBuilder str = new StringBuilder(r);
						str.setCharAt(MAN_XOR[i], Character.forDigit(n,10));
						r = str.toString();
					}
					
				}
				
				if(map.containsKey(r)) {
					System.out.print(line + " : " + r+ ", current "  +map.get(r) +" duplicate\n");
				}
				
				map.put(r, Double.valueOf(data[data.length-1]));
		
				System.out.print(r + "=" + data[data.length-1]+"\n");
			}
			
			System.out.print(map.size());
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		for (Double d : map.values()) {
			if(d < min) {
				min = d;
			}
			if(d > max) {
				max = d;
			}
			
		}
		
		System.out.print("Best " + min + "Worst " + max);
		
		for (Double d : map.values()) {
			double z = (d-min)/(max-min);
			//System.out.print("(1,"+z+")\n");
		}
		
	}
	
	public static void validate(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/Users/tao/research/experiments-data/fuzzy-requirement/single-objective-dataset/"+selected+".csv"));
			String line = null; 
			
			int[] store = null;
			int total = 0;
			while ((line = reader.readLine()) != null) {
				
				if(line.startsWith("$")) {
					String[] d = line.split(",");
					for (int i = 0; i < d.length; i++) {
						//System.out.print("\""+d[i].substring(1) + "\",\n");
					}
					
					continue;
				}
				
				String[] data = line.split(",");
				
				if(store == null) {
					store = new int[data.length - 1];
					for(int i = 0; i < store.length; i++) {
						store[i] = 0;
					}
				}
				
				for(int i = 0; i < store.length; i++) {
					
					if(data[i].equals("1")) {
						store[i] += 1;
					} 
				}
				
				total++;
		
			}
			
			String r = "";
			for(int i = 0; i < store.length; i++) {
				
				if(store[i] == total) { 
					r += i + ",";
				}
			}
			
			System.out.print(r);
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void normalize(){
		double max =  17.894581279143072;
		double v = 4.1823277703510335;
		double min = 0;
		
		v = (v - min) / (max - min);
		
		System.out.print((0.3 * v) + 1.2);
		
		/**
		 *17.894581279143072
10.953841910378587
4.819035135705402
4.1823277703510335
1.0097075186941624
		 */
	}
	
	/**
	 * apache=0.08888888888888889;0.36666666666666664;0.6444444444444445;
	 * bdbc=0.011525532255482631;0.11996467982050739;0.37815312640389964;
	 * bdbj=0.025053422739665463;0.15032053643799279;0.5187532237860143;
	 * llvm=0.290950744558992;0.43413516609392905;0.7205040091638032;
	 * x264=0.26962281884538364;0.6158034940015544;0.9619841691577251;
	 * sql=0.11226371599815588;0.45804518211157225;0.6885661595205165;
	 */
	private static void run_normalize(){
		String[] a = new String[]{"13.0", "14.5", "15.5"};
		double max = 16.851;
		
		double min = 12.513;
		
		for (String s : a) {
			
			double v = Double.parseDouble(s);
			v = (v - min) / (max - min);
			
			System.out.print(v+";");
		}
		
	}
}
