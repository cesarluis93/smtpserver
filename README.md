# smtpserver
Servidor SMTP

En este laboratorio debe implementar un servidor SMTP básico. Su programa debe interpretar los comandos SMTP desde la perspectiva del Servidor. 

Los comandos SMTP que se requieren son: HELO, MAIL FROM, RCPT TO, DATA,”.” (Fin de Correo), QUIT. Además debe seguir la secuencia de estados establecida por el protocolo.

Su servidor debe contestar con los códigos de respuesta adecuados, ya sea que el comando fue aceptado o si ocurrió algún error.

Debe configurar la estructura de almacenamiento de correos en su servidor. Para ello debe contar con un listado de usuarios registrados, y debe diseñar un esquema de almacenamiento de correos por usuario (Archivos planos, una base de datos, etc). Si su servidor recibe un correo para uno de sus usuarios, simplemente debe guardar el correo en la estructura correspondiente. Si uno de los destinatarios no pertenece a los usuarios del servidor, entonces debe proceder a abrir una conexión con el servidor asociado a dicho usuario, y enviar el correo utilizando el protocolo SMTP.
 
El formato de direcciones de correo será el siguiente:

	<usuario>@<dominio>

Para poder resolver el dominio, por ejemplo si su servicio se llama “ewoks.com” deben instalar un servicio DNS en su máquina y configurarlo como el servidor por defecto para su tarjeta. (Podrían probar el SimpleDNS, OpenDNS o cualquier otro servidor.) Se recomienda que prueben su servidor con el servidor de otro grupo. Deben agregar las entradas necesarias para poder resolver los diferentes dominios de los grupos con quienes vayan a conectarse. 

Para las pruebas, con Telnet se estará abriendo una conexión a su servidor. Se escribirán a mano los comandos SMTP y luego su servidor debe procesar la información recibida. 


Tips: Recuerde las pruebas realizadas en clase. Haga pruebas contra un servidor SMTP y observe la secuencia de comandos. Debe replicar el comportamiento.
