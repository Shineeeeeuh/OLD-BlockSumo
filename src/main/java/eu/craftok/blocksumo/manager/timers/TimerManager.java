package eu.craftok.blocksumo.manager.timers;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TimerManager {
	private static HashMap<String, Timer> timers = new HashMap<>();
	private static HashMap<String, Integer> timerstime = new HashMap<>();
	
	public static void registerTimer(Timer t) {
		timers.put(t.getID(), t);
		timerstime.put(t.getID(), t.getTime());
	}
	
	public static List<Timer> getLaunchedTimer(){
		return timers.values().stream().filter(t -> !t.isFinished()).collect(Collectors.toList());
	}
	
	public static boolean isFinished(String id) {
		return timers.get(id).isFinished();
	}
	
	public static void updateTimers() {
		if(getLaunchedTimer().size() == 0) return;
		getLaunchedTimer().forEach(t -> {
			int i = getSecondsBeforeFinish(t.getID());
			if(i-1 == 0) {
				t.run();
				if(t.isRepeatable()) {
					timerstime.put(t.getID(), t.getTime());
				}
				return;
			}else {
				timerstime.put(t.getID(), i-1);
			}
		});
	}
	
	public static int getSecondsBeforeFinish(String id) {
		return timerstime.get(id);
	}
	
	public static String getTimeBeforeFinish(String id) {
		int time = timerstime.get(id);
		int minutes = time/60;
		int secondes = time-(minutes*60);
		return (minutes < 10 ? "0" : "")+minutes+":"+(secondes < 10 ? "0" : "")+secondes;
	}
	
}
