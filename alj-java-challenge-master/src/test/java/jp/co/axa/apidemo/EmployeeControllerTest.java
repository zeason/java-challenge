package jp.co.axa.apidemo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {
	MockMvc mockMvc;

	@Autowired
	EmployeeController employeeController;

	@MockBean
	EmployeeService employeeService;

	Employee employee;

	List<Employee> list;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.employeeController).build();
		this.employee = new Employee();
		employee.setDepartment("Finance");
		employee.setId(1L);
		employee.setName("John");
		employee.setSalary(10000);
		list = new ArrayList<>();
		list.add(employee);
	}

	// Test EmployeeController.getEmployees()
	@Test
	public void getEmployees() throws Exception {
		when(employeeService.retrieveEmployees()).thenReturn(list);

		mockMvc.perform(get("/api/v1/employees")).andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(1)));
	}

	// Test EmployeeController.getEmployee(1)
	@Test
	public void getEmployee() throws Exception {
		when(employeeService.getEmployee(1L)).thenReturn(employee);

		mockMvc.perform(get("/api/v1/employees/{id}", 1)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("John")));
	}

	// Test EmployeeController.saveEmployee(employee)
	@Test
	public void saveEmployee() throws Exception {
		Employee e = new Employee();
		e.setDepartment("Finance");
		e.setName("Jim");
		e.setSalary(10000);
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(e);
		mockMvc.perform(post("/api/v1/employees").contentType(APPLICATION_JSON_UTF8)
		        .content(requestJson)).andExpect(status().isOk());
	}

	// Test EmployeeController.deleteEmployee(1)
	@Test
	public void deleteEmployee() throws Exception {
		mockMvc.perform(delete("/api/v1/employees/{id}", 1)).andExpect(status().isOk());
	}

	// Test EmployeeController.updateEmployee(employee)
	@Test
	public void updateEmployee() throws Exception {
		Employee e = new Employee();
		e.setDepartment("Finance");
		e.setName("Jim");
		e.setSalary(10000);
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(e);
		mockMvc.perform(put("/api/v1/employees/{id}", 1).contentType(APPLICATION_JSON_UTF8)
		        .content(requestJson)).andExpect(status().isOk());
	}
}
