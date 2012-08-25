Copy db_upgradeX.php to docroot. Point your web browser to it you should see 
"Upgrade process succeded" message. You can then remove file from docroot.
Start with oldest script first.

CHANGES:
db_upgrade_1.0_to_1.1.php - 20100422
-add ste_* roles
-add default values (D-admin, N-others)

db_upgrade_to_1.0.php - 20091106
-add 'uuid' and 'useUserCred' elements to '#_connections_' in 'Config' DB
-add 'PSVersion' element to '#_CONFIGURATION_' dimension in 'Config' DB
-fill those fields with default values (hash value, 0, 1.0 respectivley)