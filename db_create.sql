CREATE DATABASE olymp;
USE olymp;
CREATE TABLE UserApp
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	Email TEXT NOT NULL,
    Name VARCHAR(255) NOT NULL
);
CREATE TABLE Task
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    TimeLimit INT,
    MemoryLimit INT,
	IsAvailable TINYINT,
    Description TEXT NOT NULL
);
CREATE TABLE Test
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    TaskId INT NOT NULL,
    InputData TEXT NOT NULL,
    OutputData TEXT NOT NULL
);
CREATE TABLE Submission
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    TaskId INT NOT NULL,
    TestId INT,
    Verdict TEXT,
    TimeSpent INT,
    MemorySpent INT,
    UserId INT NOT NULL
);
CREATE TABLE TasksForUser
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	StartDate DATE NOT NULL,
	EndDate DATE NOT NULL,
    TaskId INT NOT NULL,
    UserId INT NOT NULL
);
CREATE TABLE Contest
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	IsVirtual TINYINT,
	StartDateTime DATETIME NOT NULL,
	EndDateTime DATETIME NOT NULL,
    Description TEXT NOT NULL
);
CREATE TABLE ContestTasks
(
    IdContest INT,
    IdTask INT	
);
ALTER TABLE Test ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE Submission ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE TasksForUser ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE Submission ADD FOREIGN KEY (UserId) REFERENCES UserApp (Id);
ALTER TABLE TasksForUser ADD FOREIGN KEY (UserId) REFERENCES UserApp (Id);
ALTER TABLE ContestTasks ADD FOREIGN KEY (IdContest) REFERENCES Contest (Id);
ALTER TABLE ContestTasks ADD FOREIGN KEY (IdTask) REFERENCES Task (Id);


INSERT INTO Task(Name, Description) VALUES ("A + B", "Please calculate A + B and write result to the standard output.");
INSERT INTO Task(Name, Description) VALUES ("A - B", "Please calculate A - B and write result to the standard output.");
INSERT INTO Task(Name, Description) VALUES ("A * B", "Please calculate A * B and write result to the standard output.");
INSERT INTO Task(Name, Description) VALUES ("A + B Hard", "Please calculate A + B and write result to the standard output.");
INSERT INTO Task(Name, Description) VALUES ("A * B Hard", "Please calculate A * B and write result to the standard output.");
