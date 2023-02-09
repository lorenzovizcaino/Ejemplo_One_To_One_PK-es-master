USE [instituto]
GO

DECLARE @filas int =0;

select @filas =  COUNT(*) FROM contactInfo;
if(@filas=0)
BEGIN

	DECLARE @email varchar(255)
	DECLARE @movil varchar(15)
	DECLARE @counter int = 100
	DECLARE @profeId int 

	DECLARE @max_counter int

	select  @max_counter =  count(*) + @counter from profesor

	SET @email='profesorXX@edu.gal'
	SET @movil = '+34 600 123 '

	DECLARE cursor_profesor CURSOR
	FOR 
	SELECT ID FROM profesor ORDER BY ID 

	OPEN cursor_profesor  

		FETCH NEXT FROM cursor_profesor INTO @profeId
		-- valor 0 :  	La instrucción FETCH se ejecutó correctamente.
		IF @@FETCH_STATUS <> 0   
		PRINT '         Hubo un error recuperando datos del cursor'    
		
		WHILE (@@FETCH_STATUS = 0 ) AND  (@counter< @max_counter)
		BEGIN
			INSERT INTO [dbo].[contactInfo]
						([profesorId]
						,[email]
						,[tlf_movil]
						)
			VALUES
						(@profeId
						,REPLACE(@email, 'XX', @counter)
						,@movil+ cast(@counter as varchar(3))
						)

			
			SET @counter = @counter +1
			FETCH NEXT FROM cursor_profesor INTO @profeId
		END

		CLOSE cursor_profesor  
		DEALLOCATE cursor_profesor 

END
