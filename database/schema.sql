-- Initial schema for SQL Server / Azure SQL (run against an empty database).
-- Based on the four JPA entities; no sample accounts or real data are included.
-- SECURITY: current app stores passwords as plaintext; do not invite real users yet.

IF OBJECT_ID('dbo.Users','U') IS NULL
CREATE TABLE dbo.Users (
  Id VARCHAR(20) NOT NULL PRIMARY KEY,
  [Password] VARCHAR(255) NOT NULL,
  Fullname VARCHAR(50) NOT NULL,
  Email VARCHAR(50) NOT NULL,
  [Admin] BIT NOT NULL DEFAULT (0)
);
IF OBJECT_ID('dbo.Video','U') IS NULL
CREATE TABLE dbo.Video (
  Id VARCHAR(20) NOT NULL PRIMARY KEY,
  Title VARCHAR(255) NOT NULL,
  Poster VARCHAR(255) NULL,
  [Views] INT NOT NULL DEFAULT (0),
  [Description] NVARCHAR(MAX) NULL,
  Active BIT NOT NULL DEFAULT (1)
);
IF OBJECT_ID('dbo.Favorite','U') IS NULL
CREATE TABLE dbo.Favorite (
  Id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  UserId VARCHAR(20) NOT NULL REFERENCES dbo.Users(Id),
  VideoId VARCHAR(20) NOT NULL REFERENCES dbo.Video(Id),
  LikeDate DATE NOT NULL DEFAULT (CONVERT(DATE,GETDATE())),
  CONSTRAINT UQ_Favorite_User_Video UNIQUE (UserId,VideoId)
);
IF OBJECT_ID('dbo.Share','U') IS NULL
CREATE TABLE dbo.[Share] (
  Id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  UserId VARCHAR(20) NOT NULL REFERENCES dbo.Users(Id),
  VideoId VARCHAR(20) NOT NULL REFERENCES dbo.Video(Id),
  Emails VARCHAR(255) NOT NULL,
  ShareDate DATE NOT NULL DEFAULT (CONVERT(DATE,GETDATE()))
);
-- To test admin pages: register a local throwaway account, then promote it
-- by an explicit database command only after reviewing app security:
-- UPDATE dbo.Users SET [Admin] = 1 WHERE Id = 'local-demo-user';
