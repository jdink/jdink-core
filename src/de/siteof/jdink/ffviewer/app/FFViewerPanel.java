/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.ffviewer.app;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.format.ff.FFLoader;
import de.siteof.swing.util.ImageCanvas;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FFViewerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected class PrevAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public PrevAction() {
			this.putValue(Action.NAME, "prev");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			setIndex((currentIndex + fileNames.length - 1) % fileNames.length);
		}

	}

	protected class NextAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NextAction() {
			this.putValue(Action.NAME, "next");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			setIndex((currentIndex + 1) % fileNames.length);
		}

	}

	protected class ExtractAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ExtractAction() {
			this.putValue(Action.NAME, "Extract");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event) {
			File targetDirectory = new File(resourceName + "-extracted");
			if (!targetDirectory.exists()) {
				if (!targetDirectory.mkdir()) {
					log.error("unable to create director: " + targetDirectory);
					return;
				}
			}
			byte[] buffer = new byte[4 * 1024];
			for (String fileName: fileNames) {
				InputStream in = null;
				OutputStream out = null;
				try {
					System.out.println("extracting: " + fileName);
					in = loader.getResourceAsStream(fileName);
					out = new FileOutputStream(new File(targetDirectory, fileName));
					while (true) {
						int bytesRead = in.read(buffer);
						if (bytesRead < 0) {
							break;
						}
						out.write(buffer, 0, bytesRead);
					}
				} catch (IOException e) {
					log.error("failed to extract file: " + fileName, e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							log.error("failed to close file", e);
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							log.error("failed to close file", e);
						}
					}
				}
			}
			//setIndex((currentIndex + 1) % fileNames.length);
		}

	}


	private FFLoader loader;
	private String resourceName;
	private Image[] images;
	private String[] fileNames;
	private int currentIndex = -1;
	private ImageCanvas imageCanvas;
	private JLabel statusLabel;

	private static final Log log	= LogFactory.getLog(FFViewerPanel.class);


	public FFViewerPanel(String resourceName) {
		this.resourceName = resourceName;
		FFLoader loader = new FFLoader();
		this.loader = loader;
		File file	= new File(resourceName);
		try {
			loader.load(file);
			fileNames = loader.getFileNames();
			images = new Image[fileNames.length];
//			Toolkit toolkit = Toolkit.getDefaultToolkit();
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = fileNames[i].toLowerCase();
				if (fileName.endsWith(".bmp")) {
					InputStream in = loader.getResourceAsStream(fileNames[i]);
					try {
						Image image = ImageIO.read(in);
						images[i] = image;
					} finally {
						if (in != null) {
							in.close();
						}
					}
					//Image image = toolkit.createImage(loader.getResourceBytes(fileNames[i]));
				}
			}
		} catch (FileNotFoundException e) {
			log.error("unable to load file(" + file + ") - " + e, e);
		} catch (IOException e) {
			log.error("unable to load file(" + file + ") - " + e, e);
		}
		this.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton(new PrevAction()));
		buttonPanel.add(new JButton(new NextAction()));
		buttonPanel.add(new JButton(new ExtractAction()));
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(imageCanvas = new ImageCanvas(), BorderLayout.CENTER);
		statusLabel = new JLabel();
		statusLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.add(statusLabel, BorderLayout.SOUTH);
		this.setIndex(0);
		this.setPreferredSize(new Dimension(400, 400));
	}

	protected void setIndex(int index) {
		if (index != currentIndex) {
			currentIndex = index;
			if (images != null) {
				imageCanvas.setImage(images[index]);
				statusLabel.setText(fileNames[index] + "  (" + (index+1) + "/" + fileNames.length + ")");
				imageCanvas.repaint();
			}
		}
	}

}
