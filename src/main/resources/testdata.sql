insert into user_entity (id,login,name,password,subscription_level)
    values (1,'admin','admin','admin','STANDARD')

insert into file_entity (id,user_id,name,extension,length,blobData)
    values (1,1,'test1','txt',3,'1234567890')
insert into file_entity (id,user_id,name,extension,length,blobData)
    values (2,1,'test2','jpg',4,'0987654321')


select * from file_entity

select * from user_entity