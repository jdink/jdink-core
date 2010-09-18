package de.siteof.jdink.app;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.brain.JDinkChoiceBrain;
import de.siteof.jdink.brain.JDinkItemMenuBrain;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.control.JDinkKeyEventsHandler;
import de.siteof.jdink.loader.JDinkIniLoader;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.model.JDinkTile;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.status.JDinkStatusManager;
import de.siteof.jdink.util.FileUtil;
import de.siteof.jdink.view.ColorConstants;

public class JDinkGameManager {

	private static final String INSTANCE_KEY = "gameManager";

	private static final Log log = LogFactory.getLog(JDinkGameManager.class);

	private File gameHome;

	public static JDinkGameManager getInstance(JDinkContext context) {
		JDinkGameManager gameManager = context.getMetaData(
				INSTANCE_KEY, JDinkGameManager.class);
		if (gameManager == null) {
			gameManager = new JDinkGameManager();
			context.setMetaData(INSTANCE_KEY, gameManager);
		}
		return gameManager;
	}

	private File getFile(File parent, String path) {
		File result;
		path	= path.replace('\\', '/');
		if ((path.startsWith("/")) || (path.indexOf(":") >= 0)) {
			result	= new File(path);
		} else {
			result	= new File(parent, path);
		}
		result	= FileUtil.getFileIgnoreCase(result);
		return result;
	}

	private void initPlayer(JDinkContext context, JDinkSprite sprite) {
		JDinkPlayer player = new JDinkPlayer(sprite.getSpriteNumber());
		player.setBasePush(310);
		context.setCurrentPlayer(player);

		// update_frame(733)
		sprite.setBrainNumber(1);
		sprite.setSequenceNumber(2);
		sprite.setFrameNumber(1);
		sprite.setAnimationSequenceNumber(2);
		sprite.setAnimationFrameNumber(0);
		sprite.setOriginalAnimationSequenceNumber(0);
		sprite.setDirectionIndex(2);
		sprite.setStrength(10);
		sprite.setDefense(0);
		sprite.setBaseIdle(10);
		sprite.setBaseWalk(-1);
		sprite.setBaseHit(100);
		sprite.setSize(100);
		sprite.setTiming(33);
		sprite.setDamage(0);
		sprite.setBaseDie(-1);
		sprite.setText(null);
		sprite.setTouchDamage(0);
		sprite.setHitPoints(0);
		sprite.setBaseBloodSequenceNumber(0);
		sprite.setNextAnimationTime(0);

		sprite.setSequence(context.getSequence(sprite.getSequenceNumber(), false));
		sprite.setBrain(context.getBrain(sprite.getBrainNumber()));
//        spr[1].timer = 0;
//        spr[1].hard = 1;
//        spr[1].damage = 0;
//        spr[1].skip = 0;
//        SetRect(&spr[1].alt,0,0,0,0);
//        spr[1].active = TRUE;

	}

	public void start(JDinkContext context) throws Throwable {
		if (log.isInfoEnabled()) {
			log.info("[start] starting game (" + gameHome + ")");
		}

		JDinkChoiceBrain.getInstance().hideChoiceMenu(context);
		JDinkItemMenuBrain.getInstance().hideItemMenu(context);
		JDinkStatusManager.getInstance(context).hideStatus(context);

		JDinkController controller = context.getController();
		controller.clearState(context);
		controller.setTiles(new JDinkTile[0]);

		context.clearGame();

		controller.setGameMode(0);

		JDinkIniLoader iniLoader = new JDinkIniLoader();
		iniLoader.setContext(context);
		iniLoader.load(getFile(gameHome, "dink.ini"));

		initPlayer(context, context.getController().getSprite(1, true));

		// unfortunately it is hard coded that 442 is transparent
		JDinkSequence sequence = context.getSequence(442, false);
		if (sequence != null) {
			sequence.setBackgroundColor(ColorConstants.WHITE);
		}

		JDinkScriptInstance scriptInstance = new JDinkScriptInstance();
		JDinkScriptFile scriptFile = context.getScript("main", true);
		scriptInstance.setScriptFile(scriptFile);
		scriptInstance.initialize(context); // run main

		context.getController().addFrameEventListener(new JDinkKeyEventsHandler());

		scriptFile = context.getScript("start", true);
		scriptInstance = new JDinkScriptInstance();
		scriptInstance.setScriptFile(scriptFile);
		scriptInstance.initialize(context); // run main

		controller.setChanged(true);

		log.info("[start] done starting game");
	}

	public File getGameHome() {
		return gameHome;
	}

	public void setGameHome(File gameHome) {
		this.gameHome = gameHome;
	}

}
