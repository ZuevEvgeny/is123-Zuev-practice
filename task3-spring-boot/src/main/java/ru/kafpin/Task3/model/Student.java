package ru.kafpin.Task3.model;

public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String city;
    private String gender;
    private int age;
    private String faculty;
    private String groupName;
    private String login;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String middleName,
                   String email, String city, String gender, int age,
                   String faculty, String groupName, String login) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.city = city;
        this.gender = gender;
        this.age = age;
        this.faculty = faculty;
        this.groupName = groupName;
        this.login = login;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }
}