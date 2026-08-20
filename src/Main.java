import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) {
        Employee[] employees = {
                new Employee(1, "Mickey Mouse", 100000.0),
                new Employee(2, "Donald Duck", 200000.0),
                new Employee(3, "Goofy Goo", 300000.0)
        };
        Employee brotherBear = new Employee(4, "Brother Bear", 5000.0);
        Employee mufasa = new Employee(5, "Mufasa - The Lion King", 500000.0);

        // STREAM CREATION
        Stream<Employee> employeeStream1 = Stream.of(employees);  // Turn Employee array to a stream!
        List<Employee> empList = Arrays.asList(employees);  // Convert Employee array to a List!
        Stream<Employee> employeeStream2 = empList.stream(); // Turn Employee list to a stream!
        Stream<Employee> employeeStream = Stream.of(brotherBear, mufasa); // Make a stream from individual Employee objs
        // Use Builder Design Pattern to Build a Stream of Objects
        Stream.Builder<Employee> builder = Stream.builder();
        builder.accept(brotherBear);
        builder.accept(mufasa);
        builder.accept(new Employee(6, "SherKhan", 450000.0));
        Stream<Employee> employeeStream3 = builder.build();

        // STREAM OPERATIONS
        // Terminal operation, for each Employee in the stream we increment salary by 10%
        empList.stream().forEach(e -> e.incrementSalary(10.0));

        // Intermediate operation map applies method reference getSalary to each Employee stream object
        // Terminal operation collect uses toList Collector static method to aggregate salaries into a List of Double(s)
        List<Double> salaries = empList.stream().map(Employee::getSalary).collect(Collectors.toList());

        // Stream of integers returned as stream of squared versions of themselves.
        // Intermediate operation map applies lambda expression, x * x, to each integer in the stream
        Stream<Integer> squared = Stream.of(1, 2, 3, 4, 5).map(x -> x * x);

        // Intermediate operation filter reduces/filters Employee stream to only stream Employee objs where
        // e.getSalary() < 200K
        // Terminal operation collect uses the static Collectors.toList method to aggregate remaining Employee objs
        // back into a List of Employees.
        List<Employee> employeesWithSalariesUnder200K = empList.stream()
                .filter(e -> e.getSalary() < 200000)
                .collect(Collectors.toList());
        //NOTE NOW: A stream pipeline consists of a stream source, followed by zero or more intermediate operations,
        //          and a terminal operation, such as above.

        // Optionals can be returned within streams, which allows us to handle "empty" streams or filtered streams that
        // filter to an empty stream.
        // Terminal operation findFirst returns an Optional object, which will either be "empty" or contain the first
        // Employee object in the empList stream.
        Optional<Employee> employee = empList.stream().findFirst();
        if(employee.isPresent()) {
            System.out.println(employee.get());
        }

        // collect() is used to collect the stream into a Collection, but if we need to get an array out of the stream,
        // we can simply use Terminal operation toArray().
        Employee[] employeesArr = empList.stream().toArray(Employee[]::new);
        System.out.printf("Object: %s, Arr Length: %d%n", employeesArr, employeesArr.length);

        // This is a List of Lists of String pairs/couples
        // Notice Java Generics usage i.e. <> which is necessary for Java's strong typing.
        List<List<String>> couples = Arrays.asList(
                Arrays.asList("Donald Duck", "Daisy Duck"),
                Arrays.asList("Mickey Mouse", "Minnie Mouse"),
                Arrays.asList("Nobita", "Shizuka"));

        // Intermediate operation flatMap applies method reference Collection::stream to stream each inner List of
        // Strings. So, this List of List of Strings becomes just a List of Strings
        // Terminal operation collect uses static Collectors.toList to aggregate the Strings back into a List of Strings
        List<String> coupleEmployees = couples.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(coupleEmployees);  // Direct print takes advantage of toString overrides

        // One thing that significantly improves Java streams is the ability to evaluate operations lazily.
        // This means Intermediate operations are not invoked when defined.
        // They are ONLY when a Terminal operation is invoked.
        // Think of intermediate operations like building a pipeline and adding stages.
        // Only once the terminal operation gets invoked do the elements of the pipeline get pulled through each stage.
        Employee first100Kemployee = Stream.of(employees)
                .filter(e -> e != null)
                .filter(e -> e.getSalary() > 100000)
                .findFirst()
                .orElse(null);
        System.out.println(first100Kemployee);

        // Comparison Based Stream Operators

        // Intermediate operation sorted() sorts the input stream based on the **Comparator** passed inside it.
        // In this case, the Comparator is a static function comparing "Double" data types returned by method reference
        // Employee::getSalary
        List<Employee> salarySortedEmp = empList.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .toList();
        System.out.println(salarySortedEmp);

        // Max and Min of Streams
        Employee highestSalariedEmployee = empList.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .orElseThrow(NoSuchElementException::new);

        Employee lowestSalariedEmployee = empList.stream()
                .min(Comparator.comparing(Employee::getSalary))
                .orElseThrow(NoSuchElementException::new);

        System.out.printf("Max: %s, Min: %s%n", highestSalariedEmployee, lowestSalariedEmployee);

        // allMatch, anyMatch, and noneMatch - Take a predicate and return a boolean
        List<Integer> intList = Arrays.asList(10, 8, 7, 1, 2, 4, 5);
        boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
        boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
        boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);

        System.out.printf("allEven: %s, oneEven: %s, noneMultipleOfThree: %s%n", allEven, oneEven, noneMultipleOfThree);

        // Primitive Data Type Streams
        int latestEmpId = empList.stream()
                .mapToInt(Employee::getID)
                .max()
                .orElseThrow(NoSuchElementException::new);
        System.out.printf("Latest Employee ID: %d%n", latestEmpId);

        // Specialised streams provide some additional operations that make dealing with numbers quite effortless.
        Double averageSalary = empList.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow(NoSuchElementException::new);
        System.out.printf("Average Salary: %,10.2f%n", averageSalary);

        // Reduction Operations
        // A reduction is the process of combining a stream into a summarised result by applying a combination
        // operation. We already saw a few reduction operations like findFirst(), min(), and max().
        Double totalSalaries = empList.stream()    // Stream all Employee objs
                .map(Employee::getSalary)          // Map to Double by salary by Employee reference method
                .reduce(0.0, Double::sum); // Reduce Double stream by adding each Double salary to 0.0
        System.out.printf("Total Salaries: %,10.2f%n", totalSalaries);

        // Advanced collect
        // We already saw how we used Collectors.toList() to get the list out of the stream.
        // Let’s now see a few more ways to collect elements from the stream.
        String empNames = empList.stream()  // Streams Employee ojbs
                .map(Employee::getName) // Maps to String stream ojbs from Employee's Name
                .collect(Collectors.joining(", "))  // Collects String stream objects with joining collector
                .toString();  // The result is all strings aggregated by a ","

        // Similar to above, but lets return the names as a Set
        Set<String> empNamesSet = empList.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
        System.out.println(empNamesSet);

        // We can use Collectors.toCollection() to extract the elements into any other collection by passing
        // in a Supplier<Collectio>
        // Here, an empty collection is created internally, and its add() method is called on each element of
        // the stream.
        Vector<String> empNamesVec = empList.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(Vector::new));
        System.out.println(empNamesVec);

        // Summaraizing Stats can be done easily with streams
        // The DoubleSummaryStatistics objects get us statistics like – count, sum, min, max, average, etc.
        DoubleSummaryStatistics stats = empList.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));

        Long count = stats.getCount();
        Double sum = stats.getSum();
        Double max = stats.getMax();
        Double min = stats.getMin();
        Double avg = stats.getAverage();
        System.out.printf("Count: %d, Sum: %f, Max: %f, Min: %f, Average: %f%n", count, sum, max, min, avg);

        // When using Specialized types for primitives, we can use summaryStatistics to get similar results.
        DoubleSummaryStatistics statsV2 = empList.stream()
                .mapToDouble(Employee::getSalary)
                .summaryStatistics();

        count = statsV2.getCount();
        sum = statsV2.getSum();
        max = statsV2.getMax();
        min = statsV2.getMin();
        avg = statsV2.getAverage();
        System.out.printf("Count: %d, Sum: %f, Max: %f, Min: %f, Average: %f%n", count, sum, max, min, avg);

        // Streams can even partition objects
        Map<Boolean, List<Integer>> mapOfEvenOdd = Stream.of(2, 4, 5, 6, 8).collect(
                Collectors.partitioningBy(i -> i % 2 == 0));

        System.out.printf("Even: %s%n", mapOfEvenOdd.get(true));
        System.out.printf("Odd: %s%n", mapOfEvenOdd.get(false));

        // Or Partition into more than 2 groups with groupingBy
        // Partitions Employee objects by first letter of first name
        Map<String, List<Employee>> groupByAlphabet = empList.stream().collect(
                Collectors.groupingBy(e -> String.valueOf(e.getName().charAt(0))));
        System.out.println(groupByAlphabet);

        // Partitions Employee objects by first letter of first name, but collects Employee IDs instead of
        // Employee objects using Collectors.mapping.
        Map<String, List<Integer>> idsGroupedByFirstChar = empList.stream().collect(
                Collectors.groupingBy(e -> String.valueOf(e.getName().charAt(0)),
                        Collectors.mapping(Employee::getID, Collectors.toList())));
        System.out.println(idsGroupedByFirstChar);

        // Reducing is similar to reduce, but returns a Collector to perform the reduction.
        Double percentage = 10.0;
        Double salIncrOverhead = empList.stream().collect(
                Collectors.reducing(0.0, // Seed value of 0.0 to add the first stream salary to.
                        e -> e.getSalary() * percentage / 100,  // Calculates salary increase per Employee obj
                        (s1, s2) -> s1 + s2)); // Adds previous stream total to current stream increase
        // The end result is reducing all of the salary increases to a total.
        System.out.println(salIncrOverhead);

        // Parallel Streams to increase performance

        // Here, incrementSalary would get executed on multiple elements in parallel. As in the case with
        // writing multi-threaded code, one needs to be aware of a couple of things while using parallel():
        // 1. Code is to be thread-safe. Special care is to be taken if operations performed access shared
        //   data.
        // 2. If order is of importance, parallel streams should be avoided. The result after each run
        // would differ.
        empList.stream().parallel().forEach(e -> e.incrementSalary(10.0));

        // Infinite Streams
        Stream.generate(Math::random)
                .limit(10)  // This is the termination case for this "infinite stream"
                .forEach(System.out::println);

        // Iterative Streams
        // Seeded with 2
        // Terminates with limit
        // Streams "5 i.e. the limit" multiples of 2 via i -> i * 2 lambda expression.
        List<Integer> firstFiveMultiplesOfTwo = Stream.iterate(2, i -> i * 2)
                .limit(5)
                .collect(Collectors.toList());
        System.out.println(firstFiveMultiplesOfTwo);
    }
}
