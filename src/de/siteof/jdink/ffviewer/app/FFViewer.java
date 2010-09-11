/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.ffviewer.app;

import javax.swing.JFrame;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FFViewer {

	public static void main(String[] args) {
		String resourceName	= null;
		if (args.length > 0) {
			resourceName	= args[0];
		}
		if (resourceName == null) {
			resourceName	= "M:\\_programs_\\_games_\\_rpg_\\Dink Smallwood\\dink\\graphics\\Dink\\walk\\dir.ff";
		}
		FFViewerFrame frame = new FFViewerFrame(resourceName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("FF Viewer");
		frame.pack();
		frame.setVisible(true);
	}
}
