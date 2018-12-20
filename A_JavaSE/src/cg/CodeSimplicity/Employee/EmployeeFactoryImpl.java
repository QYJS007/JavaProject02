package cg.CodeSimplicity.Employee;

public class EmployeeFactoryImpl implements EmployeeFactory{

	private static final int COMMISSIONED = 0;
	private static final int HOURLY = 1;
	private static final int SALARIED =2;

	public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {
		switch (r.type) {
		case COMMISSIONED:
			return new CommissionedEmployee(r) ;
		case HOURLY:
			return new HourlyEmployee(r);
		case SALARIED:
			return new SalariedEmploye(r);
		default:
			throw new InvalidEmployeeType(r.type);
		}
	}
}
