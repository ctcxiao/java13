package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //以下所有的*都代表变量

    //1.查询名字是*的第一个employee
    @Query("select new com.example.employee.entity.Employee(em.name, em.age, em.gender, em.id, em.companyId, em.salary) from Employee as em where name=:name")
    Employee findEmployeeByName(@Param("name")String name);
    //2.找出Employee表中第一个姓名包含`*`字符并且薪资大于*的雇员个人信息
    @Query(value = "select name from Employee where name like %?1% limit 0,1", nativeQuery = true)
    String findEmployeeContainParam(@Param("n")String param);
    //3.找出一个薪资最高且公司ID是*的雇员以及该雇员的姓名
    @Query(value = "select name from Employee where id =?1  order by salary limit 0,1", nativeQuery = true)
    String findMaxSalaryEmplaoyee(@Param("id")int id);
    //4.实现对Employee的分页查询，每页两个数据
    @Query("select new com.example.employee.entity.Employee(em.name, em.age, em.gender, em.id, em.companyId, em.salary) from Employee as em")
    Page<Employee> findWithPage(Pageable pageable);
    //5.查找**的所在的公司的公司名称
    @Query("select companyName from Company where id = (select companyId from Employee where name=:name)")
    String findCompanyName(@Param("name") String employeeName);
    //6.将*的名字改成*,输出这次修改影响的行数
    @Modifying
    @Query("update Employee set name=:newName where name=:name")
    void updateNameToNew(@Param("name")String name, @Param("newName")String newName);
    @Query(value = "select row_count()", nativeQuery = true)
    int findAffectRows();
    //7.删除姓名是*的employee
    @Modifying
    @Query("delete from Employee where name=:name")
    void deleteEmployeeByName(@Param("name")String name);
}
