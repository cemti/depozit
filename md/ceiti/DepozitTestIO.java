package md.ceiti;
import java.awt.Font;
import javax.swing.*;

public final class DepozitTestIO
{		
	public static void main(String[] args)
	{
		new MenuFrame();
	}
	
	private static JPanel constructMessage(String in, int type, int size)
	{
		JPanel panel = new JPanel();

		JTextArea text = new JTextArea(in);
			text.setFont(new Font("Courier New", type, size));
			text.setEditable(false);
			
			if(getRowsCount(in) > 25)
				text.setRows(25);
			
			if(in.indexOf('\n') > 74)
				text.setColumns(75);
			
		panel.add(new JScrollPane(text));
		return panel;
	}
	
	private static int getRowsCount(String in)
	{
		int i = 0;
		for(char x : in.toCharArray())
			if(x == '\n')
				++i;
			
		return i;
	}
	
	static void menu(int choice, MenuFrame menuFrame) throws java.io.IOException
	{
		Object msg = "Sarcina a fost realizat\u0103 cu succes.";
		int state = JOptionPane.PLAIN_MESSAGE;
		
		try
		{
			Depozit test = new Depozit();
			
			switch(choice)
			{
				case 1:
					Depozit.sarcina_1();
					break;
					
				case 2:
				{
					Object[] args = (Object[]) ArgInputDialog.getValues(0, null);
					msg = constructMessage(test.sarcina_2((int) args[0] - 1, (String) args[1]), Font.PLAIN, 24);
					break;
				}
					
				case 3:
					test.sarcina_3();
					break;
					
				case 4:
				{
					int[][] sorted = test.sarcina_5();
					int ans = sorted[sorted.length - 1][0];

					msg = String.format("Num\u0103rul tunelului este %d, cu %d %s.", 
									     sorted[sorted.length - 1][1], ans, ans == 1 ? "loc ocupat" : "locuri ocupate"
					);
					break;
				}
				case 5:
				{
					StringBuilder msgB = new StringBuilder("Tunel \u2192 locuri ocupate\n");
		
					for(int[] x : test.sarcina_5())
						msgB.append(String.format("%d \u2192 %d\n", x[1], x[0]));
					
					msgB.deleteCharAt(msgB.length() - 1);
					
					msg = constructMessage(msgB.toString(), Font.BOLD, 16);
					break;
				}
				case 6:
				{
					StringBuilder msgB = new StringBuilder();
					
					for(int[] x : test.sarcina_6())
						msgB.append(String.format("%d %d\n", x[0], x[1]));
					
					msgB.deleteCharAt(msgB.length() - 1);
					
					msg = constructMessage(msgB.toString(), Font.BOLD, 16);
					break;
				}
				case 7:
				{
					int[][] S = test.sarcina_7();
					StringBuilder msgB = new StringBuilder(String.format(
						"Aria maximal\u0103: %d\n%s",
						S[0][0],
						"Coordonatele col\u021burilor:\n"
					));
					
					for(int i = 1; i < S.length; ++i)
						msgB.append(String.format("%d %d\n", S[i][0], S[i][1]));
					
					msgB.deleteCharAt(msgB.length() - 1);
					msg = msgB.toString();
					break;
				}
				case 8:
				{
					int[] args = (int[]) ArgInputDialog.getValues(1, null);
					String[] ans = test.sarcina_8(args[0] - 1, args[1] - 1, args[2] - 1, args[3] - 1);

					JPanel panel = constructMessage(ans[0], Font.PLAIN, 24);
						panel.add(constructMessage(ans[1], Font.BOLD, 16));
					
					msg = panel;
					break;
				}
			}
		}
		catch(StringIndexOutOfBoundsException e)
		{
			msg = "Nu exist\u0103 nici una.";
			state = JOptionPane.INFORMATION_MESSAGE;
		}
		catch(java.util.InputMismatchException e)
		{
			msg = "Verifica\u021bi datele de intrare.";
			state = JOptionPane.WARNING_MESSAGE;
		}
		catch(IllegalArgumentException e)
		{
			msg = e.getMessage();
			
			if(e instanceof NumberFormatException || msg == null)
				msg = "R\u0103spuns Invalid.";
			
			state = JOptionPane.WARNING_MESSAGE;
		}
		catch(ArgInputDialog.NoAnswerException e) { return; }
		
		JOptionPane.showMessageDialog(menuFrame,
			msg,
			"Rezultatul sarcinii " + choice,
			state
		);
	}
}