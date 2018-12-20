package cg.CodeSimplicity.Employee;

public interface EmployeeFactory {

	public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;
}
