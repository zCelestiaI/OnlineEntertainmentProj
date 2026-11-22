-- Execute once before deploying the password-hashing update to an
-- existing SQL Server/Azure SQL database (after taking a database backup).
-- PBKDF2 password strings exceed the legacy VARCHAR(50) width.
IF COL_LENGTH('dbo.Users', 'Password') IS NOT NULL
    ALTER TABLE dbo.Users ALTER COLUMN [Password] VARCHAR(255) NOT NULL;
