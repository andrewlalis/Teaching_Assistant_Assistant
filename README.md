# Teaching Assistant Assistant
The next step to optimizing the performance of teaching assistants with the help of data!

Teaching assistants are usually stuck with the tedious tasks of organizing and keeping track of students, planning grading amongst themselves, and other menial tasks that have ample room for improvement. This project hopes to take advantage of this.

## Overview
The Teaching Assistant Assistant (or TAA for short) is comprised of a web interface to a persistent data system that allows for authenticated users to perform actions on the system remotely. In more layman's terms, it will allow multiple teaching assistants to view data for a course, and enter new data, much more easily than is often the case.

This application will put a focus on automating or streamlining tasks that are usually the responsibility of the teaching assistants, such as planning who will attend tutorial sessions, or how to define grading schemes, but you'll find more details in the section labelled **Use Cases**.

### Use Cases
Why go through the trouble of developing such an abstract application? Here are a few key ways in which this application will provide value to its users.

#### Data Access
With so many students (and possibly groups of students), keeping a giant spreadsheet is inefficient and hard to maintain. By persisting all of the data for the members of a course, both students and teaching assistants, it will be possible to provide easier, less painful access to relevant information faster through searches, sorting, and relationships between data points.

More specifically, the following data will be stored in this application:

* Students (and optionally student groups)
* Teaching Assistants (and optionally t.a. groups)
* Resources linked to students / groups (Github Repositories, Assignment submissions, etc.)
* Student emails and questions about the course (to form a knowledge base)
* Graded assignments with feedback on each section of the assignment
* Teaching Assistant availability
* Attendance for labs / tutorial sessions

#### Coordination
By tracking teaching assistants along with the students in a course, managing who does what, and when they do it, is much easier. Using each teaching assistant's availability, attendance records, student or team statistics, the system can plan optimum solutions for balancing workload among a number of teaching assistants.

More specifically, this application will take care of the following tasks:

* Planning which teaching assistants attend which tutorial sessions
* Deciding which teaching assistants grade which submissions
* Assigning teaching assistants to students' questions or emails
* Tracking progress for students or groups over time.

#### Efficiency in Grading
As it stands, most grading is still done using manually defined spreadsheets, formulas, rubrics, and feedback. The efficiency of this 'pipeline' can be improved quite a lot through the use of structured user interaction. This application will organize assignment submissions and their grading process in a way that minimizes errors and inefficiency.

* Assignments as a collection of partial grades (points per problem)
* Automated recording of feedback for each part of an assignment.
* Easier access to grading rubric and peer-reviewed commentary about the assignment's solutions and special cases as they become clear.
* Automated error checking and consistency in feedback.

#### More Meaningful Data
With all of the above information, this application will have everything it needs to build a rich view of the course's ecosystem, so that the performance of students (or even teaching assistants) can be analyzed for improvement and intervention. Many feel that technology drives us apart, but using this data to provide more intimate feedback and immediate action defies that sentiment.

### Technology
To accomplish all of the aforementioned goals, building a vanilla application from scratch is somewhat infeasible. Therefore, the choice has been made to use Java, in conjunction with the [Spring Boot](https://spring.io/projects/spring-boot)  framework, supported by the Java Persistence API (JPA).