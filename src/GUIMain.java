import java.awt.*; 
import javax.swing.*;

public class GUIMain {
	public static void main(String[] args)	{
		JFrame f = new JFrame("SWN Sector Database");			//JFrame = Fenster allgemein
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	//Schließen-Funktion bei KLick auf [x]
		f.setSize(1600,900); 
		
		/* Beginn: Anweisung für Setzen des Fensters in die Mitte des Bildschirms */
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - f.getSize().width) / 2;
		int y = (d.height - f.getSize().height) / 2 ;
		f.setLocation(x,y);
		
		/* Ende: Setzen Fenster ( awt - nötig ) */
		
		f.setResizable(true);								//Fenstergröße nicht veränderbar
		
		GUIDatenbank guiPanel = new GUIDatenbank();			//Erstelle was im Fenster angezeigt werden soll
		f.getContentPane().add(guiPanel);					//Füge das Panel zum Jframe (Fenster) hinzu
		
		guiPanel.setVisible(true);
		f.setVisible(true); 								//Setze JFrrame & Panel sichtbar => Wird angezeigt
	}
}
