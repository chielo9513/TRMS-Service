package org.atouma.beans;

import java.time.ZonedDateTime;


public class Request {
	
	private int requestId;
	private int employeeId;
	private int assignedBenCoId;

	private String eventLocation;
	private String eventDescription;
	private String eventTimeZone;
	private ZonedDateTime eventDateTime;
	private float eventCost;
	private GradingFormat eventGradingFormat;
	private String reason;
	private Status status;
	private EventType type;
	private boolean passProof;
	
	private String applicationTimeZone;
	private ZonedDateTime applicationDateTime;
	
	private boolean isUrgent;
	private boolean superApproved;
	private boolean deptApproved;
	private boolean benCoApproved;
	
	private float projectedAward;
	private float awardValue;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getAssignedBenCoId() {
		return assignedBenCoId;
	}
	public void setAssignedBenCoId(int assignedBenCoId) {
		this.assignedBenCoId = assignedBenCoId;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventTimeZone() {
		return eventTimeZone;
	}
	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}
	public ZonedDateTime getEventDateTime() {
		return eventDateTime;
	}
	public void setEventDateTime(ZonedDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}
	public float getEventCost() {
		return eventCost;
	}
	public void setEventCost(float eventCost) {
		this.eventCost = eventCost;
	}
	public GradingFormat getEventGradingFormat() {
		return eventGradingFormat;
	}
	public void setEventGradingFormat(GradingFormat eventGradingFormat) {
		this.eventGradingFormat = eventGradingFormat;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public boolean isPassProof() {
		return passProof;
	}
	public void setPassProof(boolean passProof) {
		this.passProof = passProof;
	}
	public String getApplicationTimeZone() {
		return applicationTimeZone;
	}
	public void setApplicationTimeZone(String applicationTimeZone) {
		this.applicationTimeZone = applicationTimeZone;
	}
	public ZonedDateTime getApplicationDateTime() {
		return applicationDateTime;
	}
	public void setApplicationDateTime(ZonedDateTime applicationDateTime) {
		this.applicationDateTime = applicationDateTime;
	}
	public boolean isUrgent() {
		return isUrgent;
	}
	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}
	public boolean isSuperApproved() {
		return superApproved;
	}
	public void setSuperApproved(boolean superApproved) {
		this.superApproved = superApproved;
	}
	public boolean isDeptApproved() {
		return deptApproved;
	}
	public void setDeptApproved(boolean deptApproved) {
		this.deptApproved = deptApproved;
	}
	public boolean isBenCoApproved() {
		return benCoApproved;
	}
	public void setBenCoApproved(boolean benCoApproved) {
		this.benCoApproved = benCoApproved;
	}
	public float getProjectedAward() {
		return this.type.getPercent()*this.eventCost;
	}
	public void setProjectedAward(float projectedAward) {
		this.projectedAward = this.type.getPercent()*this.eventCost;
	}
	public float getAwardValue() {
		return awardValue;
	}
	public void setAwardValue(float awardValue) {
		this.awardValue = awardValue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationDateTime == null) ? 0 : applicationDateTime.hashCode());
		result = prime * result + ((applicationTimeZone == null) ? 0 : applicationTimeZone.hashCode());
		result = prime * result + assignedBenCoId;
		result = prime * result + Float.floatToIntBits(awardValue);
		result = prime * result + (benCoApproved ? 1231 : 1237);
		result = prime * result + (deptApproved ? 1231 : 1237);
		result = prime * result + employeeId;
		result = prime * result + Float.floatToIntBits(eventCost);
		result = prime * result + ((eventDateTime == null) ? 0 : eventDateTime.hashCode());
		result = prime * result + ((eventDescription == null) ? 0 : eventDescription.hashCode());
		result = prime * result + ((eventGradingFormat == null) ? 0 : eventGradingFormat.hashCode());
		result = prime * result + ((eventLocation == null) ? 0 : eventLocation.hashCode());
		result = prime * result + ((eventTimeZone == null) ? 0 : eventTimeZone.hashCode());
		result = prime * result + (isUrgent ? 1231 : 1237);
		result = prime * result + (passProof ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(projectedAward);
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + requestId;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (superApproved ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (applicationDateTime == null) {
			if (other.applicationDateTime != null)
				return false;
		} else if (!applicationDateTime.equals(other.applicationDateTime))
			return false;
		if (applicationTimeZone == null) {
			if (other.applicationTimeZone != null)
				return false;
		} else if (!applicationTimeZone.equals(other.applicationTimeZone))
			return false;
		if (assignedBenCoId != other.assignedBenCoId)
			return false;
		if (Float.floatToIntBits(awardValue) != Float.floatToIntBits(other.awardValue))
			return false;
		if (benCoApproved != other.benCoApproved)
			return false;
		if (deptApproved != other.deptApproved)
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (Float.floatToIntBits(eventCost) != Float.floatToIntBits(other.eventCost))
			return false;
		if (eventDateTime == null) {
			if (other.eventDateTime != null)
				return false;
		} else if (!eventDateTime.equals(other.eventDateTime))
			return false;
		if (eventDescription == null) {
			if (other.eventDescription != null)
				return false;
		} else if (!eventDescription.equals(other.eventDescription))
			return false;
		if (eventGradingFormat != other.eventGradingFormat)
			return false;
		if (eventLocation == null) {
			if (other.eventLocation != null)
				return false;
		} else if (!eventLocation.equals(other.eventLocation))
			return false;
		if (eventTimeZone == null) {
			if (other.eventTimeZone != null)
				return false;
		} else if (!eventTimeZone.equals(other.eventTimeZone))
			return false;
		if (isUrgent != other.isUrgent)
			return false;
		if (passProof != other.passProof)
			return false;
		if (Float.floatToIntBits(projectedAward) != Float.floatToIntBits(other.projectedAward))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (requestId != other.requestId)
			return false;
		if (status != other.status)
			return false;
		if (superApproved != other.superApproved)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", employeeId=" + employeeId + ", assignedBenCoId=" + assignedBenCoId
				+ ", eventLocation=" + eventLocation + ", eventDescription=" + eventDescription + ", eventTimeZone="
				+ eventTimeZone + ", eventDateTime=" + eventDateTime + ", eventCost=" + eventCost
				+ ", eventGradingFormat=" + eventGradingFormat + ", reason=" + reason + ", status=" + status + ", type="
				+ type + ", passProof=" + passProof + ", applicationTimeZone=" + applicationTimeZone
				+ ", applicationDateTime=" + applicationDateTime + ", isUrgent=" + isUrgent + ", superApproved="
				+ superApproved + ", deptApproved=" + deptApproved + ", benCoApproved=" + benCoApproved
				+ ", projectedAward=" + projectedAward + ", awardValue=" + awardValue + "]";
	}
	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}