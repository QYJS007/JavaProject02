package cg.CodeSimplicity.Employee;

public abstract class Employee {

	public abstract boolean isPayday();
	public abstract Money calculatePay();
	public abstract void deliverPay(Money pay);
}
