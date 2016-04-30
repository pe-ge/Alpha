package com.pege.alpha.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;

public class UIStatusBar extends UIComponent {
	
	private int health;
	private final double MAX_HEALTH = 100.0;

	public UIStatusBar(int x, int y, String path) {
		super(x, y, path);
		health = 100;
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		g.setColor(Color.BLACK);
		for (int i = 0; i < 8; i++) {
			int healthBarLeftX = (int)(x + 99 + 128 * (health / MAX_HEALTH));
			int healthBarRightX = x + 227;
			int healthBarY = y + 8 + i;
			g.drawLine(healthBarLeftX, healthBarY, healthBarRightX, healthBarY);
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (health > 100) health = 100;
		if (health < 0) health = 0;
		
		this.health = health;
	}
}
