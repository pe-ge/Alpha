package com.pege.alpha.graphics.spritesheets;

import com.pege.alpha.graphics.Sprite;
import com.pege.alpha.graphics.SpriteSheet;

public class GokuSprites {
	public static Sprite[] standingUp = new Sprite[]{
		new Sprite(32, 4, 0, SpriteSheet.goku, 15, 7, 15),
	};
	
	public static Sprite[] walkingUp = new Sprite[]{
		new Sprite(32, 0, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 1, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 2, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 3, 2, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] runningUp = new Sprite[]{
		new Sprite(32, 4, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 5, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 6, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 7, 3, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] standingDown = new Sprite[]{
		new Sprite(32, 0, 0, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 1, 0, SpriteSheet.goku, 15, 7, 15),
	};
	
	public static Sprite[] walkingDown = new Sprite[]{
		new Sprite(32, 0, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 1, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 2, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 3, 1, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] runningDown = new Sprite[]{
		new Sprite(32, 4, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 5, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 6, 2, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 7, 2, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] standingRight = new Sprite[]{
		new Sprite(32, 2, 0, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 3, 0, SpriteSheet.goku, 15, 7, 15),
	};
	
	public static Sprite[] walkingRight = new Sprite[]{
		new Sprite(32, 4, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 5, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 6, 1, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 7, 1, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] runningRight = new Sprite[]{
		new Sprite(32, 0, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 1, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 2, 3, SpriteSheet.goku, 15, 7, 15),
		new Sprite(32, 3, 3, SpriteSheet.goku, 15, 7, 15)
	};
	
	public static Sprite[] standingLeft = new Sprite[]{
		standingRight[0].rotateHorizontally(),
		standingRight[1].rotateHorizontally()
	};
	
	public static Sprite[] walkingLeft = new Sprite[]{
		walkingRight[0].rotateHorizontally(),
		walkingRight[1].rotateHorizontally(),
		walkingRight[2].rotateHorizontally(),
		walkingRight[3].rotateHorizontally()
	};
	
	public static Sprite[] runningLeft = new Sprite[]{
		runningRight[0].rotateHorizontally(),
		runningRight[1].rotateHorizontally(),
		runningRight[2].rotateHorizontally(),
		runningRight[3].rotateHorizontally()
	};
}
