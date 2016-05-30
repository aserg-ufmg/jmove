package br.ufmg.dcc.labsoft.java.jmove.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class PrintOutput {

	private static Map<String, PrintStream> outputsMap = new HashMap<String, PrintStream>();

	public static void write(String text, String address) {
		PrintStream outuput = outputsMap.get(address);

		try {
			if (outuput == null) {
				String home = System.getProperty("user.home");
				outputsMap.put(address, new PrintStream(new File(home + "/"
						+ address)));
			}
			outuput = outputsMap.get(address);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		if (outuput != null) {
			outuput.print(text);
		}
	}

	public static void finish(String adress) {
		PrintStream outuput = outputsMap.get(adress);

		if (outuput != null) {
			outuput.close();
		}

		outputsMap.remove(adress);
	}
}
