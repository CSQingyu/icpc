/*
 * Created on 8-okt-2005
 */
package otherproblems;

import java.util.Arrays;

public class Dartbord2
{

	int N; // het aantal vakjes
	int[] vakje; // vakje[a] = het getal dat in vakje a staat (ascending)
	int[][][] solution;
	// solution[n][p] = alle puntenaantallen die je met p pijltjes en de vakjes
	//                  0 t/m n kunt gooien. (en dus NIET met p-x pijltjes,
	//                  0<x<p)
	
	int[] bestSolution; // de beste invulling van de vakjes
	int maxSolution; // het hoogste wat nog ononderbroken kan worden gegooid
	
	long timeFillSolutionInt = 0,
	     timeFillSolutionIntInt = 0,
	     timeInit = 0,
	     timeSearch = 0,
	     timeInsert = 0,
	     timeArrayCopy = 0;
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		long time = System.currentTimeMillis();
		if (args.length != 1)
			return;
		try
		{
			Dartbord2 db = new Dartbord2(Integer.parseInt(args[0]));
			db.start();
		}
		catch (NumberFormatException nfe)
		{
			nfe.printStackTrace();
			return;
		}
		System.out.println((System.currentTimeMillis()-time) + " miliseconds");
	}

	public Dartbord2(int aantalVakjes)
	{
		N = aantalVakjes;
		vakje = new int[N+1];
		bestSolution = new int[N+1];
		solution = new int[N+1][4][];
		
		// Dit is altijd zo:
		vakje[0] = 0;
		bestSolution[0] = 0;
		if (N != 0)
		{
			vakje[1] = 1;
			bestSolution[1] = 1;
			maxSolution = 3;
		}
		for (int p = 0; p < 4; p++)
		{
			solution[0][p] = new int[1];
			solution[0][p][0] = 0;
			if (N != 0)
			{
				solution[1][p] = new int[1];
				solution[1][p][0] = p;
			}
		}
		for (int n = 2; n <= N; n++) // [0][0] en [1][0] zijn hiervoor al ge�nit
		{
			solution[n][0] = new int[1];
			solution[n][0][0] = 0;
		}
	}
	
	/**
	 * Als start wordt aangeroepen ziet
	 * - de array vakje er zo uit:
	 * |{0, 1, 0, 0, ... , 0}| = N+1
	 * 
	 * - de matrix solution er zo uit:
	 * |{{{0}, {0},  {0},  {0}},
	 *   {{0}, {1},  {2},  {3}},
	 *   {{0}, null, null, null},
	 *   {{0}, null, null, null},
	 *   .
	 *   .
	 *   .
	 *   {{0}, null, null, null}}| = N+1
	 * 
	 */
	public void start()
	{
		if (N == 0)
		{
			System.out.println("Geen vakjes, geen cijfers!");
			return;
		}
		else if (N == 1)
		{
			System.out.println(this);
			return;
		}
		// eigenlijk hoort dit in de solve methode, welke dan weer iets anders
		// hoort te zijn, maar de top van de recursie niet in je recursieve
		// methode zetten is eigenlijk lelijk :P
		for (int i = 2; i <= 4; i++)
		{
			vakje[2] = i;
			solve(2);
		}
		System.out.println(this);
	}
	
	public void solve(int n)
	{
		// vul de matrix solutions[n] met oplossingen
		int max = fillSolution(n);
		// is n alweer de laatste rij? kijk dan of we een beste oplossing hebben
		if (n == N)
		{
			if (max > maxSolution)
			{
				long start = System.currentTimeMillis();
				System.arraycopy(vakje, 0, bestSolution, 0, N+1);
				timeArrayCopy += System.currentTimeMillis() - start;
				maxSolution = max;
			}
		}
		// anders moeten we alle volgende rijen via recursie berekenen
		else
		{
			// v <= max+1 wil zeggen dat je nooit verder hoeft te gaan dan
			// max+2, want dan kun je het getal max+1 nooit gooien
			for (int v = vakje[n]+1; v <= max+1; v++)
			{
				vakje[n+1] = v;
				solve(n+1);
			}
		}
	}
	
	// vul solution[n] met mogelijke getallen die je met n of minder vakjes kunt
	// gooien
	// geeft het maximum terug wat je kunt gooien met n vakjes
	public int fillSolution(int n)
	{
		assert n >= 2;
		for (int p = 1; p < 4; p++)
			fillSolution(n, p);
		long start = System.currentTimeMillis();
		int p1=0, p2=0, p3=0, max=1;
		boolean found;
		// aanname: met meer pijltjes kun je altijd even veel of meer getallen
		//          maken. controleer dit met asserts
		// edit: dit blijkt wel goed te zitten. ik laat de asserts lekker staan,
		//       het kost toch geen drol kwa rekentijd
		assert solution[n][0].length <= solution[n][1].length;
		assert solution[n][1].length <= solution[n][2].length;
		assert solution[n][2].length <= solution[n][3].length;
		// kijk hoever we kunnen komen met deze solution (de max dus)
		// TODO hier kan nog snelheidswinst worden gehaald, zij het minimaal
		while (p1 < solution[n][1].length &&
		       p2 < solution[n][2].length &&
		       p3 < solution[n][3].length)
		{
			found = false;
			if (solution[n][1][p1] == max)
			{
				found = true;
				p1++;
			}
			if (solution[n][2][p2] == max)
			{
				found = true;
				p2++;
			}
			if (solution[n][3][p3] == max)
			{
				found = true;
				p3++;
			}
			if (!found)
			{
				timeFillSolutionInt += System.currentTimeMillis() - start;
				return max-1;
			}
			max++;
		}
		while (p2 < solution[n][2].length &&
		       p3 < solution[n][3].length)
		{
			found = false;
			if (solution[n][2][p2] == max)
			{
				found = true;
				p2++;
			}
			if (solution[n][3][p3] == max)
			{
				found = true;
				p3++;
			}
			if (!found)
			{
				timeFillSolutionInt += System.currentTimeMillis() - start;
				return max-1;
			}
			max++;
		}
		while (p3 < solution[n][3].length)
		{
			if (solution[n][3][p3] == max)
				p3++;
			else
			{
				timeFillSolutionInt += System.currentTimeMillis() - start;
				return max-1;
			}
			max++;
		}
		timeFillSolutionInt += System.currentTimeMillis() - start;
		return max-1;
	}
	
	// vul solution[n][p] met mogelijke getallen die je met p pijltjes, en niet
	// met minder, en n of minder vakjes kunt gooien
	public void fillSolution(int n, int p)
	{
		long start = System.currentTimeMillis();
		int size = solution[n-1][p].length;
		int sizePmin1 = solution[n][p-1].length;
		int sizeTotal = sizePmin1 + size;
		// temp result, we weten namelijk alleen een upperbound voor de size
		// (vanwege dubbele waardes)
		int[] solutions = new int[sizeTotal];
		// arraycopy gooit ze aan het eind van de array, om te zorgen dat de
		// array sorted blijft (is nodig voor Arrays.binarySearch())
		long start2 = System.currentTimeMillis();
		System.arraycopy(solution[n-1][p], 0, solutions, sizePmin1, size);
		timeArrayCopy += System.currentTimeMillis() - start2;
		timeInit += System.currentTimeMillis() - start;
		// Ga alle mogelijke nieuwe getallen langs
		for (int pointer = 0; pointer < sizePmin1; pointer++)
		{
			int getal = solution[n][p-1][pointer] + vakje[n];
			// op welke positie komt ons nieuwe getal te staan?
			start2 = System.currentTimeMillis();
			int pos = Arrays.binarySearch(solutions, getal);
			timeSearch += System.currentTimeMillis() - start2;
			// alleen toevoegen als we het getal nog niet kennen
			if (pos < 0)
			{
				// Getal is nieuw, dus toevoegen
				start2 = System.currentTimeMillis();
				insert(getal, solutions, sizeTotal - size, -(pos+2));
				timeInsert += System.currentTimeMillis() - start2;
				size++;
			}
		}
		// nu weten we exact wat er in de solution komt, dus copyen we het even
		solution[n][p] = new int[size];
		start2 = System.currentTimeMillis();
		System.arraycopy(solutions, sizeTotal - size, solution[n][p], 0, size);
		timeArrayCopy += System.currentTimeMillis() - start2;
		timeFillSolutionIntInt += System.currentTimeMillis() - start;
	}
	
	public void insert(int val, int[] a, int startIndex, int position)
	{
		for (; startIndex <= position; startIndex++)
			a[startIndex-1] = a[startIndex];
		a[position] = val;
	}
	
	@Override
	public String toString()
	{
		assert N > 0;
		String s = maxSolution + ": [";
		s += bestSolution[1];
		for (int n = 2; n <= N; n++)
		{
			s += ", " + bestSolution[n];
		}
		s += "]\n\nDiagnostics:";
		s += "\nTime spent on fillSolution(int): " + timeFillSolutionInt;
		s += "\nTime spent on fillSolution(int, int): " + timeFillSolutionIntInt;
		s += "\nTime spent on initializing fillSolution(int, int): " + timeInit;
		s += "\nTime spent on searching: " + timeSearch;
		s += "\nTime spent on inserting: " + timeInsert;
		s += "\nTime spent on copying arrays: " + timeArrayCopy;
		return s;
	}
	
}
