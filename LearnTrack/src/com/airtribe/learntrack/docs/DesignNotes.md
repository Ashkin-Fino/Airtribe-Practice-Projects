1. Why ArrayList was used instead of Array

    ArrayList was used instead of arrays because the number of Students, Trainers, Courses, Batches, and Enrollments is dynamic and not fixed.
    
    ArrayList provides:
        - Dynamic resizing automatically
        - Built-in methods such as add(), remove(), size(), contains()
        - Easier iteration and collection handling
        - Better maintainability compared to manually resizing arrays
        - Using arrays would require manual resizing and copying, making the code more complex and error-prone.

2. Where static members were used and why

    Static members were used mainly for:

        - ID generation methods (e.g., generateStudentId(), generateTrainerId(), generateEnrollmentId())
        - These methods are static because ID generation logic does not depend on object instance data and should be shared across all objects.


3. Where inheritance was used and what was gained from it?

    Inheritance was used by creating a Person base class and extending it into:
        - Student
        - Trainer

    Benefits gained:
        - Common attributes such as first name, last name, email, and age were defined once in the Person class
        - Avoided duplication of fields and validation logic
        - Improved code reuse and maintainability
        - Allowed polymorphic behavior if needed in future enhancements
        - This design follows the DRY (Don't Repeat Yourself) principle and improves extensibility of the system.