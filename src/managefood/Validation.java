package managefood;
public class Validation {
    public static boolean isValidId(String id) {
        // Implement validation logic for employee ID
        return id.matches("^[0-9]{1,5}$");
    }

    public static boolean isValidName(String name) {
        // Implement validation logic for employee name
        return name.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isValidAge(String age) {
        // Implement validation logic for age
        return age.matches("^[0-9]{1,2}$");
    }

    public static boolean isValidSalary(String salary) {
        // Implement validation logic for salary
        return salary.matches("^[0-9]+$");
    }

    public static boolean isValidPhone(String phone) {
        // Implement validation logic for phone number
        return phone.matches("^\\d{10}$");
    }

    public static boolean isValidAadhar(String aadhar) {
        // Implement validation logic for Aadhar number
        return aadhar.matches("^\\d{12}$");
    }

    public static boolean isValidEmail(String email) {
        // Implement validation logic for email
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isValidGender(String gender) {
        // Implement validation logic for gender
        return gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female");
    }

    public static boolean isValidInput(String id, String name, String age, String gender, String job, String salary,
            String phone, String aadhar, String email) {
        // Implement logic to check if all inputs are valid
        return isValidId(id) && isValidName(name) && isValidAge(age) && isValidGender(gender) && isValidSalary(salary)
                && isValidPhone(phone) && isValidAadhar(aadhar) && isValidEmail(email);
    }

    public static boolean isValidTableNo(String tableNo) {
        // Check if the table number is not empty and contains only digits
        return tableNo != null && tableNo.matches("\\d+");
    }
}
