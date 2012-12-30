package de.siteof.jdink.functions.ini.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.ini.JDinkLoadSequenceFunction;
import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.loader.JDinkLazyLoader;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSequenceFrame;
import de.siteof.jdink.model.JDinkSequenceFrameAttributes;

public class JDinkSequenceLazyLoader implements JDinkLazyLoader {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory
			.getLog(JDinkLoadSequenceFunction.class);

	private final JDinkContext context;
	private final int sequenceNumber;
	private final String fileNamePrefix;
	private final Integer offsetX;
	private final Integer offsetY;
	private final Integer hardX1;
	private final Integer hardY1;
	private final Integer hardX2;
	private final Integer hardY2;
	private boolean loadImages;

	private Map<Integer, JDinkSpriteInfo> spriteInfoMap;

	public JDinkSequenceLazyLoader(JDinkContext context, int sequenceNumber, String fileNamePrefix,
			Integer offsetX, Integer offsetY, Integer hardX1, Integer hardY1,
			Integer hardX2, Integer hardY2) {
		this.context = context;
		this.sequenceNumber = sequenceNumber;
		this.fileNamePrefix = fileNamePrefix;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.hardX1 = hardX1;
		this.hardY1 = hardY1;
		this.hardX2 = hardX2;
		this.hardY2 = hardY2;
	}

	public void setSpriteInfo(int frameNumber, JDinkSpriteInfo spriteInfo) {
		synchronized (this) {
			if (spriteInfoMap == null) {
				spriteInfoMap = new HashMap<Integer, JDinkSpriteInfo>();
			}
			spriteInfoMap.put(Integer.valueOf(frameNumber), spriteInfo);
		}
	}

	@Override
	public boolean load(Object source) {
		try {
			JDinkSequence sequence = (JDinkSequence) source;
			int frameCount = 0;
			// String[] fileNames =
			// context.getFileManager().getResourceNamesByPrefix(fileNamePrefix);
			String[] fileNames = context.getFileManager()
					.getResourceNamesByPrefix(fileNamePrefix, false);
			if (fileNames.length == 0) {
				fileNames = context.getFileManager().getResourceNamesByPrefix(
						fileNamePrefix);
			}
			// Collection sequenceFileNames = new ArrayList();
			while (frameCount < 100) {
				String fileName = null;
				int frameNumber = frameCount + 1;
				String numberString = Integer.toString(frameNumber / 10)
						+ Integer.toString(frameNumber % 10);
				for (int i = 0; i < fileNames.length; i++) {
					if (fileNames[i].substring(fileNamePrefix.length(),
							fileNamePrefix.length() + 2).equals(numberString)) {
						fileName = fileNames[i];
						break;
					}
				}
				if (fileName != null) {
					JDinkSequenceFrame frame = sequence.getFrame(frameNumber,
							true);
					frame.setFileName(fileName);
					if (spriteInfoMap != null) {
						JDinkSpriteInfo spriteInfo = spriteInfoMap.get(Integer
								.valueOf(frameNumber));
						if (spriteInfo != null) {
							if ((spriteInfo.getOffsetX() != null)
									&& (spriteInfo.getOffsetY() != null)
									&& (spriteInfo.getHardX1() != null)
									&& (spriteInfo.getHardY1() != null)
									&& (spriteInfo.getHardX2() != null)
									&& (spriteInfo.getHardY2() != null)) {
								JDinkRectangle bounds = frame.getBounds();
								int frameBoundsX = -spriteInfo.getOffsetX()
										.intValue();
								int frameBoundsY = -spriteInfo.getOffsetY()
										.intValue();
								if (bounds == null) {
									// create a new bounds rectangle with no
									// size
									bounds = new JDinkRectangle(frameBoundsX,
											frameBoundsY, 0, 0);
								} else {
									// set the location of the bounds
									bounds = bounds.getLocatedTo(frameBoundsX,
											frameBoundsY);
								}
								frame.setBounds(bounds);
								frame.setCollisionShape(JDinkRectangle.between(
										spriteInfo.getHardX1().intValue(),
										spriteInfo.getHardY1().intValue(),
										spriteInfo.getHardX2().intValue(),
										spriteInfo.getHardY2().intValue()));
							}
						}
					}
					if (loadImages) {
						context.getImage(sequence, frame);
					}
					frameCount++;
				} else {
					// no file found
					break;
				}
			}
			Map<Integer, JDinkSequenceFrameAttributes> frameAttributeMap =
				context.getFrameAttributeMapBySequenceNumber(sequenceNumber);
			for (Map.Entry<Integer, JDinkSequenceFrameAttributes> entry: frameAttributeMap.entrySet()) {
				int frameNumber = entry.getKey().intValue();
				JDinkSequenceFrameAttributes attributes = entry.getValue();
				if ((attributes.getSourceSequenceNumber() > 0) && (attributes.getSourceFrameNumber() >= 0)) {
					JDinkSequence sourceSequence;
					if (attributes.getSourceSequenceNumber() == sequenceNumber) {
						sourceSequence = sequence;
					} else {
						sourceSequence = context.getSequence(attributes.getSourceSequenceNumber());
					}
					if (sourceSequence != null) {
						// override frame
						JDinkSequenceFrame sourceFrame = sourceSequence.getFrame(
								attributes.getSourceFrameNumber(), false);
						if (sourceFrame != null) {
							JDinkSequenceFrame frame = sequence.getFrame(
									frameNumber, true);
							frame.setFileName(sourceFrame.getFileName());
							frame.setImage(sourceFrame.getImage());
							//sequence.setFrame(frameNumber, sourceFrame);
						} else {
							log.warn("[load] source frame not found, sourceSequenceNumer=" +
									attributes.getSourceSequenceNumber() +
									", sourceFrameNumber=" + attributes.getSourceFrameNumber());
						}
					} else {
						log.warn("[load] source sequence not found, sourceSequenceNumer=" +
								attributes.getSourceSequenceNumber());
					}
				}
			}
			if ((offsetX != null) && (offsetX.intValue() != 0)
					&& (hardX2 != null) && (hardX2.intValue() > 0)
					&& (hardY2 != null) && (hardY2.intValue() > 0)) {
				sequence.setOffsetX(offsetX.intValue());
				sequence.setOffsetY(offsetY.intValue());
				sequence.setCollisionShape(JDinkRectangle.between(hardX1
						.intValue(), hardY1.intValue(), hardX2.intValue(),
						hardY2.intValue()));
			} else {
//				if (log.isInfoEnabled()) {
//					log
//							.info("offset/hard  values not set, attempting to calculate them. "
//									+ "fileNamePrefix=[" + fileNamePrefix + "]");
//				}
				/*
				for (int iFrame = 1; iFrame <= frameCount; iFrame++) {
					JDinkSequenceFrame frame = sequence.getFrame(iFrame, false);
					if (frame == null) {
						continue;
					}
					JDinkImage image = context.getImage(sequence, frame);
					if (image == null) {
						continue;
					}
					int width = image.getWidth();
					int height = image.getHeight();
					if ((width > 0) && (height > 0)) {
//						sequence.setOffsetX(width - (width / 2) + (width / 6));
//						sequence.setOffsetY(height - (height / 4)
//								- (height / 30));
						frame
								.setBounds(frame.getBounds().getLocatedTo(
										-sequence.getOffsetX(),
										-sequence.getOffsetY()));
						// frame.getBounds().setLocation(-sequence.getOffsetX(),
						// -sequence.getOffsetY());
						// TODO this seems incorrect (if X2 not set or valid?):
						if ((hardX2 == null) || (hardX2.intValue() > 0)) {
							int t = width / 4;
							hardX1 = new Integer(-t);
							hardX2 = new Integer(t);
						}
						if ((hardY2 == null) || (hardY2.intValue() > 0)) {
							int t = height / 10;
							hardY1 = new Integer(-t);
							hardY2 = new Integer(t);
						}
						sequence.setCollisionShape(new JDinkRectangle(hardX1
								.intValue(), hardY1.intValue(), hardX2
								.intValue()
								- hardX1.intValue(), hardY2.intValue()
								- hardY1.intValue()));
						break;
					}
				}
				*/
			}

			// for (int i = 0; i < fileNames.length; i++) {
			// String fileName = fileNames[i];
			// InputStream in =
			// executionContext.getContext().getFileManager().getResourceAsStream(fileName);
			// if (in != null) {
			// log.debug("file loaded:" + fileName);
			// } else {
			// log.debug("file not found:" + fileName);
			// System.exit(0);
			// }
			// }
			if (fileNames.length == 0) {
				if (log.isWarnEnabled()) {
					log.warn("no files found for:" + fileNamePrefix);
				}
			}
			return true;
		} catch (IOException e) {
			log.error("error loading sequence (" + source + ")" + " - " + e, e);
		}
		return true;
	}

	public String getFileNamePrefix() {
		return fileNamePrefix;
	}
}