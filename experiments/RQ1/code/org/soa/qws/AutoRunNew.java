package org.soa.qws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.femosaa.core.SASAlgorithmAdaptor;
import org.ssase.requirement.froas.RequirementPrimitive;
import org.ssase.requirement.froas.RequirementProposition;
import org.ssase.util.Repository;

public class AutoRunNew {
	/*
	 * new double[] { 500, 1000, 1500, 2000, 2500 }; new double[] { 0.1, 0.8, 5,
	 * 15, 40}; new double[] { 2000, 3300, 5000, 10000, 17000}; new double[] {
	 * 180, 220, 230, 250, 280}; new double[] { 11, 13, 14.5, 15.5, 18}; new
	 * double[] { 230, 400, 600, 800, 1000};
	 */
	// private static double[] ds = new double[] { 2000, 3300, 5000, 10000,
	// 17000};
	private static String[] weights = new String[] {"0.0-1.0","1.0-0.0"/*"0.1-0.9", "0.2-0.8",
			"0.3-0.7", "0.4-0.6", "0.5-0.5","0.6-0.4", "0.7-0.3", "0.8-0.2",
			"0.9-0.1"*/};
	private static String[] single_algs = new String[] { "ga" };
	private static String[] multi_algs = new String[] { "nsgaii" };
	private static String benchmark = "15AS3";

	public static void main(String[] args) {
		WSSimulator.setup();
		SASAlgorithmAdaptor.isFuzzy = true;

		File f = new File("/Users/"+System.getProperty("user.name")+"/research/monitor/ws-soa/sas");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		run_MOEA();

		//if(1==1)return;
		SASAlgorithmAdaptor.isFuzzy = false;
		for (String p : weights) {

			String[] s = p.split("-");
			double[] w = new double[s.length];
			for (int i = 0; i < s.length; i++) {
				w[i] = Double.parseDouble(s[i]);
			}

			for (String alg : single_algs) {

				WSSimulator.alg = alg;
				WSSimulator.weights = w;

				WSSimulator.main_test();

				File source = new File("/Users/"+System.getProperty("user.name")+"/research/monitor/ws-soa/sas");
				File r = new File(
						"/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
								+ p + "/" + benchmark + "/" + alg + "/"
								+ "/sas");
				File dest = new File(
						"/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
								+ p + "/" + benchmark + "/" + alg + "/"
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

				if (!dest.exists()) {
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

				System.out
						.print("End of "
								+ "/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
								+ p + "/" + benchmark + "/" + alg + "/" + "\n");
				// try {
				// Thread.sleep((long)2000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}
		}

	}

	public static void run_MOEA() {
		for (String alg : multi_algs) {
			WSSimulator.alg = alg;

			WSSimulator.main_test();

			File source = new File("/Users/"+System.getProperty("user.name")+"/research/monitor/ws-soa/sas");
			File r = new File(
					"/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
							+ "/" + benchmark + "/" + alg + "/" + "/sas");
			File dest = new File(
					"/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
							+ "/" + benchmark + "/" + alg + "/" + "/sas");

			if (r.exists()) {
				System.out.print("Remove " + r + "\n");
				try {
					delete(r);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!dest.exists()) {
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

			System.out
					.print("End of "
							+ "/Users/"+System.getProperty("user.name")+"/research/experiments-data/s-vs-m/services/"
							+ "/" + benchmark + "/" + alg + "/" + "\n");

		}
		File f = new File("/Users/"+System.getProperty("user.name")+"/research/monitor/ws-soa/sas");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public static void delete(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				// System.out.println("Directory is deleted : "
				// + file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					// System.out.println("Directory is deleted : "
					// + file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			// System.out.println("File is deleted : " +
			// file.getAbsolutePath());
		}
	}
}
