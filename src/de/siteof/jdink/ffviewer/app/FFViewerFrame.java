/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.ffviewer.app;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FFViewerFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public FFViewerFrame(String resourceName) {
		FFViewerPanel panel = new FFViewerPanel(resourceName);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}
}
