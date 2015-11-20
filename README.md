# TicketServiceApp
Homework
sample application to make ticket holding and reservation




makes sure you set the below System Envrionment properties in winodows/export the variables in unix/linux/mac

JAVA_HOME=C:\Java\jdk1.8.0_40
M2_HOME=C:\Java\Apache\Maven
MAVEN_HOME=C:\Java\Apache\Maven

to compile 

C:\workspace\TicketServiceApp>mvn compile

To run the application from command line:
C:\workspace\TicketServiceApp> mvn exec:java  -Dexec.mainClass=com.mycompany.AppMain

to run the tests from commandline:
C:\workspace\TicketServiceApp>mvn test