package bapc2005;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class ProblemC
{
	
	// See problemdescription.
	private static int m, s, w;
	private static int nf, nw;
	// Geeft aan op welke verdiepingen mensen zijn, in het begin.
	private static int[] people;
	private static double m1, w1;
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader("bapc2005\\sampledata\\c.in"));
		for (int t = 0, T = new Integer(in.readLine()); t < T; t++)
			solve(in);
	}
	
	private static void solve(BufferedReader in) throws Exception
	{
		read(in);
		if (nw == 0)
			System.out.println(0);
		else if (w <= m || m*nf+s >= w*people[nw-1])
			System.out.println(people[nw-1] * w);
		else
			solveGreedy();
	}
	
	private static int solve(int l, int u, int t)
	{
		// Lift is down.
		if (l == 0)
			return u == -1 ? 0 : people[u]*w;
//			return u == -1 ? t : Math.max(t, people[u]*w);
//		// All people evacuated, lift not yet down.
//		if (u == -1)
//			return t + l*m;
		int dt = Math.abs(l - people[u]) * w;
		// auto blablabla
		return -1;
	}
	
	private static void solveGreedy()
	{
		int t = 0;
		int lf = nf;
		int pointer = nw - 1;
		boolean liftInUse = false;
		int df, lowerbound;
		double a;
		while (pointer >= 0 && m*lf + s < w*people[pointer])
		{
			liftInUse = true;
			a = lf - m1*t;
			df = lf - (int)Math.ceil(m1 * (people[pointer]-a) / (m1-w1) + a);
			t += m * df + s;
			lf -= df;
			lowerbound = 2*lf - people[pointer];
			pointer = Arrays.binarySearch(people, lowerbound);
			if (pointer < 0)
				pointer = ~pointer;
			pointer--;
		}
		t = Math.max(pointer < 0 ? 0 : w*people[pointer], liftInUse ? t+m*lf : 0);
		System.out.println(t);
	}
	
	private static void read(BufferedReader in) throws Exception
	{
		// Read input.
		String[] input = in.readLine().split(" ");
		m = new Integer(input[0]);
		s = new Integer(input[1]);
		w = new Integer(input[2]);
		input = in.readLine().split(" ");
		nf = new Integer(input[0]);
		nw = new Integer(input[1]);
		people = new int[nw];
		for (int i = 0; i < nw; i++)
			people[i] = new Integer(in.readLine());
		// Preprocess input.
		Arrays.sort(people);
		m1 = -1d/m;
		w1 = -1d/w;
	}
	
}