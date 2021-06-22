package org.sas.benchmark.froas;

import org.ssase.model.Delegate;

public class BenchmarkDelegate implements Delegate{

	@Override
	public double predict(double[] xValue) {
		String v = "";
		for (double d : xValue) {
			v += (int)d;
		}
		//System.out.print(v + "***\n");
		return Parser.map.containsKey(v)? Parser.map.get(v)*100 : Double.MAX_VALUE;
	}

}
