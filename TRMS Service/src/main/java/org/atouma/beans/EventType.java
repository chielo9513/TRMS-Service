package org.atouma.beans;

public enum EventType {
	UNICOURSE(0.8f), SEMINAR(0.6f), 
	CERT_PREP_CLASS(.75f), CERTIFICATION(1f), 
	TECHNICAL_TRAINING(.9f), OTHER(.3f);

	private float percent;

	EventType(float percent) {
		this.percent = percent;
	}

	public float getPercent() {
		return percent;

	}
}
