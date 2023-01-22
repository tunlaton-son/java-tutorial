package com.axonstech.training.service;

import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.dto.request.NewEmployeeRequest;
import com.axonstech.training.entity.Company;
import com.axonstech.training.entity.Employee;
import com.axonstech.training.repository.CompanyRepository;
import com.axonstech.training.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock private EmployeeRepository employeeRepository;
    @Mock private CompanyRepository companyRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository, companyRepository);
    }

    @Test
    void canGetAllEmployees() {
        // arrange
        Page<Employee> pagedResponse = new PageImpl(Arrays.asList(new Employee()));
        Mockito.when(employeeRepository.findAll(PageRequest.of(0, 10))).thenReturn(pagedResponse);
        // act
        employeeService.getEmployees(null, 1, 10);
        // assert
        verify(employeeRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void canGetAllActiveEmployees() {
        // arrange
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Employee> pagedResponse = new PageImpl(Arrays.asList(new Employee()));
        Mockito.when(employeeRepository.findByActive(true, pageRequest)).thenReturn(pagedResponse);
        // act
        employeeService.getEmployees(true, 1, 10);
        // assert
        verify(employeeRepository).findByActive(true, pageRequest);
    }

    @Test
    void canGetEmployee() {
        // arrange
        Long id = 99L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setCompany(new Company());
        Mockito.when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // act
        EmployeeDto dto = employeeService.getEmployee(id);

        // assert
        assertThat(dto).isInstanceOf(EmployeeDto.class);
    }

    @Test
    void canSave() throws Exception {
        // arrange
        String username = "sompong.pos";
        NewEmployeeRequest employeeRequest = new NewEmployeeRequest();
        employeeRequest.setUsername(username);
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setCompany(new Company());
        Mockito.when(employeeRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.empty());
        // act
        employeeService.save(employeeRequest);
        // assert
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedStudent = employeeArgumentCaptor.getValue();
        assertThat(capturedStudent.getUsername()).isEqualTo(username);
    }

    @Test
    void willThrowWhenSaveEmployeeExist() throws Exception {
        // arrange
        String username = "sompong.pos";
        NewEmployeeRequest employeeRequest = new NewEmployeeRequest();
        employeeRequest.setUsername(username);
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setCompany(new Company());
        Mockito.when(employeeRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(employee));
        // act
        // assert
        assertThatThrownBy(() -> employeeService.save(employeeRequest))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("username is already taken");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void update() throws Exception {
        //arrange
        EmployeeDto employeeDto = new EmployeeDto();
        Employee employee = new Employee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        //act
        employeeService.update(employeeDto);
        //assert
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
    }

    @Test
    void willThrowWhenUpdateEmployeeNotFound() throws Exception {
        //arrange
        EmployeeDto employeeDto = new EmployeeDto();
        Employee employee = new Employee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        //act
        //assert
        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Id not found");
    }

    @Test
    void canDelete() throws Exception {
        // given
        long id = 99;
        given(employeeRepository.existsById(id))
                .willReturn(true);
        // when
        employeeService.delete(id);

        // then
        verify(employeeRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteEmpNotFound() throws Exception {
        // given
        long id = 99;
        given(employeeRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> employeeService.delete(id))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Id not found");
        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void canAddToCompany() throws Exception {
        // given
        long userId = 99L;
        String companyCode = "001";
        Employee employee = new Employee();
        Company company = new Company();

        Mockito.when(employeeRepository.findById(userId)).thenReturn(Optional.of(employee));
        Mockito.when(companyRepository.findById(companyCode)).thenReturn(Optional.of(company));
        // when
        employeeService.addToCompany(userId, companyCode);
        // then
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
    }

    @Test
    void willThrowWhenAddToCompanyCuzEmployeeNotFound() throws Exception {
        // given
        long userId = 99L;
        String companyCode = "001";

        Mockito.when(employeeRepository.findById(userId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> employeeService.addToCompany(userId, companyCode))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("not found user");
    }

    @Test
    void willThrowWhenAddToCompanyCuzCompanyNotFound() throws Exception {
        // given
        long userId = 99L;
        String companyCode = "001";
        Employee employee = new Employee();

        Mockito.when(employeeRepository.findById(userId)).thenReturn(Optional.of(employee));
        Mockito.when(companyRepository.findById(companyCode)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> employeeService.addToCompany(userId, companyCode))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("not found company");
    }
}