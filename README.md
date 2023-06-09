- # General
    - #### Team#:t51b
    
    - #### Names: Andrew Wang
    
    - #### Project 5 Video Demo Link:

    - #### Instruction of deployment: 
	- Original Fabflix: http://3.21.44.255:8080/cs122b-project2-login-cart-example/
	- Master: http://18.219.77.72:8080/cs122b-project2-login-cart-example/ 
	- Slave: http://18.188.30.64:8080/cs122b-project2-login-cart-example/
	- GCP Load Balancer: http://34.94.41.59/cs122b-project2-login-cart-example/ 
	- AWS Load Balancer: http://3.21.44.255/cs122b-project2-login-cart-example/

    - #### Collaborations and Work Distribution: No collaborations


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
	- All under directory: cs122b-project2-login-cart-example-main/src
        - LoginServlet.java
        - StarServlet.java
        - MovieServlet.java
        - MovieListAPI.java
        - OrderPage.java
        - Dashboard.java
        
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
	The content.xml file contains all the information needed to connect to the database, such as the username and password. Each java servlet that requires access to the mysql database does so by creating a DataSource object, which selects an available existing connection from the connection pool. This connection is then used to communicate with the fabflix mysql database.
	
    
    - #### Explain how Connection Pooling works with two backend SQL.
	Connection pooling can manage and distribute connections among the two backend SQL servers. Two connection pools are created, one for master and one for slave. When a servlet needs to access the database, write queries would get a connection from the master pool, while read queries would typically get a connection from the slave pool. After the connection is acquired from the appropriate backend, it is used to execute the query. This allows for optimized utilization of connections.
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
	- 000-default.conf


    - #### How read/write requests were routed to Master/Slave SQL?
	Normally, since most of fabflix's queries are read only, they are evenly distributed among master and slave. However, write queries from the dashboard are set up specifically to access the master SQL with a different connection pool.
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
	To run the script on the command line, it takes the file path as its singular argument. The log processing script then reads this file line by line, calculating averages by reading the 'TS' or 'TJ' at the start of each line and adding the values to their respective totals before dividing them by the total number of TS/TJ values found. 


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/SingleHTTP1Thread.png)   | 4.177                         | 4.283                                  | 0.106                        | 1 thread is the fastest server response           |
| Case 2: HTTP/10 threads                        | ![](/img/SingleHTTP10Threads.png)   | 7.858                         | 8.148                                  | 0.291                        | More threads causes the server to take more time           |
| Case 3: HTTPS/10 threads                       | ![](/img/SingleHTTPS10Threads.png)   | 5.301                         | 5.480                                  | 0.180                        | https is faster time than http           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](/img/SingleHTTPNoPooling.png)   | 60.036                      | 109.255                                 | 49.219                    | No connection pooling results in much slower times all around           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/ScaledHTTP1Thread.png)   | 4.668                         | 5.078                                  | 0.410                        | 1 thread gets the fastest server response           |
| Case 2: HTTP/10 threads                        | ![](/img/ScaledHTTP10Threads.png)   | 5.792                          | 5.965                                   | 0.173                        | on the scaled version more threads has less of an impact           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](/img/ScaledHTTPNoPooling.png)   | 5.439                         | 22.270                                  | 16.831                        | No connection pooling significantly slows           |
