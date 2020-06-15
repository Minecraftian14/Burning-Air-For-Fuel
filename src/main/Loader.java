package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Arrays;

public class Loader {

	static File f;
	
	static FileOutputStream fos;
	static ObjectOutputStream oos;
	
	static FileInputStream fis;
	static ObjectInputStream ois;
	
	public static void load() throws IOException {
		
		f = new File(new Loader().getUrl());
		
	}
	
	public static Scores getScores() throws ClassNotFoundException, IOException {

		fis = new FileInputStream(f);
		ois = new ObjectInputStream(fis);

		Scores out = new Scores();
		out = (Scores)ois.readObject();
		
		ois.close();
		fis.close();
		
		return out;
		
	}
	
	public static void setScores(Scores in) throws IOException {

		fos = new FileOutputStream(f);
		oos = new ObjectOutputStream(fos);
		
		oos.writeObject(in);
		
		oos.close();
		fos.close();
		
	}
	
	public String getUrl() {
		
		URL out = this.getClass().getClassLoader().getResource("obj.ser");

		return out.getPath();
		
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Loader.load();
		Scores s = Loader.getScores();

//		s.score1 = new String[]{"Someone A", "500"};
//		s.score2 = new String[]{"Someone B", "400"};
//		s.score3 = new String[]{"Someone C", "300"};
//		s.score4 = new String[]{"Someone D", "200"};
//		s.score5 = new String[]{"Someone E", "100"};
//
//		Loader.setScores(s);

		System.out.println(Arrays.toString(s.score1));
		System.out.println(Arrays.toString(s.score2));
		System.out.println(Arrays.toString(s.score3));
		System.out.println(Arrays.toString(s.score4));
		System.out.println(Arrays.toString(s.score5));
	}
	
}
