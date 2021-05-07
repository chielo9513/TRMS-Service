package org.atouma.beans;

public class Employee {

		private Integer employeeId;
		private String employeeEmail;
		private String name;
		private Integer supervisor;
		private Integer deptHead;
		private Role role;
		private Float totalReimbursements;
		private Float pendingReimbursements;
		private Float awardedReimbursements;
		private Float availableReimbursements;
		
		
		
		public Employee() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Integer getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(Integer employeeId) {
			this.employeeId = employeeId;
		}
		public String getEmployeeEmail() {
			return employeeEmail;
		}
		public void setEmployeeEmail(String employeeEmail) {
			this.employeeEmail = employeeEmail;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getSupervisor() {
			return supervisor;
		}
		public void setSupervisor(Integer supervisor) {
			this.supervisor = supervisor;
		}
		public Integer getDeptHead() {
			return deptHead;
		}
		public void setDeptHead(Integer deptHead) {
			this.deptHead = deptHead;
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}
		public Float getTotalReimbursements() {
			return totalReimbursements;
		}
		public void setTotalReimbursements(Float totalReimbursements) {
			this.totalReimbursements = totalReimbursements;
		}
		public Float getPendingReimbursements() {
			return pendingReimbursements;
		}
		public void setPendingReimbursements(Float pendingReimbursements) {
			this.pendingReimbursements = pendingReimbursements;
		}
		public Float getAwardedReimbursements() {
			return awardedReimbursements;
		}
		public void setAwardedReimbursements(Float awardedReimbursements) {
			this.awardedReimbursements = awardedReimbursements;
		}
		public Float getAvailableReimbursements() {
			return availableReimbursements;
		}
		public void setAvailableReimbursements(Float availableReimbursements) {
			this.availableReimbursements = availableReimbursements;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((availableReimbursements == null) ? 0 : availableReimbursements.hashCode());
			result = prime * result + ((awardedReimbursements == null) ? 0 : awardedReimbursements.hashCode());
			result = prime * result + ((deptHead == null) ? 0 : deptHead.hashCode());
			result = prime * result + ((employeeEmail == null) ? 0 : employeeEmail.hashCode());
			result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((pendingReimbursements == null) ? 0 : pendingReimbursements.hashCode());
			result = prime * result + ((role == null) ? 0 : role.hashCode());
			result = prime * result + ((supervisor == null) ? 0 : supervisor.hashCode());
			result = prime * result + ((totalReimbursements == null) ? 0 : totalReimbursements.hashCode());
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
			Employee other = (Employee) obj;
			if (availableReimbursements == null) {
				if (other.availableReimbursements != null)
					return false;
			} else if (!availableReimbursements.equals(other.availableReimbursements))
				return false;
			if (awardedReimbursements == null) {
				if (other.awardedReimbursements != null)
					return false;
			} else if (!awardedReimbursements.equals(other.awardedReimbursements))
				return false;
			if (deptHead == null) {
				if (other.deptHead != null)
					return false;
			} else if (!deptHead.equals(other.deptHead))
				return false;
			if (employeeEmail == null) {
				if (other.employeeEmail != null)
					return false;
			} else if (!employeeEmail.equals(other.employeeEmail))
				return false;
			if (employeeId == null) {
				if (other.employeeId != null)
					return false;
			} else if (!employeeId.equals(other.employeeId))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (pendingReimbursements == null) {
				if (other.pendingReimbursements != null)
					return false;
			} else if (!pendingReimbursements.equals(other.pendingReimbursements))
				return false;
			if (role != other.role)
				return false;
			if (supervisor == null) {
				if (other.supervisor != null)
					return false;
			} else if (!supervisor.equals(other.supervisor))
				return false;
			if (totalReimbursements == null) {
				if (other.totalReimbursements != null)
					return false;
			} else if (!totalReimbursements.equals(other.totalReimbursements))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Employee [employeeId=" + employeeId + ", employeeEmail=" + employeeEmail + ", name=" + name
					+ ", supervisor=" + supervisor + ", deptHead=" + deptHead + ", role=" + role
					+ ", totalReimbursements=" + totalReimbursements + ", pendingReimbursements="
					+ pendingReimbursements + ", awardedReimbursements=" + awardedReimbursements
					+ ", availableReimbursements=" + availableReimbursements + "]";
		}
}
		
		