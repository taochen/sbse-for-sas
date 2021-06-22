package org.soa.femosaa;

import org.ssase.model.Delegate;

public class SOADelegate implements Delegate{

	
	private Workflow workflow;
	
	private int index;
	
	public  SOADelegate (int index, Workflow workflow) {
		this.index = index;
		this.workflow = workflow;
	}
	
	public double predict(double[] xValue) {
		double v = workflow.getObjectiveValues(index, xValue);
		
		if(index == 0 && v == 0) {
			String o = "";
			for (double d : xValue) {
				o += d + ", ";
			}
			System.out.print("*** Zero " + o + "\n");
			//throw new RuntimeException("this is error");
			
		}
		
		// 0 is throughput
		return index == 0? (v == 0? Double.MAX_VALUE : (1/v)*100) : v*100;
		/**
		 * We consider Double.MAX_VALUE/100 as normalization bounds as well, which should not cause issue as the min is E-324 but max is
		 * E308, E15 is unlikely to cause issue in the FEMOSAA experiments. Beside, some very good one will have 0, which does not matter anyway
		 * 
		 * for seeding, unlikely that Double.MAX_VALUE/100 will occu
		 * for froas, no effect at all as the fuzzy value is considered (update of Double.MAX_VALUE/100 has been prevented)
		 * 
		 * We have now revised the algorithms such that the Double.MAX_VALUE/100 is neither considered as bounds nor normal value, i.e.,
		 * it will be replaced as either 1.0 or nz_[i]
		 * 
		 */
	}
}
