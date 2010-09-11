package de.siteof.jdink.model.view;

import java.util.List;
import java.util.Map;

import de.siteof.jdink.geom.JDinkRectangle;
import de.siteof.jdink.model.JDinkTile;

public final class JDinkDisplayInformation {
	
	private final int backgroundColorIndex;
	private final JDinkTile[] tiles;
	private final JDinkRectangle tilesBounds;
	private final List<JDinkSpriteDisplayInformation> spriteInformationList;
	private final Map<Integer, JDinkSpriteLayerView> spriteLayerMap;
	
	public JDinkDisplayInformation(
			int backgroundColorIndex,
			JDinkTile[] tiles,
			JDinkRectangle tilesBounds,
			List<JDinkSpriteDisplayInformation> spriteInformationList,
			Map<Integer, JDinkSpriteLayerView> spriteLayerMap) {
		this.backgroundColorIndex = backgroundColorIndex;
		this.tiles = tiles;
		this.tilesBounds = tilesBounds;
		this.spriteInformationList = spriteInformationList;
		this.spriteLayerMap = spriteLayerMap;
	}

	public JDinkRectangle getTilesBounds() {
		return tilesBounds;
	}

	public List<JDinkSpriteDisplayInformation> getSpriteInformationList() {
		return spriteInformationList;
	}

	public Map<Integer, JDinkSpriteLayerView> getSpriteLayerMap() {
		return spriteLayerMap;
	}

	public JDinkTile[] getTiles() {
		return tiles;
	}

	public int getBackgroundColorIndex() {
		return backgroundColorIndex;
	}

}
