package main;

import java.io.Serializable;

public class Scores implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3288640410630540080L;

	public String[] score1 = new String[] { "", "0" };
	public String[] score2 = new String[] { "", "0" };
	public String[] score3 = new String[] { "", "0" };
	public String[] score4 = new String[] { "", "0" };

	public String[] score5 = new String[] { "", "0" };
	
	private String[] buffe = new String[2];

	public void arrange() {

		for (int i = 0; i < 4; i++) {
			if (Integer.parseInt(score1[1]) < Integer.parseInt(score2[1]))
				swap(1);
			if (Integer.parseInt(score2[1]) < Integer.parseInt(score3[1]))
				swap(2);
			if (Integer.parseInt(score3[1]) < Integer.parseInt(score4[1]))
				swap(3);
			if (Integer.parseInt(score4[1]) < Integer.parseInt(score5[1]))
				swap(4);
		}

	}

	private void swap(int i) {
		switch (i) {
		case 1:
			buffe = score1;
			score1 = score2;
			score2 = buffe;
			break;
		case 2:
			buffe = score2;
			score2 = score3;
			score3 = buffe;
			break;
		case 3:
			buffe = score3;
			score3 = score4;
			score4 = buffe;
			break;
		case 4:
			buffe = score4;
			score4 = score5;
			score5 = buffe;
			break;
		}
	}

}
