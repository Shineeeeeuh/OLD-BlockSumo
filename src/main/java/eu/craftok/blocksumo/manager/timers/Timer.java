package eu.craftok.blocksumo.manager.timers;

public abstract class Timer {
	
	private boolean finished, repeatable;
	private String id;
	
	public Timer(String id, boolean repeatable) {
		this.finished = false;
		this.repeatable = repeatable;
		this.id = id;
	}
	
	public abstract int getTime();
	
	public abstract void run();
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public boolean isRepeatable() {
		return repeatable;
	}
	
	public String getID() {
		return id;
	}
	
}
