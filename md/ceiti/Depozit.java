package md.ceiti;
import java.io.*;

public class Depozit
{
	static final String[] FILENAMES = {
		"Depozit.in",
		"Depozit.out",
		"Binar.txt"
	};
	
	private static final String NO_ANS = "Nu exista nici o solutie.";
	private boolean[][] labirint;
	private int x, y;
	
	public Depozit() throws IOException
	{
		try(java.util.Scanner fileIn = new java.util.Scanner(new File(FILENAMES[0])))
		{
			x = fileIn.nextInt();
			y = fileIn.nextInt();
			labirint = new boolean[x][y];
			
			fileIn.nextInt(); // ignor
			
			while(fileIn.hasNext())
				try
				{
					labirint[fileIn.nextInt() - 1][fileIn.nextInt() - 1] = true;
				}
				catch(ArrayIndexOutOfBoundsException e) {}
		}
	}
	
	public static void sarcina_1() throws IOException
	{
		java.util.List<String> buffer = new java.util.ArrayList<>();
		
		try(java.util.Scanner file = new java.util.Scanner(new File(FILENAMES[0])))
		{
			while(file.hasNext())
				buffer.add(file.nextLine());
			
			java.util.Collections.swap(buffer, 2, buffer.size() - 1);
		}
		
		try(PrintStream file = new PrintStream(FILENAMES[0]))
		{
			for(String x : buffer)
				file.println(x);
		}
	}
	
	public String sarcina_2(int nrOrdine, String orientare) throws IOException
	{
		java.util.function.BiFunction<Integer, Integer, Boolean> getValues = (i, b) ->
			(boolean) ArgInputDialog.getValues(2, String.format("%d/%d", i, b));
		
		java.util.Map<String, Runnable> orientari = new java.util.HashMap<>();			
		StringBuilder msg = new StringBuilder();
		
		orientari.put("orizontal", () -> {
			for(int i = 0; i < y; ++i)
			{
				labirint[nrOrdine][i] = getValues.apply(i + 1, y);
				msg.append(String.format("\u2550%c", labirint[nrOrdine][i] ? '\u25cf' : '\u25cb'));
			}
			
			msg.append('\u2550');
		});
		
		orientari.put("vertical", () -> {
			for(int i = 0; i < x; ++i)
			{
				labirint[i][nrOrdine] = getValues.apply(i + 1, x);
				msg.append(String.format("\u2551\n%c\n", labirint[i][nrOrdine] ? '\u25cf' : '\u25cb'));
			}
			
			msg.append('\u2551');
		});
		
		try
		{
			orientari.get(orientare.toLowerCase()).run();
		}
		catch(NullPointerException | ArrayIndexOutOfBoundsException e)
		{
			throw new IllegalArgumentException();
		}
		
		try(PrintStream fileOut = new PrintStream(FILENAMES[0]))
		{
			StringBuilder buffer = new StringBuilder();
			int c = 0;
			
			for(int j, i = 0; i < x; ++i)
				for(j = 0; j < y; ++j)
					if(labirint[i][j])
					{
						buffer.append(String.format("%d %d%n", i + 1, j + 1));
						++c;
					}
					
			fileOut.printf("%d %d%n%d%n%s", x, y, c, buffer);
		}
		
		return msg.toString();
	}
	
	public void sarcina_3() throws IOException
	{
		try(PrintStream fileOut = new PrintStream(FILENAMES[2]))
		{
			for(int j, i = 0; i < x; ++i)
			{
				for(j = 0; j < y; ++j)
					fileOut.printf("%c ", labirint[i][j] ? '1' : '0');
				
				fileOut.println();
			}
		}
	}
	
	// sarcina 4 este deja implementata in DepozitTestIO
	
	public int[][] sarcina_5()
	{
		int[][] data = new int[y][2];
		int[] swp;
		
		int mn, j, i, n = y;
		
		for(i = 0; i < y; ++i)
		{
			data[i][1] = i + 1;
			
			for(j = 0; j < x; ++j)
				if(labirint[j][i])
					++data[i][0];
		}

		for(i = 1; i < y; ++i)
		{			
			mn = 0;
			
			for(j = 1; j < n; ++j)
				if(data[j][0] < data[mn][0])
					mn = j;
				
			swp = data[--n];
			data[n] = data[mn];
			data[mn] = swp;
		}
		
		return data;
	}
	
	public java.util.List<int[]> sarcina_6()
	{		
		int 
			j, i,
			x = this.x - 2, 
			y = this.y - 2;
			
		java.util.List<int[]> adrese = new java.util.ArrayList<>();
		
		for(i = 1; i <= x; ++i)
			for(j = 1; j <= y; ++j)
				if(	!labirint[i][j] &&
					!labirint[i + 1][j] && !labirint[i][j + 1] &&
					!labirint[i - 1][j] && !labirint[i][j - 1])
					adrese.add(new int[]{i + 1, j + 1});

		return adrese;
	}

	private boolean isRectangle(int a, int b, int c, int d) 
	// reduce de la O(n ^ 2) la O(n) datorita multithreading-ului paralel
	{
		int j = 0;
		boolean[] results = {true, true};
		
		Thread[] futures = {
			new Thread(() -> { // verific vertical
				for(int i = b; i <= d && results[1]; ++i)
					if(labirint[a][i] || labirint[c][i])
					{
						results[0] = false;
						return;
					}
			})
			,
			new Thread(() -> { // verific orizontal
				for(int i = a + 1; i < c && results[0]; ++i)
					if(labirint[i][b] || labirint[i][d])
					{
						results[1] = false;
						return;
					}
			})
		};
		
		try
		{
			for(j = 0; j < futures.length; ++j)
				futures[j].start();
		
			for(j = 0; j < futures.length; ++j)
				futures[j].join();
			
			return results[0] && results[1];
		}
		catch(InterruptedException e)
		{
			return false;
		}

	}
	
	public int[][] sarcina_7()
	{
		int[][] mx = new int[][]{
			{1},
			{0, 0},
			{0, 0}
		};
		
		int area;
		
		for(int l, k, j, i = 0; i < x; ++i) // brute-force, O(n ^ 5) !!!
			for(j = 0; j < y; ++j)
				for(k = i; k < x; ++k)
					for(l = j; l < y; ++l)
						if(isRectangle(i, j, k, l))
						{
							area = (l - j + 1) * (k - i + 1);
							
							if(area > mx[0][0])
							{
								mx[0][0] = area;
								
								mx[1][0] = i + 1;
								mx[1][1] = j + 1;
								
								mx[2][0] = k + 1;
								mx[2][1] = l + 1;
							}
						}
		
		return mx;
	}

	public String[] sarcina_8(int a, int b, int c, int d) throws IOException
	{
		class Lee 
		{
			int[][] labirintCloned = new int[x][y];
			
			int[][] moves = {
				{0, 1},
				{1, 0}, 
				{0, -1}, 
				{-1, 0}
			};
			
			java.util.Queue<int[]> ways = new java.util.LinkedList<>();
		
			Lee(int $x, int $y)
			{ 
				for(int j, i = 0; i < x; ++i)
					for(j = 0; j < y; ++j)
						labirintCloned[i][j] = labirint[i][j] ? -2 : -1; // ? ocupat : liber
				
				if(!check(c, d) || !check(a, b) || !go($x, $y))
					throw new IllegalArgumentException(NO_ANS);
			}
			
			boolean check(int x, int y)
			{
				try
				{
					return labirintCloned[x][y] == -1;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					return false;
				}
			}
			
			boolean go(int x, int y)
			{
				ways.add(new int[]{x, y});

				int[] pos;
				
				for(int xp, yp, i; !ways.isEmpty();)
				{					
					pos = ways.element();
					ways.remove();
					
					if(pos[0] == c && pos[1] == d)
						return true;
		
					for(i = 0; i < moves.length; ++i)
					{
						xp = pos[0] + moves[i][0];
						yp = pos[1] + moves[i][1];
						
						if(check(xp, yp))
						{
							ways.add(new int[]{xp, yp});
							labirintCloned[xp][yp] = (i + 2) & 3;
						}
					}
				}
				
				return false;
			}
		
			java.util.Collection<int[]> parse()
			{
				java.util.Deque<int[]> mnWays = new java.util.ArrayDeque<>();
				mnWays.push(new int[]{c, d});
				
				for(int dir, x = c, y = d; x != a || y != b;)
				{
					dir = labirintCloned[x][y];
					x += moves[dir][0];
					y += moves[dir][1];
					
					mnWays.addFirst(new int[]{x, y});
				}
				
				return mnWays;
			}
		}
		
		java.util.Collection<int[]> mnWays;

		try
		{
			mnWays = new Lee(a, b).parse();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new IllegalArgumentException(NO_ANS);
		}
		
		char[][] printedLabirint = new char[x][y];
		int j, i;
		
		for(i = 0; i < x; ++i)
			for(j = 0; j < y; ++j)
				printedLabirint[i][j] = labirint[i][j] ? '\u25cf' : '\u25cb';
		
		StringBuilder steps = new StringBuilder(mnWays.size() - 1 + System.lineSeparator());

		for(int[] x : mnWays)
		{
			printedLabirint[x[0]][x[1]] = '\u25a0';
			steps.append(String.format("%d %d%n", x[0] + 1, x[1] + 1));
		}
		
		steps.deleteCharAt(steps.length() - 1);
		
		try(PrintStream fileOut = new PrintStream(FILENAMES[1]))
		{
			fileOut.print(steps);
		}
		finally
		{
			StringBuilder printedWays = new StringBuilder();
			boolean test;
			
			for(i = 0;; ++i)
			{				
				for(j = 0; j < y; ++j)
					printedWays.append(" \u2551");
				
				test = i >= x;
				
				if(test)
					break;
				
				printedWays.append('\n');
	
				for(j = 0; j < y; ++j)
					printedWays.append("\u2550" + printedLabirint[i][j]);

				printedWays.append('\u2550');
				
				if(!test)
					printedWays.append('\n');
			}
			
			return new String[]{ printedWays.toString(), steps.toString() };
		}
	}
}