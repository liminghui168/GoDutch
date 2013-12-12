package com.liminghui.godutch.control;

public class SlideMenuItem {
	private int itemId;
	private String title;

	public SlideMenuItem() {

	}

	public SlideMenuItem(int itemId, String title) {
		super();
		this.itemId = itemId;
		this.title = title;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
