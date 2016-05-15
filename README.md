# Courier Bird

<img align="right" src="logo_128.png?raw=true">

A web application for use by a company specializing in delivery service.

This project has been developed within the discipline "The Modern Platforms of Software Development" of the 6th term of education at BSUIR, 2016.

**Please, do not use this application for commercial activity.**

####Disclaimer

The application is still in the Beta stage, so it may contain bugs, as well as speed/performance issues and may still cause crashes or data loss. Use it for your own risk!

> In no event shall the copyright owner or contributors be liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this software, even if advised of the possibility of such damage.

##Description

**Courier Bird** is a JavaEE application. The backend is written on the [Apache Struts 2] framework, and the frontend is written in HTML5, CSS3 and JavaScript, using [Bootstrap] and [AngularJS].

> The frontend is based on a free [Xeon Onepage Site Template].

####Actors

* Guest
* Client
* Manager
* Courier
* Accountant

####Documents

* Agreement
* Act
* Finance Report
* Price list
* List of orders (for courier)

####Entities
* Order
* Application
* Shipping
* User
* Office
* Role
* Status

##Dependencies

The 3rd party libraries used in the project:

- [Apache Commons]
- [Apache Log4J 2.x]
- [Apache POI]
- [iText PDF]
- [JUnit 4]
- [MySQL Connector/J]

##Build

1. [Download] and unzip the source repository, or clone it using Git:  
`git clone https://github.com/mariateseiko/DeliveryService.git`
2. Cd into the project directory:  
`cd DeliveryService`
3. Build with Maven:  
`mvn build`

##Run

Use [Apache Tomcat] (version 8.0 and higher) to deploy and run the application.

##Further development

The future of the project is unknown. There are a lot to do.

- [ ] Extend a sphere coverage
- [ ] Add roles
- [ ] More functionallity
- [ ] Code refactoring
- [ ] Localization
- [ ] More...

##Licence

This software is distributing without any licence. You can use, modify and distribute it freely!

[Apache Struts 2]: https://struts.apache.org/
[Bootstrap]: http://getbootstrap.com/
[AngularJS]: https://angularjs.org/
[Xeon Onepage Site Template]: https://shapebootstrap.net/item/1524966-xeon-best-onepage-site-template
[Download]: https://github.com/mariateseiko/DeliveryService/archive/master.zip
[Apache Tomcat]: http://tomcat.apache.org/
[Apache Commons]: https://commons.apache.org/
[Apache Log4J 2.x]: http://logging.apache.org/log4j/2.x/
[Apache POI]: https://poi.apache.org/
[iText PDF]: http://itextpdf.com/
[JUnit 4]: http://junit.org/junit4/
[MySQL Connector/J]: https://dev.mysql.com/downloads/connector/j/
