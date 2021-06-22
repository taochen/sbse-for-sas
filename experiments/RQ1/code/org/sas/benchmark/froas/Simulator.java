package org.sas.benchmark.froas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.femosaa.core.EAConfigure;
import org.femosaa.core.SASAlgorithmAdaptor;
import org.femosaa.seed.NewSeeder;
import org.femosaa.util.Logger;
import org.ssase.Service;
import org.ssase.objective.Objective;
import org.ssase.objective.QualityOfService;
import org.ssase.objective.optimization.femosaa.nsgaii.NSGAIIwithKAndDRegion;
import org.ssase.objective.optimization.gp.GPRegion;
import org.ssase.objective.optimization.hc.HCRegion;
import org.ssase.objective.optimization.rs.RSRegion;
import org.ssase.primitive.ControlPrimitive;
import org.ssase.primitive.Primitive;
import org.ssase.region.OptimizationType;
import org.ssase.region.Region;
import org.ssase.util.Repository;
import org.ssase.util.Ssascaling;

/**
 * 
 *
 */
public class Simulator 
{
	
	static List<Objective> o = new ArrayList<Objective>();
	static List<ControlPrimitive> cp = null;
	static List<Double> overall = new  ArrayList<Double>();
	public static String alg = "hc";
    public static void main( String[] args )
    {
    	setup();
    	main_test();
    }
    
    public static void setup() {
    	Ssascaling.activate();
		Parser.main(null);

		EAConfigure.getInstance().setupWSConfiguration();
		//System.out.print(EAConfigure.getInstance().generation + "*********\n");
		// List<WSAbstractService> as = workflow.all;
		// List<WSConcreteService> exist = new ArrayList<ConcreteService>();
		// for (AbstractService a : as) {
		// exist.addAll(a.getOption());
		// }

	    cp = new ArrayList<ControlPrimitive>();

		Set<ControlPrimitive> set = new HashSet<ControlPrimitive>();
		for (Service s : Repository.getAllServices()) {

			for (Primitive p : s.getPossiblePrimitives()) {
				if (p instanceof ControlPrimitive) {
					set.add((ControlPrimitive) p);
				}
			}

		}

		cp.addAll(set);
		Collections.sort(cp, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				ControlPrimitive cp1 = (ControlPrimitive) arg0;
				ControlPrimitive cp2 = (ControlPrimitive) arg1;
				
				int in1 = VariableOrder.getList().indexOf(cp1.getName());
				int in2 = VariableOrder.getList().indexOf(cp2.getName());
				
				//System.out.print(value1 + "-----------:------------" + value2 + "\n");
				return in1 < in2 ? -1 : 1;
			}

		});

		// Assume all objectives have the same order and inputs
		
		for (ControlPrimitive p : cp) {
			System.out.print("*****" + p.getName() + "\n");
		}
		// Region.selected = OptimizationType.FEMOSAA01 ;
		Ssascaling.loadFeatureModel(cp);

		
		
		// compact(cp, "CS1", 0);
		// compact(cp, "CS2", 1);
		// compact(cp, "CS3", 2);
		// compact(cp, "CS4", 3);
		// compact(cp, "CS5", 4);

	
	//	if(1==1)
	//		return;
		

		BenchmarkDelegate qos0 = new BenchmarkDelegate();
		//BenchmarkDelegate qos1 = new WSSOADelegate(1, workflow);
		//BenchmarkDelegate qos2 = new WSSOADelegate(2, workflow);

		Set<Objective> obj = Repository.getAllObjectives();
		
//		for (Objective ob : obj) {
//			
//			for (String s : remove_strings) {
//				if(s.equals(ob.getName())) {
//					obj.remove(ob);
//				}
//			}
//			
//		}
//		
		
		for (Objective ob : obj) {
			if ("sas-rubis_software-Response Time".equals(ob.getName())) {
				o.add(ob);
			}
		}

//		for (Objective ob : obj) {
//			if ("sas-rubis_software-Throughput".equals(ob.getName())) {
//				o.add(ob);
//			}
//		}
//
//		for (Objective ob : obj) {
//			if ("sas-rubis_software-Cost".equals(ob.getName())) {
//				o.add(ob);
//			}
//		}

		for (Objective ob : o) {

			QualityOfService qos = (QualityOfService) ob;
			if (qos.getName().equals("sas-rubis_software-Response Time")) {
				qos.setDelegate(qos0);
			} 
			
//			else if (qos.getName().equals("sas-rubis_software-Throughput")) {
//				qos.setDelegate(qos1);
//			} else {
//				qos.setDelegate(qos2);
//			}

		}
    }
    
    public static void main_test() {

	

		//Repository.initUniformWeight("W3D_105.dat", 105);
		//int max_number_of_eval_to_have_only_seed = 0;
		long time = 0; 
		int n = 100;//30 
		for (int i = 0; i < n;/*1*/ i++) {
			long t = System.currentTimeMillis(); 
			//org.femosaa.core.SASSolution.putDependencyChainBack();

//			preRunAOOrSOSeed();

			if(alg.equals("ga")) {
				testGA();
			} else if(alg.equals("hc"))  {
				testHC();
			} else if(alg.equals("rs"))  {
				testRS();
			}
			//testGA();
			//testHC();
			//testRS();
//			if(1==1) return;
//		
			
			time += System.currentTimeMillis() - t;


			
		}
		
		for (Double d : overall) {
			System.out.print("("+d + ")\n");
		}
		
	}
    
    private static void testRS() {
		double[] r = null;
		Region.selected = OptimizationType.RS;

		System.out
				.print("=============== RSRegion ===============\n");
		RSRegion moead = new RSRegion();
		moead.addObjectives(o);
		long time = System.currentTimeMillis();
		LinkedHashMap<ControlPrimitive, Double> result = moead.optimize();
		BenchmarkDelegate qos0 = new BenchmarkDelegate();
		double[] x = new double[result.size()]; 
		int i = 0;
		for (Entry<ControlPrimitive, Double> e : result.entrySet()) {
			x[i] = e.getValue();
			i++;
		}
		
		overall.add(qos0.predict(x)/100);
		// r = getFitness(moead.optimize());
		org.ssase.util.Logger.logOptimizationTime(null,
				String.valueOf((System.currentTimeMillis() - time)));
		// logData("sas", "Throughput", String.valueOf(r[0]));
		// logData("sas", "Cost", String.valueOf(r[1]));

	}
    
	private static void testHC() {
		double[] r = null;
		Region.selected = OptimizationType.HC;

		System.out
				.print("=============== HCRegion ===============\n");
		HCRegion moead = new HCRegion();
		moead.addObjectives(o);
		long time = System.currentTimeMillis();
		LinkedHashMap<ControlPrimitive, Double> result = moead.optimize();
		BenchmarkDelegate qos0 = new BenchmarkDelegate();
		double[] x = new double[result.size()]; 
		int i = 0;
		for (Entry<ControlPrimitive, Double> e : result.entrySet()) {
			x[i] = e.getValue();
			i++;
		}
		
		overall.add(qos0.predict(x)/100);
		// r = getFitness(moead.optimize());
		org.ssase.util.Logger.logOptimizationTime(null,
				String.valueOf((System.currentTimeMillis() - time)));
		// logData("sas", "Throughput", String.valueOf(r[0]));
		// logData("sas", "Cost", String.valueOf(r[1]));

	}
	
	
	private static void testGA() {
		double[] r = null;
		Region.selected = OptimizationType.GP;

		System.out
				.print("=============== GPRegion ===============\n");
		GPRegion moead = new GPRegion();
		moead.addObjectives(o);
		long time = System.currentTimeMillis();
		LinkedHashMap<ControlPrimitive, Double> result = moead.optimize();
		BenchmarkDelegate qos0 = new BenchmarkDelegate();
		double[] x = new double[result.size()]; 
		int i = 0;
		for (Entry<ControlPrimitive, Double> e : result.entrySet()) {
			x[i] = e.getValue();
			i++;
		}
		
		overall.add(qos0.predict(x)/100);
		// r = getFitness(moead.optimize());
		org.ssase.util.Logger.logOptimizationTime(null,
				String.valueOf((System.currentTimeMillis() - time)));
		// logData("sas", "Throughput", String.valueOf(r[0]));
		// logData("sas", "Cost", String.valueOf(r[1]));

	}
}


/*
 * 
 * */
