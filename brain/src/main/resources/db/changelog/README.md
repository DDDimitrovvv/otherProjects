## Instructions how to execute sql files:

1. Create an SQL file;
2. Check the validation and sequence after the last executed file in changelog-master.xml;
3. Mark which sql file must be included / executed by application starting:
   - comment the include files that must not be executed;
4. Check the preset value of hibernate:ddl-auto:
   -  set in application.yml file the property for "hibernate:ddl-auto:" to validate;