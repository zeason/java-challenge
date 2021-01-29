package jp.co.axa.apidemo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {
	@Autowired
	EmployeeService employeeService;

	Employee employee;

	List<Employee> list;

	@Before
	public void setup() throws Exception {
		this.employee = new Employee();
		employee.setDepartment("Finance");
		employee.setId(1L);
		employee.setName("John");
		employee.setSalary(10000);
		list = new ArrayList<>();
		list.add(employee);
		employeeService.saveEmployee(employee);
	}

	// Test EmployeeService.retrieveEmployees()
	@Test
	public void getEmployees() throws Exception {
		List<Employee> employees = employeeService.retrieveEmployees();

		assertThat(employees).isNotNull().isNotEmpty().anyMatch(el -> el.getName().equals("John"));
	}

	// Test EmployeeService.getEmployee(1)
	@Test
	public void getEmployee() throws Exception {
		// test normal case
		Employee e = employeeService.getEmployee(1L);

		assertThat(e).isNotNull();

		// test error case
		try {
			e = employeeService.getEmployee(100L);
		} catch (Exception ex) {
			assertThat(ex).isNotNull();
		}
	}

	// Test EmployeeService.saveEmployee(employee)
	@Test
	public void saveEmployee() throws Exception {
		Employee e = new Employee();
		e.setDepartment("Finance");
		e.setName("Jim");
		e.setSalary(10000);
		employeeService.saveEmployee(e);

		List<Employee> employees = employeeService.retrieveEmployees();
		assertThat(employees).isNotNull().isNotEmpty().anyMatch(el -> el.getName().equals("Jim"));
	}

	// Test EmployeeService.updateEmployee(employee)
	@Test
	public void updateEmployee() throws Exception {
		Employee e = new Employee();
		e.setDepartment("Finance");
		e.setId(1L);
		e.setName("Kate");
		e.setSalary(10000);
		employeeService.updateEmployee(e);
		List<Employee> employees = employeeService.retrieveEmployees();
		assertThat(employees).isNotNull().isNotEmpty().anyMatch(el -> el.getName().equals("Kate"));
	}

	// Test EmployeeService.deleteEmployee(1)
	@Test
	public void deleteEmployee() throws Exception {
		employeeService.deleteEmployee(1L);
		List<Employee> employees = employeeService.retrieveEmployees();
		assertThat(employees).isNotNull().noneMatch(el -> el.getName().equals("John"));
	}
}
