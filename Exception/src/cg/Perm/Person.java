package cg.Perm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Person {

	private String name = new String("asasa");
	private String  school =  new String("asasa");
	private String age ;
	private List sex= new ArrayList<Object>();
	private Map year;
	private String gender;
	private String fox;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public List getSex() {
		return sex;
	}
	public void setSex(List sex) {
		this.sex = sex;
	}
	public Map getYear() {
		return year;
	}
	public void setYear(Map year) {
		this.year = year;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFox() {
		return fox;
	}
	public void setFox(String fox) {
		this.fox = fox;
	}
	
	
	
}
