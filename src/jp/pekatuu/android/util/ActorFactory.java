package jp.pekatuu.android.util;


public abstract class ActorFactory<T extends Actor> {
//	protected final ArrayList<T> actors;
	protected final T[] actors;
	protected final int capacity;
	protected int lastIndex;
	
	public ActorFactory(int capacity){
		this.capacity = capacity;
		actors = initActors(capacity);
		lastIndex = 0;
	}
	protected abstract T[] initActors(int capacity);
	
	public T[] getAllActors(){
		return actors;
	}
	
	public T newActor(){
		for (int i = 0; i < capacity; i++ ){
			final int index = (i + lastIndex) % capacity;
			T candidate = actors[index];
			if (!candidate.isAlive){
				lastIndex = (index + 1) % capacity;
				candidate.isAlive = true;
				return candidate;
			}
		}
		return null;
	}
}
