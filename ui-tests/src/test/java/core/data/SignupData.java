package core.data;

public class SignupData {

    private final String name;
    private final String email;
    private final String title;
    private final String password;
    private final String dayOfBirth;
    private final String monthOfBirth;
    private final String yearOfBirth;
    private final String firstName;
    private final String lastName;
    private final String company;
    private final String address;
    private final String address2;
    private final String country;
    private final String state;
    private final String city;
    private final String zipcode;
    private final String mobileNumber;





    public SignupData(
             String name,
             String email,
             String title,
             String password,
             String dayOfBirth,
             String monthOfBirth,
             String yearOfBirth,
             String firstName,
             String lastName,
             String company,
             String address,
             String address2,
             String country,
             String state,
             String city,
             String zipcode,
             String mobileNumber
    ) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.password = password;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.address = address;
        this.address2 = address2;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipcode = zipcode;
        this.mobileNumber = mobileNumber;

    }
    //Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTitle() { return title; }
    public String getPassword() { return password; }
    public String getDayOfBirth() { return dayOfBirth; }
    public String getMonthOfBirth() { return monthOfBirth; }
    public String getYearOfBirth() { return yearOfBirth; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompany() { return company; }
    public String getAddress() { return address; }
    public String getAddress2() { return address2; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
    public String getMobileNumber() { return mobileNumber; }


}


