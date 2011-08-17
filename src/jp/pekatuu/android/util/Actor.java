package jp.pekatuu.android.util;

import jp.pekatuu.android.util.geometory.Vector3D;

public abstract class Actor {
	public final Vector3D p;
	public final Vector3D vec;
	public float vel;
	public boolean isAlive;

	public Actor(){
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Actor(Vector3D p){
		this(p.x, p.y, p.z);
	}
	
	public Actor(float x, float y, float z){
		this.p = new Vector3D(x, y, z);
		this.vec = new Vector3D();
		isAlive = false;
	}
	
	public void init(Vector3D p, Vector3D vec, float vel){
		this.p.set(p);
		this.vec.set(vec);
		this.vel = vel;
		isAlive = true;
	}
	
	public abstract void move();
	public void destroy(){
		isAlive = false;
	}
}
