package com.pege.alpha.graphics.ui;

import java.awt.Graphics;

public class UIStatusBar extends UIComponent {
	
	private int health;

	public UIStatusBar(int x, int y, String path) {
		super(x, y, path);
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
