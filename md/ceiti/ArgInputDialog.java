package md.ceiti;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ArgInputDialog extends JDialog implements ActionListener
{	
	public static class NoAnswerException extends RuntimeException {} // exceptie verificata
	
	private static final String[][] LABELS = {
		{ "Numarul de ordine", "Orientarea" },
		{ "a", "b", "c", "d" },
		{ "Ocupat sau nu?" }
	};

	private JTextField[] texts;
	private boolean[] flags = new boolean[1];
	
	private ArgInputDialog(int status, String title)
	{
		super((JFrame) null, title == null ? "Introduceti datele" : title, true);	

		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.QUESTION_DIALOG);
		
		JPanel inputs = new JPanel();
			inputs.setLayout(new BoxLayout(inputs, BoxLayout.PAGE_AXIS));
			
		final int N = LABELS[status].length;
		
		texts = new JTextField[N];
		
		for(int i = 0; i < N; ++i)
		{			
			texts[i] = new JTextField();
			texts[i].setBorder(
				BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
					LABELS[status][i]
				)
			);

			inputs.add(texts[i]);
		}

		JButton button = new JButton("Finisat");
			button.addActionListener(this);
			
		add(inputs, BorderLayout.CENTER);
		add(button, BorderLayout.PAGE_END);
	
		pack();
		setSize(140, getHeight());
		
		setResizable(false);
		setVisible(true);
	}
	
	public static Object getValues(int status, String title)
	{
		ArgInputDialog aid = new ArgInputDialog(status, title);
		
		if(!aid.flags[0])
			throw new NoAnswerException();
		
		java.util.function.Supplier<?>[] lambdas = {
			() -> {
				return new Object[]{
					Integer.parseInt(aid.texts[0].getText()),
					aid.texts[1].getText()
				};
			}
			,
			() -> {
				int[] args = new int[aid.texts.length];
				
				for(int i = 0; i < aid.texts.length; ++i)
					args[i] = Integer.parseInt(aid.texts[i].getText());
				
				return args;
			}
			,
			() -> {
				java.util.Map<String, Boolean> map = new java.util.HashMap<>();
				map.put("ocupat", true);
				map.put("yes", true);
				map.put("da", true);
				map.put("1", true);
				
				return map.getOrDefault(aid.texts[0].getText().toLowerCase(), false);
			}
		};

		return lambdas[status].get();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{ 
		flags[0] = true;
		dispose(); 
	}
}