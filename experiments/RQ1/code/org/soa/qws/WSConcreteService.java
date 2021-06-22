package org.soa.qws;

public class WSConcreteService {
	
	public static String[] objectives={"Latency","Throughput","Availability","Compliance","Practices"};
	
	private double[] objectiveValues;
	public WSConcreteService(double latency, double throughput, double cost) {
		super();
		objectiveValues = new double[]{latency,throughput,cost};
	}
	
	public WSConcreteService(double[] d) {
		super();
		objectiveValues = d;
	}
	
	public double getLatency() {
		return objectiveValues[0];
	}
	public double getThroughput() {
		return objectiveValues[1];
	}
	public double getCost() {
		return objectiveValues[2];
	}
	
	public double[] getObjectiveValues() {
		return objectiveValues;
	}
	
	
	

}
