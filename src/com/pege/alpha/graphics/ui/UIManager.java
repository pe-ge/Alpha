package com.pege.alpha.graphics.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
	private List<UIComponent> userInterface;
	
	public UIManager() {
		userInterface = new ArrayList<UIComponent>();
		userInterface.add(new UIStatusBar(10, 10, "/ui/status_bar.png"));
	}
	
	public void render(Graphics g) {
		for (UIComponent uiComponent : userInterface) {
			uiComponent.render(g);
		}
	}
}
