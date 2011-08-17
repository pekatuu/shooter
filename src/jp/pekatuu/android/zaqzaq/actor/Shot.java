package jp.pekatuu.android.zaqzaq.actor;

import jp.pekatuu.android.util.Actor;
import jp.pekatuu.android.util.geometory.Vector3D;

public class Shot extends Actor{
	@Override
	public void move(){
		Vector3D.addScaled(p, vec, vel);
	}
}
