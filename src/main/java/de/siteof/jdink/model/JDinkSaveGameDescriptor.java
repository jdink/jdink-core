package de.siteof.jdink.model;

import java.io.Serializable;

public class JDinkSaveGameDescriptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private int version;
	private String gameInfo;
	private int minutes;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(String gameInfo) {
		this.gameInfo = gameInfo;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

}
