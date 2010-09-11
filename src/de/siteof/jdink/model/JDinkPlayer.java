package de.siteof.jdink.model;

import de.siteof.jdink.script.JDinkScope;

public class JDinkPlayer {

	private final int spriteNumber;

	private int basePush;
	private JDinkScope scope;

	private final JDinkItemStore itemStore = new JDinkItemStore();
	private final JDinkItemStore magicItemStore = new JDinkItemStore();

	public JDinkPlayer(int spriteNumber) {
		this.spriteNumber = spriteNumber;
	}

	public JDinkItem allocateItem() {
		return itemStore.allocateItem();
	}

	public JDinkItem getItem(int itemNumber) {
		return itemStore.getItem(itemNumber);
	}

	public JDinkItem setItem(JDinkItem item) {
		return this.setItem(item.getItemNumber(), item);
	}

	public JDinkItem setItem(int itemNumber, JDinkItem item) {
		return itemStore.setItem(itemNumber, item);
	}

	public JDinkItem allocateMagicItem() {
		return magicItemStore.allocateItem();
	}

	public JDinkItem getMagicItem(int itemNumber) {
		return magicItemStore.getItem(itemNumber);
	}

	public JDinkItem seMagictItem(JDinkItem item) {
		return this.setMagicItem(item.getItemNumber(), item);
	}

	public JDinkItem setMagicItem(int itemNumber, JDinkItem item) {
		return magicItemStore.setItem(itemNumber, item);
	}

	// plain getter/setter

	public int getBasePush() {
		return basePush;
	}

	public void setBasePush(int basePush) {
		this.basePush = basePush;
	}

	public JDinkScope getScope() {
		return scope;
	}

	public void setScope(JDinkScope scope) {
		this.scope = scope;
	}

	public int getSpriteNumber() {
		return spriteNumber;
	}

	public JDinkItemStore getItemStore() {
		return itemStore;
	}

	public JDinkItemStore getMagicItemStore() {
		return magicItemStore;
	}

}
