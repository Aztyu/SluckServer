Comment installer ?

Cloner le projet
Dans Eclipse -> Importer un projet Maven pour ouvrir le projet
Ajouter le projet � Tomcat, lancer et aller sur : http://localhost:8080/server

Pour changer la base de donn�es utilis�e :

Modifier src\main\resources\datasource.properties

datasource.username='username MySQL'
datasource.password='password MySQL'
datasource.url=jdbc:mysql://'IP MySQL':'PORT MySQL'/sluck