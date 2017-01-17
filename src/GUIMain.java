import java.awt.*; 
import javax.swing.*;

public class GUIMain {
	public static void main(String[] args)	{
		JFrame f = new JFrame("SWN Sector Database");			//JFrame = Fenster allgemein
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	//Schlie�en-Funktion bei KLick auf [x]
		f.setSize(1600,900); 
		
		/* Beginn: Anweisung f�r Setzen des Fensters in die Mitte des Bildschirms */
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - f.getSize().width) / 2;
		int y = (d.height - f.getSize().height) / 2 ;
		f.setLocation(x,y);
		
		/* Ende: Setzen Fenster ( awt - n�tig ) */
		
		f.setResizable(true);								//Fenstergr��e nicht ver�nderbar
		
		GUIDatenbank guiPanel = new GUIDatenbank();			//Erstelle was im Fenster angezeigt werden soll
		f.getContentPane().add(guiPanel);					//F�ge das Panel zum Jframe (Fenster) hinzu
		
		guiPanel.setVisible(true);
		f.setVisible(true); 								//Setze JFrrame & Panel sichtbar => Wird angezeigt
	}
}
