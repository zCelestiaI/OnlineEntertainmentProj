-- Run this against your EXISTING PolyOE database BEFORE starting the updated app.
-- Preserves all existing Users, Video, Favorite and Share rows.
-- Back up your database first; do not use schema.sql against an existing DB.
-- Original column Password was NVARCHAR(50), too short for PBKDF2 hashes.

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL OR OBJECT_ID(N'dbo.Video', N'U') IS NULL
BEGIN
  THROW 50001, 'Original PolyOE Users and Video tables must exist', 1;
END;

IF COL_LENGTH('dbo.Users', 'Password') IS NOT NULL
    ALTER TABLE dbo.Users ALTER COLUMN [Password] NVARCHAR(255) NOT NULL;

IF COL_LENGTH('dbo.Users', 'ChannelName') IS NULL
    ALTER TABLE dbo.Users ADD ChannelName NVARCHAR(100) NULL;
IF COL_LENGTH('dbo.Users', 'ChannelDescription') IS NULL
    ALTER TABLE dbo.Users ADD ChannelDescription NVARCHAR(500) NULL;

IF COL_LENGTH('dbo.Video', 'UploaderId') IS NULL
    ALTER TABLE dbo.Video ADD UploaderId NVARCHAR(20) NULL;
IF COL_LENGTH('dbo.Video', 'SourceUrl') IS NULL
    ALTER TABLE dbo.Video ADD SourceUrl NVARCHAR(1024) NULL;
IF COL_LENGTH('dbo.Video', 'SourceType') IS NULL
    ALTER TABLE dbo.Video ADD SourceType NVARCHAR(20) NULL;
IF COL_LENGTH('dbo.Video', 'CreatedAt') IS NULL
BEGIN
    ALTER TABLE dbo.Video ADD CreatedAt DATETIME2 NULL;
    UPDATE dbo.Video SET CreatedAt = SYSUTCDATETIME() WHERE CreatedAt IS NULL;
    ALTER TABLE dbo.Video ALTER COLUMN CreatedAt DATETIME2 NOT NULL;
    ALTER TABLE dbo.Video ADD CONSTRAINT DF_Video_CreatedAt DEFAULT(SYSUTCDATETIME()) FOR CreatedAt;
END;

IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Video_Uploader'
               AND parent_object_id = OBJECT_ID(N'dbo.Video'))
    ALTER TABLE dbo.Video
    ADD CONSTRAINT FK_Video_Uploader FOREIGN KEY(UploaderId) REFERENCES dbo.Users(Id);

-- Existing admin-managed Video rows keep UploaderId=NULL and remain visible.
-- Old accounts remain valid: plaintext passwords are migrated on first successful login.
-- CAUTION: original example accounts ('admin','123') must be changed before public use.
