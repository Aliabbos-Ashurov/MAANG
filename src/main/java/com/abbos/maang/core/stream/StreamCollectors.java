package com.abbos.maang.core.stream;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-04-01
 */
public class StreamCollectors {
    public static void main(String[] args) {
        // Employees
        List<Employee> employees2 = List.of(
                new Employee("Alice Borderland", 5000),
                new Employee("Bob Mack", 7000),
                new Employee("Charlie Diamond", 6000)
        );

        List<Employee> collect = employees2.stream()
                .collect(new ConcurrentStreamCollector<>());  // custom collector
        collect.forEach(System.out::println);
    }

    private static final class Employee {
        String name;
        double salary;

        public Employee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return String.format("Employee: name(%s) salary(%s)", this.name, this.salary);
        }
    }
}
