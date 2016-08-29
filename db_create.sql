CREATE DATABASE olymp;
USE olymp;
CREATE TABLE UserApp
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	Email TEXT NOT NULL,
	Token TEXT NOT NULL,
    Name VARCHAR(255) NOT NULL
);
CREATE TABLE Task
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    TimeLimit INT,
    MemoryLimit INT,
	IsAvailable TINYINT,
	LabNum INT DEFAULT -1,
	LabTaskNum INT DEFAULT -1,
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
    Commentary TEXT,
    Lang TEXT,
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
	Length INT,
    Description TEXT NOT NULL
);
CREATE TABLE ContestTasks
(
    IdContest INT NOT NULL,
    IdTask INT NOT NULL	
);
CREATE TABLE ContestInstance
(
	Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    IdContest INT NOT NULL,
    StartTime DATETIME NOT NULL
);
CREATE TABLE ContestUser
(
	Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    IdContestInstance INT NOT NULL,
    IdUser INT NOT NULL
);
CREATE TABLE ContestStatus
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    TaskId INT NOT NULL,
    TestId INT,
    Verdict TEXT,
    TimeSpent INT,
    MemorySpent INT,
    UserId INT NOT NULL,
    ContestInstanceId INT NOT NULL
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
