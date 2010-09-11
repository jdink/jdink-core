/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.functions.script;


import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.AbstractJDinkFunction;
import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.io.Resource;
import de.siteof.jdink.model.JDinkSound;

/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDinkPlaySoundFunction extends AbstractJDinkFunction {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(JDinkPlaySoundFunction.class);

	/* (non-Javadoc)
	 * @see de.siteof.jdink.functions.JDinkFunction#invoke(de.siteof.jdink.functions.JDinkExecutionContext)
	 */
	public Object invoke(JDinkExecutionContext executionContext) throws Exception {
		Integer soundNumber = toInteger(executionContext.getArgument(0), null);
		Integer minSpeed = toInteger(executionContext.getArgument(1), null);
		Integer randSpeedToAdd = toInteger(executionContext.getArgument(2), null);
		Integer spriteNumber = toInteger(executionContext.getArgument(3), null);
		Integer repeat = toInteger(executionContext.getArgument(4), null);
		assertNotNull(soundNumber, "play sound: soundNumber missing");
		assertNotNull(minSpeed, "play sound: minSpeed missing");
		assertNotNull(randSpeedToAdd, "play sound: randSpeedToAdd missing");
		assertNotNull(spriteNumber, "play sound: spriteNumber missing");
		assertNotNull(repeat, "play sound: repeat missing");
		if (log.isDebugEnabled()) {
			log.debug("play sound: soundNumber=" + soundNumber + " minSpeed=" + minSpeed +
					 " randSpeedToAdd=" + randSpeedToAdd + " spriteNumber=" + spriteNumber + " repeat=" + repeat);
		}
		boolean noSound = executionContext.getContext().getConfiguration().isSoundEnabled();
		if (noSound) {
			return null;
		}
		JDinkSound sound = executionContext.getContext().getSound(soundNumber.intValue());
		if (sound != null) {
			Resource resource = sound.getResource();
			if (resource != null) {
				InputStream in = (resource != null ? resource.getInputStream() : null);
				if (in != null) {
					try {
						AudioInputStream stream = AudioSystem.getAudioInputStream(in);
						if (stream.getFormat().getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
			                log.debug("Converting Audio stream format");
			                stream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED,stream);
			            }
						DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
			            final Clip clip = (Clip) AudioSystem.getLine(info);
			            clip.open(stream);
			            clip.setMicrosecondPosition(0);
			            //clip.setFramePosition(0);
			            //clip.loop(0);
			            //clip.start();
			            clip.loop(0);
			            clip.addLineListener(new LineListener() {
			                public void update(LineEvent le) {
			                    if (le.getType().equals(LineEvent.Type.STOP)) {
			                        clip.close();
			                        //System.exit(0);
			                    }
			                }
			            });
			            //clip.drain();
			            //Thread.sleep(100);
			            if (log.isDebugEnabled()) {
			            	log.debug("sound: " + resource.getName() + "  active:" + clip.isActive());
			            }
						stream.close();
					} catch (Throwable e) {
						log.error("failed to play sound: " + resource.getName());
					} finally {
						in.close();
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("sound stream could not be opened: " + resource.getName());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("sound resoure not found");
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("sound not found: " + soundNumber);
			}
		}
		return null;
	}

}
