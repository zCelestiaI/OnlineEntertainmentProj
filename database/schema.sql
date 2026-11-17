-- Online Entertainment / PolyOE: CREATE TABLE script for a NEW EMPTY database.
-- The project originally used NVARCHAR for all textual identifiers and labels.
-- To upgrade an EXISTING database instead, run database/migrate_youtube_mvp.sql.
-- No sample accounts are created; register through /register.

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.Users (
    Id NVARCHAR(20) NOT NULL CONSTRAINT PK_Users PRIMARY KEY,
    [Password] NVARCHAR(255) NOT NULL,
    Fullname NVARCHAR(50) NOT NULL,
    Email NVARCHAR(50) NOT NULL,
    [Admin] BIT NOT NULL CONSTRAINT DF_Users_Admin DEFAULT (0),
    ChannelName NVARCHAR(100) NULL,
    ChannelDescription NVARCHAR(500) NULL
  );
END;

IF OBJECT_ID(N'dbo.Video', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.Video (
    Id NVARCHAR(20) NOT NULL CONSTRAINT PK_Video PRIMARY KEY,
    Title NVARCHAR(255) NOT NULL,
    Poster NVARCHAR(255) NULL,
    [Views] INT NOT NULL CONSTRAINT DF_Video_Views DEFAULT (0),
    [Description] NVARCHAR(MAX) NULL,
    Active BIT NOT NULL CONSTRAINT DF_Video_Active DEFAULT (1),
    UploaderId NVARCHAR(20) NULL,
    SourceUrl NVARCHAR(1024) NULL,
    SourceType NVARCHAR(20) NULL,
    CreatedAt DATETIME2 NOT NULL CONSTRAINT DF_Video_CreatedAt DEFAULT(SYSUTCDATETIME()),
    CONSTRAINT FK_Video_Uploader FOREIGN KEY(UploaderId) REFERENCES dbo.Users(Id)
  );
END;

IF OBJECT_ID(N'dbo.Favorite', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.Favorite (
    Id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Favorite PRIMARY KEY,
    UserId NVARCHAR(20) NOT NULL,
    VideoId NVARCHAR(20) NOT NULL,
    LikeDate DATE NOT NULL CONSTRAINT DF_Favorite_LikeDate DEFAULT (CONVERT(DATE, GETDATE())),
    CONSTRAINT FK_Favorite_User FOREIGN KEY(UserId) REFERENCES dbo.Users(Id),
    CONSTRAINT FK_Favorite_Video FOREIGN KEY(VideoId) REFERENCES dbo.Video(Id),
    CONSTRAINT UQ_Favorite_User_Video UNIQUE(UserId, VideoId)
  );
END;

IF OBJECT_ID(N'dbo.[Share]', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.[Share] (
    Id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Share PRIMARY KEY,
    UserId NVARCHAR(20) NOT NULL,
    VideoId NVARCHAR(20) NOT NULL,
    Emails NVARCHAR(255) NOT NULL,
    ShareDate DATE NOT NULL CONSTRAINT DF_Share_ShareDate DEFAULT (CONVERT(DATE, GETDATE())),
    CONSTRAINT FK_Share_User FOREIGN KEY(UserId) REFERENCES dbo.Users(Id),
    CONSTRAINT FK_Share_Video FOREIGN KEY(VideoId) REFERENCES dbo.Video(Id)
  );
END;
