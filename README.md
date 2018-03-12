# Medienverwaltung
A web application to manage books, music, videos and software which uses JSF, Primefaces and Bootstrap. This web application provides a platform where users can register and view/edit/add/delete data entries about media files (books, music,videos, videoclips, software). Data entries can be searched or exported as a PDF,XLS,CSV or XML file.
Attachments for a data entry can be upload to a Nextcloud instance.

## Prerequisites
To run this application following is needed:
* A Glassfish server or Tomee server with at least JAVA JDK 7
* Postgresql Server

## Installing
* Enter DB Connection Information in File hibernate.cfg - The file is in the folder src/main/resources
* Enter URLs, username and password of a nextcloud instance in the lines 87-90 of the file /src/main/resources/...medienverwaltung/controller/MedienController 
* Recompile the project 
* Use the war file in the target folder to install the web application on the glassfish or tomee server

## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

