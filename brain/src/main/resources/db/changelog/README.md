## Instructions how to execute sql files:

1. Create a SQL file;
2. Check the validation and sequence after the last executed file in changelog-master.xml;
3. Mark which sql file must be included / executed by application starting;
4. Check the preset value of hibernate:ddl-auto:
   - if table "baseid" is missing - hibernate:ddl-auto: create and comment all the included lines changelog-master.xml;
   This will create all tables without any inserted data;
   - if table "baseid" is available use hibernate:ddl-auto: none to execute all sql files included in changelog-master.xml (remove all tables except baseid);
   - when all sql files are a properly executed stop the application and set the property hibernate:ddl-auto: update or leave it to none;