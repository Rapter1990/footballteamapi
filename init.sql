CREATE DATABASE footballteamdatabase;
CREATE DATABASE sonarqubedb;

CREATE USER sonar WITH PASSWORD 'sonar';
GRANT ALL PRIVILEGES ON DATABASE sonarqube TO sonar;
