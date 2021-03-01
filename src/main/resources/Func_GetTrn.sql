CREATE OR REPLACE FUNCTION Func_GetTrn(
	Vzid VARCHAR2,
	Vtypetrn VARCHAR2,
	Vtrn VARCHAR,
	Vlen NUMBER) RETURN VARCHAR2
IS
	Vlastnum NUMBER;
	Vinc NUMBER;
	Vnewnum NUMBER;
	Vnewnumlength NUMBER;
	VcountV NUMBER;
	Vpaddval VARCHAR2(30);
	VreturnNumber VARCHAR2(100);
	v_stmt            long;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
	Vlastnum := 0;
	Vinc := 1;
	VcountV := 1;
	Vpaddval := '0';

	SELECT xnum INTO Vlastnum FROM xtrn WHERE zid=Vzid AND xtypetrn=Vtypetrn AND xtrn=Vtrn;
	SELECT xinc INTO Vinc FROM xtrn WHERE zid=Vzid AND xtypetrn=Vtypetrn AND xtrn=Vtrn;

	Vnewnum := Vlastnum + Vinc;

	/* UPDATE xtrn TABLE FOR NEXT VALUE */
	v_stmt:='UPDATE xtrn SET xnum ='|| Vnewnum ||' WHERE zid='''||Vzid ||''' AND xtypetrn='''||Vtypetrn ||''' AND xtrn='''||Vtrn||'''';
	EXECUTE IMMEDIATE v_stmt; 
	COMMIT;

	/* GENERATE NUMBER */
    VreturnNumber := TO_CHAR(Vnewnum);
	Vnewnumlength := Vlen - LENGTH(VreturnNumber);
	WHILE VcountV <= Vnewnumlength LOOP
		VreturnNumber := CONCAT( Vpaddval, VreturnNumber );
		VcountV := VcountV + 1;
	END LOOP;

	/* Set number to retrun value */
	VreturnNumber := CONCAT(Vtrn , VreturnNumber);
	return VreturnNumber;
END;