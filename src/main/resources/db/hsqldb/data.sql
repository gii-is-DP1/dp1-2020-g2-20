--USUARIOS Y AUTHORITIES--
INSERT INTO users(username,password,enabled) VALUES ('prince','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'prince','propietario');

INSERT INTO users(username,password,enabled) VALUES ('marcos','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'marcos','manager');

INSERT INTO users(username,password,enabled) VALUES ('carlos','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'carlos','camarero');

INSERT INTO users(username,password,enabled) VALUES ('carmen','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'carmen','camarero');

INSERT INTO users(username,password,enabled) VALUES ('coral','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'coral','cocinero');

INSERT INTO users(username,password,enabled) VALUES ('cobi','12345',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'cobi','cocinero');

--PERSONAS--
INSERT INTO propietario(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (1,' Prince', 'Presley', 'princePR@gmail.com', 616161616, 'prince', '12345');
INSERT INTO manager(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (1, 'Marcos', 'Martin', 'marcosMA@gmail.com', 626262626, 'marcos', '12345');
INSERT INTO camarero(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (1, 'Carlos', 'Catalan', 'carlosCA@gmail.com', 636363636, 'carlos', '12345');
INSERT INTO camarero(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (2, 'Carmen', 'Casio', 'carmenCA@gmail.com', 646464646, 'carmen', '12345');
INSERT INTO cocinero(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (1, 'Coral', 'Cohen', 'coralCO@gmail.com', 656565656, 'coral', '12345');
INSERT INTO cocinero(id,name,apellido,gmail,telefono,usuario,contrasena) VALUES (2, 'Cobi', 'Connor', 'cobiCO@gmail.com', 666666666, 'cobi', '12345');

--PROVEEDORES--
INSERT INTO proveedor(id,name,gmail,telefono) VALUES (1, 'Makro', 'foorder.dp@gmail.com', 666123456);
INSERT INTO proveedor(id,name,gmail,telefono) VALUES (2, 'CashFresh', 'foorder.dp@gmail.com',666234567);
INSERT INTO proveedor(id,name,gmail,telefono) VALUES (3, 'Frutas Manolo', 'foorder.dp@gmail.com',666345678);
INSERT INTO proveedor(id,name,gmail,telefono) VALUES (4, 'PepsiCo', 'foorder.dp@gmail.com',666345678);

--TIPOS DE PRODUCTO--
INSERT INTO tipoproducto VALUES (1, 'Carne');
INSERT INTO tipoproducto VALUES (2, 'Pescado');
INSERT INTO tipoproducto VALUES (3, 'Frutas y Verduras');
INSERT INTO tipoproducto VALUES (4, 'Lacteos');
INSERT INTO tipoproducto VALUES (5, 'Bebidas');
INSERT INTO tipoproducto VALUES (6, 'Otros');

--PRODUCTOS--
			--Makro--
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (1 , 6, 'Pan'		  , 10.0, 5.0 , 30.0, 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (2 , 1, 'Solomillo'	  , 3.0 , 1.0 , 10.0, 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (3 , 2, 'Bacalao'	  , 2.0 , 6.0 , 10.0, 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (4 , 4, 'Roquefort'	  , 1.0 , 4.0 , 4.0 , 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (5 , 6, 'Huevos'	  , 12.0, 18.0, 36.0, 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (6 , 6, 'Arroz'		  , 1.0 , 5.5 , 10.0, 1);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (7 , 1, 'Carne Picada', 1.0 , 1.5 , 4.0 , 1);
			--CashFresh--
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (8 , 4, 'Leche'		  , 6.0 , 20.0, 25.0, 2);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (9 , 4, 'Mantequilla' , 5.0 , 15.0, 20.0, 2);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (10, 6, 'Nata'		  , 4.0 , 9.0 , 15.0, 2);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (11, 6, 'Tomate Frito', 5.0 , 18.0, 20.0, 2);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (12, 6, 'Sal'		  , 0.5 , 0.8 , 3.0 , 2);
			--Frutas Manolo--
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (13, 3, 'Lechuga'	  , 5.0 , 10.0, 15.0, 3);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (14, 3, 'Cebolla'	  , 2.0 , 1.5 , 10.0, 3);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (15, 3, 'Tomate'	  , 1.0 , 2.0 , 4.0 , 3);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (16, 3, 'Patatas'	  , 1.0 , 3.0 , 5.0 , 3);
			--PepsiCo--
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (17, 5, 'Pepsi'		  , 10.0, 20.0, 50.0, 4);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (18, 5, '7UP'		  , 5.0 , 10.0, 25.0, 4);
INSERT INTO producto(id,tipo_producto,name,cantidad_minima,cantidad_actual,cantidad_maxima,proveedor_id) VALUES (19, 5, 'Lipton'	  , 5.0 , 10.0, 25.0, 4);

--PLATOS--
INSERT INTO platos(id,name,precio,disponible) VALUES (1, 'Albondigas'		  ,5.0, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (2, 'Revuelto de bacalao',7.8, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (3, 'Solomillo al roque' ,9.0, FALSE);
INSERT INTO platos(id,name,precio,disponible) VALUES (4, 'Arroz a la cubana'  ,4.5, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (5, 'Tortilla de patatas',5.0, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (6, 'Ensalada'			  ,3.0, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (7, 'Arroz con leche'	  ,4.0, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (8, 'Pepsi'			  ,1.5, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (9, '7UP'				  ,1.5, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (10,'Lipton'			  ,1.5, TRUE);
INSERT INTO platos(id,name,precio,disponible) VALUES (11,'Cesta de pan'		  ,1.0, TRUE);

--INGREDIENTES--
				--Albóndigas--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (1 , 0.5 , 7 , 1);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (2 , 0.25, 14, 1);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (3 , 1.0 , 11, 1);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (4 , 0.01, 12, 1);
				--Revuelto de bacalao--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (5 , 1.0 , 3 , 2);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (6 , 2.0 , 5 , 2);
				--Solomillo al roque--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (7 , 1.0 , 2 , 3);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (8 , 0.5 , 4 , 3);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (9 , 1.0 , 10, 3);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (10, 0.2 , 9 , 3);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (11, 0.01, 12, 3);
				--Arroz a la cubana--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (12 , 0.25, 6 , 4);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (13 , 0.5 , 11, 4);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (14 , 1.0 , 5 , 4);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (15 , 0.01, 12, 4);
				--Tortilla de patatas--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (16 , 3.0 , 5 , 5);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (17 , 0.25, 16, 5);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (18 , 0.02, 12, 5);
				--Ensalada--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (19 , 1.0 , 13, 6);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (20 , 0.3 , 15, 6);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (21 , 0.01, 12, 6);
				--Arroz con leche--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (22 , 0.5 , 6 , 7);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (23 , 2.0 , 8 , 7);
				--Bebidas y Pan--
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (24 , 1.0 , 17, 8);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (25 , 1.0 , 18, 9);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (26 , 1.0 , 19, 10);
INSERT INTO ingrediente(id,cantidad,producto_id,plato_id) VALUES (27 , 1.0 , 1 , 11);

--COMANDA--
INSERT INTO comanda(id, mesa, fecha_creado, fecha_finalizado, precio_total, camarero_id) VALUES (1,2,'2021-02-08 14:56:17','2021-02-08 15:23:02',15.5,1);
INSERT INTO comanda(id, mesa, fecha_creado, precio_total, camarero_id) VALUES (2,3,'2021-02-09 14:58:25',7.5,2);

--ESTADOS DEL PLATO--
INSERT INTO estadoplato VALUES (1, 'ENCOLA');
INSERT INTO estadoplato VALUES (2, 'ENPROCESO');
INSERT INTO estadoplato VALUES (3, 'FINALIZADO');

--PLATOS PEDIDOS--
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (1,1,3,3);
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (2,1,2,5);
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (3,1,1,8);
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (4,2,1,1);
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (5,2,1,10);
INSERT INTO platopedido(id,comanda_id,estadoplato,plato_id) VALUES (6,2,3,11);

--INGREDIENTES PEDIDOS--
				--COMANDA 1--
							--Solomillo al roque (3)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (7 , 1.0 , 1);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (8 , 0.5 , 1);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (9 , 1.0 , 1);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (10, 0.2 , 1);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (11, 0.01, 1);
							--Tortilla de patatas (5)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (16 , 3.0 , 2);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (17 , 0.25, 2);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (18 , 0.02, 2);
							--Pepsi (8)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (24 , 1.0 , 3);
				--COMANDA 2--
							--Albóndigas (1)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (1 , 0.5 , 4);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (2 , 0.25, 4);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (3 , 1.0 , 4);
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (4 , 0.01, 4);
							--Lipton (10)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (26 , 1.0 , 5);
							--Cesta de pan (11)--
INSERT INTO ingrediente_pedido(ingrediente_id,cant_pedida,pp_id) VALUES (27 , 1.0 , 6);

--PEDIDO--
INSERT INTO pedido(id,fechapedido,hallegado,proveedor_id) VALUES (1, '2021-02-04', FALSE, 1);
INSERT INTO pedido(id,fechapedido,fechaentrega,hallegado,proveedor_id) VALUES (2, '2021-02-05', '2021-02-06', TRUE, 2);

--LINEA PEDIDO--
INSERT INTO lineapedido(id,producto_id,cantidad,pedido_id) VALUES (1, 1, 10.0, 1);
INSERT INTO lineapedido(id,producto_id,cantidad,pedido_id) VALUES (2, 8, 20.0, 2);

