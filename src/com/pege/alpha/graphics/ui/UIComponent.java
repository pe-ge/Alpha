package com.pege.alpha.graphics.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIComponent {
	
	protected int x, y;
	protected BufferedImage image;
	
	public UIComponent(int x, int y, String path) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(UIComponent.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
}
