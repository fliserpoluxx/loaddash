package net.golema.loadash.queue;

public enum QueuePriority {

	ADMIN("Admin", 5), STAFF("Staff", 4), HIGHEST("Très Elevée", 3), HIGH("Elevée", 2), LOW("Faible", 1);

	private String name;
	private int priorityInt;

	/**
	 * Construire une Priorité de Queue.
	 * 
	 * @param name
	 * @param priorityInt
	 */
	private QueuePriority(String name, int priorityInt) {
		this.name = name;
		this.priorityInt = priorityInt;
	}

	public String getName() {
		return name;
	}

	public int getPriorityInt() {
		return priorityInt;
	}

	/**
	 * Récupérer une Prioritée de Queue.
	 * 
	 * @param power
	 * @return
	 */
	public static QueuePriority getPriorityByPower(int power) {
		for (QueuePriority queuePriority : QueuePriority.values())
			if (queuePriority.getPriorityInt() == power)
				return queuePriority;
		return QueuePriority.LOW;
	}
}