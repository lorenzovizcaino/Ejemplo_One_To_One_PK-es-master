USE [instituto]

-- Se elimina la tabla anterior si la hubiese
IF (OBJECT_ID('dbo.[FK_contactInfo_profesor]', 'F') IS NOT NULL)
BEGIN
	ALTER TABLE [dbo].[contactInfo] DROP CONSTRAINT [FK_contactInfo_profesor]
END

IF OBJECT_ID(N'dbo.[contactInfo]', N'U') IS NOT  NULL
BEGIN
		DROP TABLE [dbo].[contactInfo]	   
END

-- Creamos tabla contactInfo con PK profesorId (misma PK que en tabla profesor)


CREATE TABLE [dbo].[contactInfo](
	[profesorId] [int] NOT NULL,
	[email] [varchar](255) NOT NULL,
	[tlf_movil] [varchar](15) NULL,
 CONSTRAINT [PK_contactInfo_1] PRIMARY KEY CLUSTERED 
(
	[profesorId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[contactInfo]  WITH CHECK ADD  CONSTRAINT [FK_contactInfo_profesor] FOREIGN KEY([profesorId])
REFERENCES [dbo].[profesor] ([Id])
GO

ALTER TABLE [dbo].[contactInfo] CHECK CONSTRAINT [FK_contactInfo_profesor]
GO

ALTER TABLE contactInfo
ADD CONSTRAINT UC_contactInfo_UNIQUE_email UNIQUE(email)


GO


