package com.abbos.maang.core.stream;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StreamSort {
    public static void main(String[] args) {

        // sort by length ASC
        Stream.of("cherry", "banana", "apple", "pear", "watermelon")
                .sorted(Comparator.comparing(String::length))
                .forEach(System.out::println);

        // sort by length DESC
        Stream.of("cherry", "banana", "apple", "pear", "watermelon")
                .sorted(Comparator.comparing(String::length, Comparator.reverseOrder()))
                .forEach(System.out::println);

        // sort by ASC
        Stream.of(100, 200, 50, 70, 1000)
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);

        // sort by DESC
        Stream.of(100, 200, 50, 70, 1000)
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

        // sort by last character
        Stream.of("cherry", "banana", "apple", "pear", "watermelon")
                .sorted(Comparator.comparing(word -> word.charAt(word.length() - 1)))
                .forEach(System.out::println);

        // Employees
        List<Employee> employees = List.of(
                new Employee("Alice", 5000),
                new Employee("Bob", 7000),
                new Employee("Charlie", 6000)
        );

        // sort employees by salary DESC
        employees.stream()
                .sorted(Comparator.comparing(employee -> employee.salary, Comparator.reverseOrder()))
                .forEach(System.out::println);

        /*
         * Sort employees based on their salary range:
         *
         * Less than 5000 → first
         * 5000 - 8000 → second
         * More than 8000 → last
         *
         */
        employees.stream().sorted(Comparator.comparingInt(emp -> {
                    if (emp.salary < 5000) return 1;
                    else if (emp.salary <= 8000) return 2;
                    return 3;
                }))
                .forEach(System.out::println);


        /*
         * Sort a list of words based on how many vowels (a, e, i, o, u) they contain.
         * Words with fewer vowels should come first.
         */
        List.of("cherry", "banana", "apple", "pineapple").stream()
                .sorted(Comparator.comparingInt((String value) ->
                        (int) value.chars()
                                .filter(c -> "aeiou".indexOf(Character.toLowerCase(c)) >= 0)
                                .count()
                ).reversed()) // Reverse order
                .forEach(System.out::println);

        System.out.println("task ---");

        //  Sort a Map by Value in Descending Order
        Map<String, Integer> scores = Map.of("Alice", 90, "Bob", 70, "Charlie", 85);

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .forEach(System.out::println);


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

