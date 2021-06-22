package org.sas.benchmark.froas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ssase.requirement.froas.RequirementPrimitive;
import org.ssase.requirement.froas.RequirementProposition;
import org.ssase.util.Repository;

public class AutoRun {
/*
 * new double[] { 500, 1000, 1500, 2000, 2500 };
 * new double[] { 0.1, 0.8, 5, 15, 40};
 * new double[] { 2000, 3300, 5000, 10000, 17000};
 * new double[] { 180, 220, 230, 250, 280};
 * new double[] { 11, 13, 14.5, 15.5, 18};
 * new double[] { 230, 400, 600, 800, 1000};
 * */
	private static double[] ds = new double[] { 2000, 3300, 5000, 10000, 17000};
	private static String[] ps = new String[] { "p1", "p2", "p3", "p4", "p5",
			"p6", "p7" };
	private static String[] algs = new String[] { "ga", "hc", "rs" };
	private static String benchmark = "bdbj";

	public static void main(String[] args) {
		Simulator.setup();
		for (String p : ps) {
			for (String alg : algs) {

				for (double d : ds) {
					

					Simulator.alg = alg;
					String req = p;
					if ("p1".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												RequirementPrimitive.AS_GOOD_AS_POSSIBLE));
					} else if ("p2".equals(req)) {
						Repository.setRequirementProposition("sas-rubis_software-Response Time",
								new RequirementProposition(d,
										RequirementPrimitive.BETTER_THAN_d));
					} else if ("p3".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												d,
												RequirementPrimitive.AS_GOOD_AS_POSSIBLE_TO_d));
					} else if ("p4".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												d,
												RequirementPrimitive.AS_CLOSE_AS_POSSIBLE_TO_d));
					} else if ("p5".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												d,
												RequirementPrimitive.AS_FAR_AS_POSSIBLE_FROM_d));
					} else if ("p6".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												d,
												RequirementPrimitive.AS_GOOD_AS_POSSIBLE,
												RequirementPrimitive.BETTER_THAN_d));
					} else if ("p7".equals(req)) {
						Repository
								.setRequirementProposition(
										"sas-rubis_software-Response Time",
										new RequirementProposition(
												d,
												RequirementPrimitive.AS_GOOD_AS_POSSIBLE,
												RequirementPrimitive.AS_GOOD_AS_POSSIBLE_TO_d));
					}

					Simulator.main_test();

					File source = new File(
							"/Users/tao/research/monitor/ws-soa/sas");
					File r = new File(
							"/Users/tao/research/experiments-data/fuzzy-requirement/results/"
									+ p + "/" + benchmark + "/" + alg + "/" + d
									+ "/sas");
					File dest = new File(
							"/Users/tao/research/experiments-data/fuzzy-requirement/results/"
									+ p + "/" + benchmark + "/" + alg + "/" + d
									+ "/sas");

					if (r.exists()) {
						System.out.print("Remove " + r + "\n");
						try {
							delete(r);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(!dest.exists()) {
						dest.mkdirs();
					}
					
					try {
						copyFolder(source, dest);
						if (source.exists()) {
						System.out.print("Remove " + source + "\n");
						delete(source);
					}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					System.out.print("End of " + "/Users/tao/research/experiments-data/fuzzy-requirement/results/"
							+ p + "/" + benchmark + "/" + alg + "/" + d + "\n");
//					try {
//						Thread.sleep((long)2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					if (p.equals("p1")) {
						break;
					}

				}

			}
		}

	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}

	}

	public static void delete(File file)
    throws IOException{

    if(file.isDirectory()){

        //directory is empty, then delete it
        if(file.list().length==0){

           file.delete();
          // System.out.println("Directory is deleted : " 
                                            // + file.getAbsolutePath());

        }else{

           //list all the directory contents
           String files[] = file.list();

           for (String temp : files) {
              //construct the file structure
              File fileDelete = new File(file, temp);

              //recursive delete
             delete(fileDelete);
           }

           //check the directory again, if empty then delete it
           if(file.list().length==0){
             file.delete();
             //System.out.println("Directory is deleted : " 
                                           //   + file.getAbsolutePath());
           }
        }

    }else{
        //if file, then delete it
        file.delete();
        //System.out.println("File is deleted : " + file.getAbsolutePath());
    }
}
}
