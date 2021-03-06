package com.allenday.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.allenday.image.backend.Processor;

import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class ImageProcessor {
	public static final int R = 0;
	public static final int G = 1;
	public static final int B = 2;
	public static final int T = 3;
	public static final int C = 4;
	public static final int M = 5;
	
	List<File> files = new ArrayList<File>();
	Processor processor;
	
	//static KDTree[] kdtree = {new KDTree<String>(8), new KDTree<String>(8), new KDTree<String>(8)}; 

	public void setFiles(File directory) {
		files.clear();
		recurse(directory);
	}
	
	public void addFiles(File directory) {
		recurse(directory);
	}
	
	public void recurse(File directory) {
		if (directory.isDirectory()) {
			String[] ents = directory.list();
			int i;
			for (i = 0; i < ents.length; i++) {
				File f = new File(ents[i]);
				if (f.isDirectory()) {
					File ff = new File(directory+"/"+f);
					//System.err.println("recurse="+ff);
					recurse(ff);
				}
				else {
					File ff = new File(directory+"/"+f);
					//System.err.println("add file1="+ff);
					files.add(ff);
				}
			}
		}
		else {
			//System.err.println("add file2="+directory);
			files.add(directory);
		}
	}
	
	public List<ImageFeatures> processImages() {
		List<ImageFeatures> results = new ArrayList<ImageFeatures>();
		for (File f : files) {
			if ( f.toString().indexOf("jpg") == -1 && f.toString().indexOf("jpeg") == -1)
				continue;

			try {
				processor = new Processor(f, false);
				double[] r = processor.getRedHistogram();
				double[] g = processor.getGreenHistogram();
				double[] b = processor.getBlueHistogram();
				double[] t = processor.getTextureHistogram();
				double[] c = processor.getCurvatureHistogram();
				int[]    m = processor.getTopology(4);
				
				double[] mm = new double[m.length];
				for (int z = 0; z < m.length; z++)
					mm[z] = m[z];
				
				ImageFeatures ff = new ImageFeatures(f.toString());
				ff.setR(r);
				ff.setG(g);
				ff.setB(b);
				ff.setT(t);
				ff.setC(c);
				ff.setM(mm);
				
				results.add(ff);
			} catch (KeySizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return results;
	}
		
	/*
		File out = new File("/Users/allenday/Sites/tmp/21K.topology.dat");
		FileWriter w = new FileWriter(out);
		BufferedWriter bw = new BufferedWriter(w);
		File d = new File("/Users/allenday/Sites/tmp/21K/");
		String[] files = d.list();
		for (int i = 0; i < files.length; i++) {
//			if (i > 1000)
//				break;
			File file = new File(d+"/"+files[i]);
			if ( file.toString().indexOf("jpg") == -1 && file.toString().indexOf("jpeg") == -1)
				continue;
			Processor processor;
			try {
				processor = new Processor(file, false);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			double[] r = processor.getRedHistogram();
//			kdtree[R].insert(r, file);
			double[] g = processor.getGreenHistogram();
//			kdtree[G].insert(g, file);
			double[] b = processor.getBlueHistogram();
//			kdtree[B].insert(b, file);
			double[] t = processor.getTextureHistogram();
			double[] c = processor.getCurvatureHistogram();
			int[] m = processor.getTopology(4);
			
			double[] mm = new double[m.length];
			for (int z = 0; z < m.length; z++)
				mm[i] = m[z];
			
			ImageFeatures ff = new ImageFeatures();
			ff.setR(r);
			ff.setG(g);
			ff.setB(b);
			ff.setT(t);
			ff.setC(c);
			ff.setM(mm);
			
			String record = file +
					"\t" + r[0] + "," + r[1] + "," + r[2] + "," + r[3] + "," + r[4] + "," + r[5] + "," + r[6] + "," + r[7] +
					"\t" + g[0] + "," + g[1] + "," + g[2] + "," + g[3] + "," + g[4] + "," + g[5] + "," + g[6] + "," + g[7] +
					"\t" + b[0] + "," + b[1] + "," + b[2] + "," + b[3] + "," + b[4] + "," + b[5] + "," + b[6] + "," + b[7] +
					"\t" + t[0] + "," + t[1] + "," + t[2] + "," + t[3] + "," + t[4] + "," + t[5] + "," + t[6] + "," + t[7] +
					"\t" + c[0] + "," + c[1] + "," + c[2] + "," + c[3] + "," + c[4] + "," + c[5] + "," + c[6] + "," + c[7] +
					"\t" + m[0] + "," + m[1] + "," + m[2] + "," + m[3] + "," + m[4] + "," + m[5] + "," + m[6] + "," + m[7] + "," + m[8] + "," + m[9] + "," + m[10] + "," + m[11] + "," + m[12] + "," + m[13] + "," + m[14] + "," + m[15] +
					"";
			
			bw.write(record+"\n");
			bw.flush();
			System.err.println(i+" "+record);
		}
	}
	*/
}
