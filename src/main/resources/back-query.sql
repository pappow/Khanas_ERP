
-- Create users for business

insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900000', '1234','1','1');
insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900010', '1234','1','1');
insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900020', '1234','1','1');
commit;


--


select * from opordheader;
select * from oporddetail;
delete from oporddetail;
delete from opordheader;
DELETE FROM PRO_SUGG;
commit;


select * from opordheader;
select * from oporddetail;
delete from oporddetail;
delete from opordheader;
DELETE FROM PRO_SUGG;
commit;