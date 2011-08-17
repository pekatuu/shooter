package jp.pekatuu.android.zaqzaq;

import jp.pekatuu.android.zaqzaq.actor.Enemy;
import jp.pekatuu.android.zaqzaq.actor.Player;
import jp.pekatuu.android.zaqzaq.actor.Shots;

public class GameData {
	public static float height, width;
	public static Player player = new Player();
	public static Enemy enemy = new Enemy();
	public static Shots shots = new Shots(32);

	public static void updateScreenSize(int w, int h) {
		height = h;
		width = w;
	} ;


}
