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
    TimeLimit INT DEFAULT 2,
    MemoryLimit INT DEFAULT 64,
	IsAvailable TINYINT,
	LabNum INT DEFAULT -1,
	LabTaskNum INT DEFAULT -1,
    Description TEXT NOT NULL,
    Checker TEXT,
    CheckerLanguage VARCHAR(255)
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
	Code TEXT,
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

CREATE TABLE ContestUser
(
	Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	StartDate DATE NOT NULL,
	EndDate DATE NOT NULL,
    IdContest INT NOT NULL,
    IdUser INT NOT NULL
);

CREATE TABLE ContestSubmission
(
    Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    TaskId INT NOT NULL,
    TestId INT,
    Verdict TEXT,
    TimeSpent INT,
    MemorySpent INT,
    ContestUserId INT NOT NULL
);

ALTER TABLE Test ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE Submission ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE TasksForUser ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE Submission ADD FOREIGN KEY (UserId) REFERENCES UserApp (Id);
ALTER TABLE TasksForUser ADD FOREIGN KEY (UserId) REFERENCES UserApp (Id);
ALTER TABLE ContestTasks ADD FOREIGN KEY (IdContest) REFERENCES Contest (Id);
ALTER TABLE ContestTasks ADD FOREIGN KEY (IdTask) REFERENCES Task (Id);
ALTER TABLE ContestSubmission ADD FOREIGN KEY (ContestUserId) REFERENCES ContestUser (Id);
ALTER TABLE ContestSubmission ADD FOREIGN KEY (TaskId) REFERENCES Task (Id);
ALTER TABLE ContestUser ADD FOREIGN KEY (IdContest) REFERENCES Contest(Id);
ALTER TABLE ContestUser ADD FOREIGN KEY (IdUser) REFERENCES UserApp (Id);

INSERT INTO UserApp(Email, Token, Name) VALUES ("108th@mail.ru", "475508", "Judge");

INSERT INTO Task(Name, Description, IsAvailable) VALUES ("A + B", "Please calculate A + B and write result to the standard output.", true);
INSERT INTO Task(Name, Description, IsAvailable) VALUES ("A - B", "Please calculate A - B and write result to the standard output.", true);
INSERT INTO Task(Name, Description, IsAvailable) VALUES ("A * B", "Please calculate A * B and write result to the standard output.", true);
INSERT INTO Task(Name, Description, IsAvailable) VALUES ("A + B Hard", "Please calculate A + B and write result to the standard output.", true);
INSERT INTO Task(Name, Description, IsAvailable) VALUES ("A * B Hard", "Please calculate A * B and write result to the standard output.", true);

INSERT INTO Test(TaskId, InputData, OutputData) VALUES (1, "1\n2", "3\n");
INSERT INTO Test(TaskId, InputData, OutputData) VALUES (2, "1\n2", "-1\n");
INSERT INTO Test(TaskId, InputData, OutputData) VALUES (3, "1\n2", "2\n");
INSERT INTO Test(TaskId, InputData, OutputData) VALUES (4, "14687236487236478234\n57832647823648723648", "7251988431088520187\n");
INSERT INTO Test(TaskId, InputData, OutputData) VALUES (5, "112431\n2123123", "238704842013\n");

UPDATE Task SET Checker = "import java.util.Scanner;\nimport java.io.File;\nimport java.io.FileNotFoundException;\n\npublic class Checker {\n\n    public static void main(String... args) throws FileNotFoundException {\n        Scanner S_in = new Scanner(new File(args[0]));\n        Scanner S_out = new Scanner(new File(args[1]));\n        int a = S_in.nextInt();\n        int b = S_in.nextInt();\n        int c = S_out.nextInt();\n        if(a + b == c) System.out.println(\"OK\"); else System.out.println(\"WA\");\n    }\n}" WHERE Id = 1;

UPDATE Task SET CheckerLanguage = "JAVA" WHERE Id = 1;