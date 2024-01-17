package md.ceiti;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuFrame extends JFrame implements ActionListener
{
	private static final String PRINT = 
		"Programul Depozit \u00a9 Cem\u00eertan Cristian, 2020\n" +
		"Alege\u021bi sarcina de mai jos:\n" +
		"\t1. Interschimbarea, \u00een planul depozitului, primei cu ultima linie;\n" +
		"\t2. Reformarea tunelului, dup\u0103 num\u0103rul de ordine \u0219i orientarea introduse de la tastatur\u0103;\n" +
		"\t3. Creearea fi\u0219ierului, unde se va \u00eenscrie versiunea binar\u0103 a labirintului;\n" +
		"\t4. Determinarea num\u0103rului a unui tunel vertical, cu num\u0103rul minimal de depozite ocupate;\n" +
		"\t5. Afisarea pe ecran lista tunelurilor verticale, \u00een ordinea descendent\u0103 dup\u0103 num\u0103rul lor de zone ocupate;\n" +
		"\t6. Determinarea adreselor zonelor de depozitare libere, care nu au zone vecine ocupate;\n" +
		"\t7. Determinarea ariei maxime \u00een harta depozitului, inclusiv \u0219i coordonatele col\u021burilor st\u00e2nga-sus \u0219i dreapta-jos;\n" +
		"\t8. Determinarea celui mai scurt drum de parcurs, de la zona [a, b] \u00een zona [c, d].";
	
	public MenuFrame()
	{
		super("Depozit");
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		
		JPanel 
			menuText = new JPanel(), 
			buttonsArea = new JPanel();
	
		JTextArea text = new JTextArea(PRINT);
			text.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			text.setEditable(false);
		
		menuText.setBackground(Color.WHITE);
		menuText.add(text);

		add(menuText, BorderLayout.PAGE_START);
		
		JButton[] buttons = new JButton[8]; // sunt 8 sarcini
		
		for(int i = 0; i < buttons.length; ++i)
		{
			buttons[i] = new JButton(i + 1 + "");
			buttons[i].addActionListener(this);
			
			buttonsArea.add(buttons[i]);
		}
		
		add(buttonsArea, BorderLayout.PAGE_END);
		pack();
		
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			DepozitTestIO.menu(Integer.parseInt(((JButton) e.getSource()).getText()), this);
		}
		catch(java.io.IOException ex)
		{
			JOptionPane.showMessageDialog(this,
				"Eroare de fi\u0219ier.",
				"Eroare",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}