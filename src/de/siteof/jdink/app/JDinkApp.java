package de.siteof.jdink.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.brain.JDinkButtonBrain;
import de.siteof.jdink.brain.JDinkDefaultBrain;
import de.siteof.jdink.brain.JDinkMouseBrain;
import de.siteof.jdink.brain.JDinkOneTimeBackgroundBrain;
import de.siteof.jdink.brain.JDinkOneTimeBrain;
import de.siteof.jdink.brain.JDinkPeopleBrain;
import de.siteof.jdink.brain.JDinkPigBrain;
import de.siteof.jdink.brain.JDinkPillBrain;
import de.siteof.jdink.brain.JDinkPlayerBrain;
import de.siteof.jdink.brain.JDinkRepeatBrain;
import de.siteof.jdink.brain.JDinkScaleBrain;
import de.siteof.jdink.brain.JDinkTextBrain;
import de.siteof.jdink.brain.JDinkUnimplementedBrain;
import de.siteof.jdink.collision.JDinkConfiguration;
import de.siteof.jdink.control.JDinkController;
import de.siteof.jdink.control.JDinkKeyEventsHandler;
import de.siteof.jdink.format.map.JDinkMapLoader;
import de.siteof.jdink.functions.ini.JDinkLoadSequenceFunction;
import de.siteof.jdink.functions.ini.JDinkLoadSequenceNowFunction;
import de.siteof.jdink.functions.ini.JDinkSetFrameDelayFunction;
import de.siteof.jdink.functions.ini.JDinkSetFrameFrameFunction;
import de.siteof.jdink.functions.ini.JDinkSetFrameSpecialFunction;
import de.siteof.jdink.functions.ini.JDinkSetSpriteInfoFunction;
import de.siteof.jdink.functions.script.JDinkAddItemFunction;
import de.siteof.jdink.functions.script.JDinkArmWeaponFunction;
import de.siteof.jdink.functions.script.JDinkBreakpointFunction;
import de.siteof.jdink.functions.script.JDinkChoiceStartFunction;
import de.siteof.jdink.functions.script.JDinkDebugFunction;
import de.siteof.jdink.functions.script.JDinkDrawStatusFunction;
import de.siteof.jdink.functions.script.JDinkEditorFrameFunction;
import de.siteof.jdink.functions.script.JDinkEditorSeqFunction;
import de.siteof.jdink.functions.script.JDinkEditorTypeFunction;
import de.siteof.jdink.functions.script.JDinkExternalFunction;
import de.siteof.jdink.functions.script.JDinkFillScreenFunction;
import de.siteof.jdink.functions.script.JDinkGameExistFunction;
import de.siteof.jdink.functions.script.JDinkGetVersionFunction;
import de.siteof.jdink.functions.script.JDinkInitFunction;
import de.siteof.jdink.functions.script.JDinkInsideBoxFunction;
import de.siteof.jdink.functions.script.JDinkKillGameFunction;
import de.siteof.jdink.functions.script.JDinkKillThisTaskFunction;
import de.siteof.jdink.functions.script.JDinkLoadGameFunction;
import de.siteof.jdink.functions.script.JDinkLoadSoundFunction;
import de.siteof.jdink.functions.script.JDinkMakeGlobalIntFunction;
import de.siteof.jdink.functions.script.JDinkPlayMidiFunction;
import de.siteof.jdink.functions.script.JDinkPlaySoundFunction;
import de.siteof.jdink.functions.script.JDinkPreloadSeqFunction;
import de.siteof.jdink.functions.script.JDinkPushActiveFunction;
import de.siteof.jdink.functions.script.JDinkRandomFunction;
import de.siteof.jdink.functions.script.JDinkResetTimerFunction;
import de.siteof.jdink.functions.script.JDinkSaveGameFunction;
import de.siteof.jdink.functions.script.JDinkScreenLockFunction;
import de.siteof.jdink.functions.script.JDinkSetDinkBasePushFunction;
import de.siteof.jdink.functions.script.JDinkSetDinkSpeedFunction;
import de.siteof.jdink.functions.script.JDinkSetModeFunction;
import de.siteof.jdink.functions.script.JDinkStopCdFunction;
import de.siteof.jdink.functions.script.JDinkStopMidiFunction;
import de.siteof.jdink.functions.script.JDinkUnimplementedFunction;
import de.siteof.jdink.functions.script.JDinkWaitFunction;
import de.siteof.jdink.functions.script.sprite.JDinkCreateSpriteFunction;
import de.siteof.jdink.functions.script.sprite.JDinkFreezeFunction;
import de.siteof.jdink.functions.script.sprite.JDinkMoveFunction;
import de.siteof.jdink.functions.script.sprite.JDinkMoveStopFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSayFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSayStopFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSayStopXYFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSayXYFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpActiveFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpBaseAttackFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpBaseDeathFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpBaseWalkFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpBrainFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpBrainParam2Function;
import de.siteof.jdink.functions.script.sprite.JDinkSpBrainParamFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpDefenseFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpDirFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpDistanceFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpEditorNumFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpExpFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpFrameDelayFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpFrameFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpHardFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpHitPointsFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpKillWaitFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpNoControlFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpNoHitFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpNoTouchFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpNoclipFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpPFrameFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpPSeqFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpQueFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpReverseFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpScriptFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpSeqFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpSoundFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpSpeedFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpStrengthFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpTargetFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpTimingFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpTouchDamageFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpXFunction;
import de.siteof.jdink.functions.script.sprite.JDinkSpYFunction;
import de.siteof.jdink.functions.script.sprite.JDinkUnfreezeFunction;
import de.siteof.jdink.geom.JDinkPoint;
import de.siteof.jdink.interaction.JDinkHitInteractionHandler;
import de.siteof.jdink.interaction.JDinkInteractionHandler;
import de.siteof.jdink.interaction.JDinkInteractionManager;
import de.siteof.jdink.interaction.JDinkInteractionType;
import de.siteof.jdink.interaction.JDinkTalkInteractionHandler;
import de.siteof.jdink.interaction.JDinkTouchInteractionHandler;
import de.siteof.jdink.loader.JDinkFileManager;
import de.siteof.jdink.loader.JDinkIniLoader;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.model.JDinkPlayer;
import de.siteof.jdink.model.JDinkSequence;
import de.siteof.jdink.model.JDinkSprite;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptInstance;
import de.siteof.jdink.util.FileUtil;
import de.siteof.jdink.util.debug.JDinkObjectOutputUtil;
import de.siteof.jdink.util.debug.JDinkXStreamObjectOutput;
import de.siteof.jdink.view.ColorConstants;
import de.siteof.jdink.view.JDinkView;
import de.siteof.jdink.view.JDinkViewFactory;
import de.siteof.task.ITask;
import de.siteof.task.ITaskManager;
import de.siteof.task.RunnableTask;
import de.siteof.task.ThreadPoolTaskManager;

/**
 * <p>Main controller of the JDink application</p>
 */
public class JDinkApp {

	private static final String DINK_CONFIGURATION_PROPERTY_NAME	= "dink.configuration";

	private static final String PROPERTY_NAME_DINK_HOME	= "dink.home";
	private static final String PROPERTY_NAME_DMOD_HOME	= "dmod.home";

	private JDinkContext context;
	private final AtomicBoolean started = new AtomicBoolean();
	private final AtomicBoolean starting = new AtomicBoolean();
	private final AtomicBoolean updatingFrame = new AtomicBoolean();
	private final AtomicLong lastFrameUpdateTime = new AtomicLong();
	private final ITaskManager taskManager = new ThreadPoolTaskManager(10);
	private final ITask updateFrameTask;
//	private ThreadPool threadPool = new ThreadPool(10);

	private final long updateInterval = 50;
	private final long minimumInterval = 30;

	private Timer timer;

	private static final Log log	= LogFactory.getLog(JDinkApp.class);


	public JDinkApp() {
		updateFrameTask = new RunnableTask(new Runnable() {
			public void run() {
				try {
					if (context.getView().isStopping()) {
						// do it the hard way for now
						log.info("view is stopping, exiting application...");
						System.exit(0);
					}
					long now = System.currentTimeMillis();
					long previousTime = lastFrameUpdateTime.getAndSet(now);
					long deltaTime = now - previousTime;
					if (log.isDebugEnabled()) {
//						log.debug("updateFrame: since last time " + deltaTime + "ms");
					}
					if (deltaTime < minimumInterval) {
						if (log.isInfoEnabled()) {
							log.info("updateFrame: too low of a time difference: " + deltaTime + "ms");
						}
						updatingFrame.set(false);
					} else {
						boolean wasChanged = context.getController().isChanged();
						context.getController().setChanged(false);
						updatingFrame.set(false);
						context.getController().nextFrame(context);
						if ((context.getController().isChanged()) || (wasChanged)) {
							updateView();
							context.getController().setChanged(false);
//								canvas.repaint();
						}
					}
				} catch (Throwable e) {
					log.error("error calling nextFrame - " + e, e);
				}
//				updatingFrame.set(false);
			}});
	}

	private void updateFrame() {
		if (!started.get()) {
			if (!starting.getAndSet(true)) {
				taskManager.addTask(new RunnableTask(new Runnable() {
					public void run() {
						try {
							doStart();
						} catch (Throwable e) {
							log.error("error executing start - " + e, e);
						}
						started.set(true);
					}}));
			}
		} else {
			if (!updatingFrame.getAndSet(true)) {
				if (starting.getAndSet(false)) {
					this.context.getView().setSplashImage(null);
				}
				taskManager.addTask(updateFrameTask);
//				context.getController().nextFrame(context);
//				if (context.getController().isChanged()) {
//					context.getController().setChanged(false);
//					canvas.repaint();
//				}
			} else {
				log.debug("currently updating frame");
			}
		}
	}

	public void start() throws Throwable {
		long delay = updateInterval;

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateFrame();
			}}, 0, delay);
	}

	protected void loadMap() throws Throwable {
		JDinkMapLoader loader = new JDinkMapLoader();
		loader.load(context.getFileManager().getResource("map.dat"));
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

	private void updateView() {
//		log.info("updateView");
		context.getController().setChanged(true);
		context.getView().updateView();
		Thread.yield();
	}


	private int getColor(int red, int green, int blue) {
		return (((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF));
	}

	private int[] initFontColors(int[] fontColors) {
	    // Light Magenta
		fontColors[1] = getColor(255, 198, 255);

	    // Dark Green
		fontColors[2] = getColor(131, 181, 74);

	    // Bold Cyan
		fontColors[3] = getColor(99, 242, 247);

	    // Orange
	    fontColors[4] = getColor(255, 156, 74);

	    // Magenta
	    fontColors[5] = getColor(222, 173, 255);

	    // Brown Orange
	    fontColors[6] = getColor(244, 188, 73);

	    // Light Gray
	    fontColors[7] = getColor(173, 173, 173);

	    // Dark Gray
	    fontColors[8] = getColor(85, 85, 85);

	    // Sky Blue
	    fontColors[9] = getColor(148, 198, 255);

	    // Bright Green
	    fontColors[10] = getColor(0, 255, 0);

	    // Yellow
	    fontColors[11] = getColor(255, 255, 2);

	    // Yellow
	    fontColors[12] = getColor(255, 255, 2);

	    // Hot Pink
	    fontColors[13] = getColor(255, 132, 132);

	    // Yellow
	    fontColors[14] = getColor(255, 255, 2);

	    // White
	    fontColors[15] = getColor(255, 255, 255);

		return fontColors;
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
		sprite.setDirectionIndex(2);
		sprite.setStrength(10);
		sprite.setDefense(0);
		sprite.setBaseIdle(10);
		sprite.setBaseWalk(-1);
		sprite.setBaseHit(100);
		sprite.setSize(100);
		sprite.setTiming(33);

		sprite.setSequence(context.getSequence(sprite.getSequenceNumber(), false));
		sprite.setBrain(context.getBrain(sprite.getBrainNumber()));
//        spr[1].timer = 0;
//        spr[1].hard = 1;
//        spr[1].damage = 0;
//        spr[1].skip = 0;
//        SetRect(&spr[1].alt,0,0,0,0);
//        spr[1].active = TRUE;

	}


	private void doStart() throws Throwable {

		String dinkConfigurationFile = System.getProperty(
				DINK_CONFIGURATION_PROPERTY_NAME, "config/dink.properties");

		context = new JDinkContext();
		context.setController(new JDinkController());
		context.getController().setMousePosition(new JDinkPoint(0, 0));
		context.setFontColors(initFontColors(new int[16]));
		context.setTextBorderColor(getColor(8, 14, 21));

		JDinkView view = JDinkViewFactory.getInstance().createView();
		view.init(context);
		context.setView(view);

		Properties properties	= new Properties();
		InputStream in	= new FileInputStream(dinkConfigurationFile);
		try {
			properties.load(in);
		} finally {
			in.close();
		}

		Map<String, String> m = new HashMap<String, String>(properties.size());
		for (Map.Entry<Object, Object> entry: properties.entrySet()) {
			m.put((String) entry.getKey(), (String) entry.getValue());
		}

		this.context.setConfiguration(new JDinkConfiguration(m));

		String dinkHome	= (String) properties.get(PROPERTY_NAME_DINK_HOME);
		String dmodName	= (String) properties.get(PROPERTY_NAME_DMOD_HOME);
		if (dinkHome == null) {
			dinkHome	= "M:\\_programs_\\_games_\\_rpg_\\Dink Smallwood";
		}

		File dinkRoot = new File(dinkHome);

		if (dmodName == null) {
			dmodName	= "akt2";
		}
//		String dmod = "akt2";
		File fileDmod = getFile(dinkRoot, dmodName);
		File fileDefault = new File(dinkRoot, "dink");
		JDinkIniLoader iniLoader = new JDinkIniLoader();
		JDinkFileManager fileManager = new JDinkFileManager();
		fileManager.setPathParents(new File[] {fileDmod, fileDefault});
		context.setFileManager(fileManager);
		context.setImageLoader(view.getImageLoader());

		context.setGameId(fileDmod.getName());
		JDinkObjectOutputUtil.setObjectOutput(new JDinkXStreamObjectOutput(
				new File("./dump", context.getGameId())));

		//loadMap();
		if (true) {
			//return;
		}

		this.context.getView().setSplashImage(context.getImage("tiles/Splash.bmp"));
		this.updateView();
//		imageCanvas.repaint();

		Map<JDinkInteractionType, JDinkInteractionHandler> handlers =
			new HashMap<JDinkInteractionType, JDinkInteractionHandler>();
		handlers.put(JDinkInteractionType.TALK, new JDinkTalkInteractionHandler());
		handlers.put(JDinkInteractionType.HIT, new JDinkHitInteractionHandler());
		handlers.put(JDinkInteractionType.TOUCH, new JDinkTouchInteractionHandler());
		context.setInteractionManager(new JDinkInteractionManager(handlers));

		// see update_frame for list of brain types
		context.addBrain(0, new JDinkDefaultBrain());
		context.addBrain(1, new JDinkPlayerBrain());
		context.addBrain(3, new JDinkUnimplementedBrain(3, "duck"));
		context.addBrain(4, new JDinkPigBrain());
		context.addBrain(5, new JDinkOneTimeBackgroundBrain());
		context.addBrain(6, new JDinkRepeatBrain());
		context.addBrain(7, new JDinkOneTimeBrain());
		context.addBrain(8, new JDinkTextBrain());
		context.addBrain(9, new JDinkPillBrain());
		context.addBrain(10, new JDinkUnimplementedBrain(10, "dragon"));
		context.addBrain(11, new JDinkUnimplementedBrain(11, "missle"));
		context.addBrain(12, new JDinkScaleBrain());
		context.addBrain(13, new JDinkMouseBrain());
		context.addBrain(14, new JDinkButtonBrain());
		context.addBrain(15, new JDinkUnimplementedBrain(15, "shadow"));
		context.addBrain(16, new JDinkPeopleBrain());
		context.addBrain(17, new JDinkUnimplementedBrain(17, "missle_brain_expire"));

		// ini functions
//		context.addFunction("SET_SPRITE_INFO", new JDinkSetSpriteInfoFunction());
		context.addFunction("set_sprite_info", new JDinkSetSpriteInfoFunction());
		context.addFunction("load_sequence_now", new JDinkLoadSequenceNowFunction());
		context.addFunction("load_sequence", new JDinkLoadSequenceFunction());
		context.addFunction("set_frame_delay", new JDinkSetFrameDelayFunction());
		context.addFunction("set_frame_frame", new JDinkSetFrameFrameFunction());
		context.addFunction("set_frame_special", new JDinkSetFrameSpecialFunction());

		// implemented
		context.addFunction("create_sprite", new JDinkCreateSpriteFunction());
		context.addFunction("editor_frame", new JDinkEditorFrameFunction());
		context.addFunction("editor_seq", new JDinkEditorSeqFunction());
		context.addFunction("editor_type", new JDinkEditorTypeFunction());
		context.addFunction("debug", new JDinkDebugFunction());
		context.addFunction("external", new JDinkExternalFunction());
		context.addFunction("make_global_int", new JDinkMakeGlobalIntFunction());
		context.addFunction("move", new JDinkMoveFunction());
		context.addFunction("move_stop", new JDinkMoveStopFunction());
		context.addFunction("get_version", new JDinkGetVersionFunction());
		context.addFunction("kill_this_task", new JDinkKillThisTaskFunction());
		context.addFunction("init", new JDinkInitFunction());
		context.addFunction("inside_box", new JDinkInsideBoxFunction());
		context.addFunction("load_sound", new JDinkLoadSoundFunction());
		context.addFunction("load_game", new JDinkLoadGameFunction());
		context.addFunction("playsound", new JDinkPlaySoundFunction());
		context.addFunction("preload_seq", new JDinkPreloadSeqFunction());
		context.addFunction("random", new JDinkRandomFunction());
		context.addFunction("save_game", new JDinkSaveGameFunction());
		context.addFunction("say", new JDinkSayFunction());
		context.addFunction("say_stop", new JDinkSayStopFunction());
		context.addFunction("say_stop_xy", new JDinkSayStopXYFunction());
		context.addFunction("say_xy", new JDinkSayXYFunction());
		context.addFunction("screenlock", new JDinkScreenLockFunction());
		context.addFunction("sp", new JDinkSpFunction());
		context.addFunction("sp_active", new JDinkSpActiveFunction());
		context.addFunction("sp_base_attack", new JDinkSpBaseAttackFunction());
		context.addFunction("sp_base_death", new JDinkSpBaseDeathFunction());
		context.addFunction("sp_base_walk", new JDinkSpBaseWalkFunction());
		context.addFunction("sp_brain", new JDinkSpBrainFunction());
		context.addFunction("sp_brain_parm", new JDinkSpBrainParamFunction());
		context.addFunction("sp_brain_parm2", new JDinkSpBrainParam2Function());
		context.addFunction("sp_editor_num", new JDinkSpEditorNumFunction());
		context.addFunction("sp_frame", new JDinkSpFrameFunction());
		context.addFunction("sp_frame_delay", new JDinkSpFrameDelayFunction());
		context.addFunction("sp_hard", new JDinkSpHardFunction());
		context.addFunction("sp_kill_wait", new JDinkSpKillWaitFunction());
		context.addFunction("sp_nocontrol", new JDinkSpNoControlFunction());
		context.addFunction("sp_nohit", new JDinkSpNoHitFunction());
		context.addFunction("sp_notouch", new JDinkSpNoTouchFunction());
		context.addFunction("sp_pseq", new JDinkSpPSeqFunction());
		context.addFunction("sp_pframe", new JDinkSpPFrameFunction());
		context.addFunction("sp_que", new JDinkSpQueFunction());
		context.addFunction("sp_reverse", new JDinkSpReverseFunction());
		context.addFunction("sp_script", new JDinkSpScriptFunction());
		context.addFunction("sp_touch_damage", new JDinkSpTouchDamageFunction());
		context.addFunction("sp_x", new JDinkSpXFunction());
		context.addFunction("sp_y", new JDinkSpYFunction());
		context.addFunction("wait", new JDinkWaitFunction());

		// review
		context.addFunction("sp_seq", new JDinkSpSeqFunction());
		context.addFunction("sp_noclip", new JDinkSpNoclipFunction());
		context.addFunction("sp_speed", new JDinkSpSpeedFunction());
		context.addFunction("sp_dir", new JDinkSpDirFunction());
		context.addFunction("sp_timing", new JDinkSpTimingFunction());
		context.addFunction("sp_distance", new JDinkSpDistanceFunction());
		context.addFunction("sp_defense", new JDinkSpDefenseFunction());
		context.addFunction("sp_strength", new JDinkSpStrengthFunction());
		context.addFunction("sp_hitpoints", new JDinkSpHitPointsFunction());
		context.addFunction("sp_exp", new JDinkSpExpFunction());
		context.addFunction("sp_target", new JDinkSpTargetFunction());
		context.addFunction("kill_game", new JDinkKillGameFunction());
		context.addFunction("set_mode", new JDinkSetModeFunction());
		context.addFunction("set_dink_base_push", new JDinkSetDinkBasePushFunction());
		context.addFunction("add_item", new JDinkAddItemFunction());
		context.addFunction("push_active", new JDinkPushActiveFunction());
		context.addFunction("freeze", new JDinkFreezeFunction());
		context.addFunction("unfreeze", new JDinkUnfreezeFunction());

		// unimplemented functions
		context.addFunction("fill_screen", new JDinkFillScreenFunction());
		context.addFunction("sp_sound", new JDinkSpSoundFunction());
		context.addFunction("playmidi", new JDinkPlayMidiFunction());
		context.addFunction("stopcd", new JDinkStopCdFunction());
		context.addFunction("stopmidi", new JDinkStopMidiFunction());
		context.addFunction("choice_start", new JDinkChoiceStartFunction());
		context.addFunction("game_exist", new JDinkGameExistFunction());
		context.addFunction("reset_timer", new JDinkResetTimerFunction());
		context.addFunction("arm_weapon", new JDinkArmWeaponFunction());
		context.addFunction("draw_status", new JDinkDrawStatusFunction());
		context.addFunction("breakpoint", new JDinkBreakpointFunction());

		context.addFunction("set_dink_speed", new JDinkSetDinkSpeedFunction());
		context.addFunction("sp_range", new JDinkUnimplementedFunction("sp_range"));
		context.addFunction("sp_attack_hit_sound", new JDinkUnimplementedFunction("sp_attack_hit_sound"));

		// do we need that hardness attribute at all? it seems to be used for optimisation that we don't need
//		context.addFunction("sp_hard", new JDinkUnimplementedFunction("sp_hard"));
		context.addFunction("draw_hard_sprite", new JDinkUnimplementedFunction("draw_hard_sprite"));

		iniLoader.setContext(context);
		iniLoader.load(getFile(fileDmod, "dink.ini"));

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

		log.debug("done");
	}

}
