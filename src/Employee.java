class Employee {
    private int ID;
    private String name;
    private double salary;

    public int getID() {
        return this.ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Employee(int ID, String name, double salary) {
        this.ID = ID;
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void incrementSalary(double percentage) {
        this.salary += (this.salary * percentage) / 100;
    }

    @Override
    public String toString() {
        // % starts the format specifier.
        // , adds group separators (i.e. commas) for the thousands place.
        // 10 sets the minimum width of the total output, and pads the whole number with spaces if the text is short.
        // .2 restricts the number to exactly 2 decimal places.
        // f formats the value as a floating-point number.
        return String.format("{\"ID\": %d, \"name\": %s, \"salary\": $%,10.2f}", this.ID, this.name, this.salary);
    }
}
