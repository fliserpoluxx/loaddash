package net.golema.loadash.queue;

public enum QueueLeaveReason {

	VOLUNTARY(1, "Volontaire"), TELEPORTATION(2, "Téléportation de Serveur"), KICK(3, "Exclusion"), UNKNOW(-1, "");

	private int id;
	private String reason;

	/**
	 * Construire une énumération de Leave de Queue.
	 * 
	 * @param id
	 * @param reason
	 */
	private QueueLeaveReason(int id, String reason) {
		this.id = id;
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public String getReason() {
		return reason;
	}
}