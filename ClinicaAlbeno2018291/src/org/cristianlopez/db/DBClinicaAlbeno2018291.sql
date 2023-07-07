Drop database if exists DBClinicaAlbeno2018291;
create database DBClinicaAlbeno2018291;

use DBClinicaAlbeno2018291;

Create table Pacientes(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8),
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente (codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
); 	

Create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades(codigoEspecialidad)
);

Create table Recetas(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores(numeroColegiado)
);

Create table Citas(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(150),
    descripcionCondActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetaleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Recetas foreign key (codigoReceta)
		references Recetas(codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos(codigoMedicamento)
);

-- --------------------------------------------------------------------------------------------
-- --------------------------procedimientos almacenado ----------------------------------------

-- --------------------------------- PACIENTES ------------------------------------------------
-- --------------------------------- Agregar Pacientes ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50), in apellidosPaciente varchar(50), 
		in sexo char, in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
		BEGIN
			INSERT INTO Pacientes(codigoPaciente, nombresPaciente, apellidosPaciente, sexo, 
				fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita) 
			VALUES (codigoPaciente, nombresPaciente, apellidosPaciente, upper(sexo), 
				fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
		END $$	
DELIMITER ;

CALL sp_AgregarPaciente(1001, 'Pedro Manuel', 'Armas Chang', 'm', '1982-08-17', 'Zona 1 Mixco', '45789634', now());
CALL sp_AgregarPaciente(1002, 'Jong Jorge', 'Perez Alen', 'm', '1982-07-19', 'Zona 2', '12345678', now());

-- --------------------------------- Listar Pacientes ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_ListarPaciente()
		BEGIN
			Select
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo, fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
            from Pacientes P;
		END $$	
DELIMITER ;

CALL sp_ListarPaciente();

-- --------------------------------- Buscar Pacientes ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_BuscarPaciente(in codPaciente int)
		BEGIN
			Select
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo, fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
            from Pacientes P where codigoPaciente = codPaciente;
		END $$	
DELIMITER ;

CALL sp_BuscarPaciente(1001);
-- --------------------------------- Eliminar Pacientes ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarPaciente(in codPaciente int)
		BEGIN
			Delete from Pacientes 
				where codigoPaciente = codPaciente;
		END $$	
DELIMITER ;

CALL sp_EliminarPaciente(1001);
CALL sp_ListarPaciente();
-- --------------------------------- Editar Pacientes ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarPaciente(in codPaciente int, in nomPaciente varchar(50), in apPaciente varchar(50), 
		in sx char, in fechaNac date, in dirPaciente varchar(100), in telPersonal varchar(8), in fechaPV date)
		BEGIN
			Update Pacientes P
            set P.nombresPaciente = nomPaciente, 
            P.apellidosPaciente = apPaciente, 
            P.sexo = upper(sx), 
            P.fechaNacimiento = fechaNac, 
            P.direccionPaciente = dirPaciente, 
            P.telefonoPersonal = telPersonal, 
            P.fechaPrimeraVisita = fechaPV
            where codigoPaciente = codPaciente;
		END $$	
DELIMITER ;

-- CALL sp_EditarPaciente();
-- CALL sp_ListarPacientes();

-- --------------------------------- ESPECIALIDADES ------------------------------------------------
-- --------------------------------- Agregar Especialidades ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarEspecialidad(in descripcion varchar(100))
		BEGIN
			INSERT INTO Especialidades(descripcion) 
			VALUES (descripcion);
		END $$	
DELIMITER ;

CALL sp_AgregarEspecialidad('Especialidad1');
CALL sp_AgregarEspecialidad('Especialidad2');
-- --------------------------------- Listar Especialidades ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_ListarEspecialidad()
		BEGIN
			Select
            E.codigoEspecialidad,
            E.descripcion
            from Especialidades E;
		END $$	
DELIMITER ;

CALL sp_ListarEspecialidad();
-- --------------------------------- Buscar Especialidades ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_BuscarEspecialidad(in codEspecialidad int)
		BEGIN
			Select
            E.codigoEspecialidad,
            E.descripcion
            from Especialidades E where codigoEspecialidad = codigoEspecialidad;
		END $$	
DELIMITER ;

CALL sp_BuscarEspecialidad(1);
-- --------------------------------- Eliminar Especialidades ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarEspecialidad(in codEspecialidad int)
		BEGIN
			Delete from Especialidades 
				where codigoEspecialidad = codEspecialidad;
		END $$	
DELIMITER ;

CALL sp_EliminarEspecialidad(1);
CALL sp_ListarPaciente();
-- --------------------------------- Editar Especialidades ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarEspecialidad(in codEspecialidad int, in descr varchar(100))
		BEGIN
			Update Especialidades E
            set
				E.descripcion = descr
            where codigoEspecialidad = codEspecialidad;
		END $$	
DELIMITER ;

CALL sp_EditarEspecialidad(2, 'Especialidad3');

-- --------------------------------- MEDICAMENTOS ------------------------------------------------
-- --------------------------------- Agregar Medicamentos ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarMedicamento(in nombreMedicamento varchar(100))
		BEGIN
			INSERT INTO Medicamentos(nombreMedicamento) 
			VALUES (nombreMedicamento);
		END $$	
DELIMITER ;

CALL sp_AgregarMedicamento('Argotamina');
CALL sp_AgregarMedicamento('Acerca');
-- --------------------------------- Listar Medicamentos ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_ListarMedicamento()
		BEGIN
			Select
            M.codigoMedicamento,
            M.nombreMedicamento
            from Medicamentos M;
		END $$	
DELIMITER ;

CALL sp_ListarMedicamento();
-- --------------------------------- Buscar Medicamentos ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_BuscarMedicamento(in codMedicamento int)
		BEGIN
			Select
            M.codigoMedicamento,
            M.nombreMedicamento
            from Medicamentos M where codigoMedicamento = codMedicamento;
		END $$	
DELIMITER ;

CALL sp_BuscarMedicamento(1);
-- --------------------------------- Eliminar Medicamentos ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarMedicamento(in codMedicamento int)
		BEGIN
			Delete from Medicamentos 
				where codigoMedicamento = codMedicamento;
		END $$	
DELIMITER ;

CALL sp_EliminarMedicamento(1);
CALL sp_ListarMedicamento();
-- --------------------------------- Editar Medicamentos ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarMedicamento(in codMedicamento int, in nomMedicamento varchar(100))
		BEGIN
			Update Medicamentos M
            set
				M.nombreMedicamento = nomMedicamento
            where codigoMedicamento = codMedicamento;
		END $$	
DELIMITER ;

CALL sp_EditarMedicamento(2, 'Paracetamol');



-- --------------------------------- DOCTORES ------------------------------------------------
-- --------------------------------- Agregar Doctores ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarDoctor(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50), 
		in telefonoContacto varchar(8), in codigoEspecialidad int)
		BEGIN
			INSERT INTO Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad) 
			VALUES (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
		END $$	
DELIMITER ;

CALL sp_AgregarDoctor(1, 'Alfredo Manases', 'Bulero Valen', '12345678', 2);
CALL sp_AgregarDoctor(2, 'Asta Gregorio', 'Toro Negro', '23456789', 2);
-- --------------------------------- Listar Doctores ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_ListarDoctor()
		BEGIN
			Select
            D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
            from Doctores D;
		END $$	
DELIMITER ;

CALL sp_ListarDoctor();
-- --------------------------------- Buscar Doctores ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_BuscarDoctor(in numColegiado int)
		BEGIN
			Select
            D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
            from Doctores D where numeroColegiado = numColegiado;
		END $$	
DELIMITER ;

CALL sp_BuscarDoctor(1);
-- --------------------------------- Eliminar Doctores ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarDoctor(in numColegiado int)
		BEGIN
			Delete from Doctores 
				where numeroColegiado = numColegiado;
		END $$	
DELIMITER ;

CALL sp_EliminarDoctor(1);
CALL sp_ListarDoctor();
-- --------------------------------- Editar Doctores ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarDoctor(in numColegiado int, in nombreDoc varchar(50), in apellidosDoc varchar(50), 
		in telContacto varchar(8), in codEspecialidad int)
		BEGIN
			Update Doctores D
            set
                D.nombresDoctor = nombreDoc,
				D.apellidosDoctor = apellidosDoc,
				D.telefonoContacto = telContacto,
				D.codigoEspecialidad = codEspecialidad
            where numeroColegiado = numColegiado;
		END $$	
DELIMITER ;

CALL sp_EditarDoctor(2, 'Asta Yuno', 'Amanecer Dorado', '23456789', 2);


-- --------------------------------- RECETAS ------------------------------------------------
-- --------------------------------- Agregar Recetas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarReceta(in fechaReceta date, in numeroColegiado int)
		BEGIN
			INSERT INTO Recetas(fechaReceta, numeroColegiado) 
			VALUES (fechaReceta, numeroColegiado);
		END $$	
DELIMITER ;

CALL sp_AgregarReceta('2022-02-03', 2);
CALL sp_AgregarReceta('2022-02-04', 2);
-- --------------------------------- Listar Recetas ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_ListarReceta()
		BEGIN
			Select
				R.codigoReceta, 
                R.fechaReceta, 
                R.numeroColegiado
            from Recetas R;
		END $$	
DELIMITER ;

CALL sp_ListarReceta();
-- --------------------------------- Buscar Recetas ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_BuscarReceta(in codReceta int)
		BEGIN
			Select
				R.codigoReceta, 
                R.fechaReceta, 
                R.numeroColegiado
            from Recetas R where codigoReceta = codReceta;
		END $$	
DELIMITER ;

CALL sp_BuscarReceta(1);
-- --------------------------------- Eliminar Recetas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarReceta(in codReceta int)
		BEGIN
			Delete from Recetas 
				where codigoReceta = codReceta;
		END $$	
DELIMITER ;

CALL sp_EliminarReceta(1);
CALL sp_ListarReceta();
-- --------------------------------- Editar Recetas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarReceta(in codReceta int, in fecReceta date, in numColegiado int)
		BEGIN
			Update Recetas R
            set
                R.fechaReceta = fecReceta,
                R.numeroColegiado = numColegiado
            where codigoReceta = codReceta;
		END $$	
DELIMITER ;

CALL sp_EditarReceta(2, '2022-03-04', 2);

da
-- --------------------------------- CITAS ------------------------------------------------
-- --------------------------------- Agregar Citas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarCita(in fechaCita date, in horaCita time, in tratamiento varchar(150), in descripcionCondActual varchar(255), in codigoPaciente int, in numeroColegiado int)
		BEGIN
			INSERT INTO Citas(fechaCita, horaCita, tratamiento, descripcionCondActual, codigoPaciente, numeroColegiado) 
			VALUES (fechaCita, horaCita, tratamiento, descripcionCondActual, codigoPaciente, numeroColegiado);
		END $$	
DELIMITER ;

CALL sp_AgregarCita('2022-02-03', '38:59:59', 'Tomar paracetamol', 'mal', 1002, 2);
CALL sp_AgregarCita('2022-03-04', '28:59:59', 'Tomar acetaminofen', 'medianamente mal', 1002, 2);
-- --------------------------------- Listar Citas ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_ListarCita()
		BEGIN
			Select
				C.codigoCita, 
                C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.codigoPaciente, 
                C.descripcionCondActual, 
                C.numeroColegiado
            from Citas C;
		END $$	
DELIMITER ;

CALL sp_ListarCita();
-- --------------------------------- Buscar Citas ------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_BuscarCita(in codCita int)
		BEGIN
			Select
				C.codigoCita, 
                C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.codigoPaciente, 
                C.descripcionCondActual, 
                C.numeroColegiado
            from Citas C where codigoCita = codCita;
		END $$	
DELIMITER ;

CALL sp_BuscarCita(1);
-- --------------------------------- Eliminar Citas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarCita(in codCita int)
		BEGIN
			Delete from Citas 
				where codigoCita = codCita;
		END $$	
DELIMITER ;

CALL sp_EliminarCita(1);
CALL sp_ListarCita();
-- --------------------------------- Editar Citas ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarCita(in codCita int, in fecCita date, in hoCita time, in trata varchar(150), in desCondActual varchar(255), in codPaciente int, in numColegiado int)
		BEGIN
			Update Citas C
            set
                C.fechaCita = fecCita, 
                C.horaCita = hoCita, 
                C.tratamiento = trata, 
                C.descripcionCondActual = desCondActual,
                C.codigoPaciente = codPaciente,
                C.numeroColegiado = numColegiado 
			where codigoCita = codCita;
		END $$	
DELIMITER ;

CALL sp_EditarCita(2, '2022-05-04', '21:49:15', 'Tomar acetaminofen', 'bien', 1002, 2);



-- --------------------------------- DETALLERECETA ------------------------------------------------
-- --------------------------------- Agregar DetalleReceta ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		BEGIN
			INSERT INTO DetalleReceta(dosis, codigoReceta, codigoMedicamento) 
			VALUES (dosis, codigoReceta, codigoMedicamento);
		END $$	
DELIMITER ;

CALL sp_AgregarDetalleReceta('1 pastilla al dia', 2, 2);
CALL sp_AgregarDetalleReceta('1 pastilla cada 12 horas', 2, 2);
-- --------------------------------- Listar DetalleReceta ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_ListarDetalleReceta()
		BEGIN
			Select
				DC.codigoDetalleReceta, 
                DC.dosis, 
                DC.codigoReceta, 
                DC.codigoMedicamento
            from DetalleReceta DC;
		END $$	
DELIMITER ;

CALL sp_ListarDetalleReceta();
-- --------------------------------- Buscar DetalleReceta ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_BuscarDetalleReceta(in codDetalleReceta int)
		BEGIN
			Select
				DC.codigoDetalleReceta, 
                DC.dosis, 
                DC.codigoReceta, 
                DC.codigoMedicamento
            from DetalleReceta DC where codigoDetalleReceta = codDetalleReceta;
		END $$	
DELIMITER ;

CALL sp_BuscarDetalleReceta(1);
-- --------------------------------- Eliminar DetalleReceta ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EliminarDetalleReceta(in codDetalleReceta int)
		BEGIN
			Delete from DetalleReceta 
				where codigoDetalleReceta = codDetalleReceta;
		END $$	
DELIMITER ;

CALL sp_EliminarDetalleReceta(1);
CALL sp_ListarDetalleReceta();
-- --------------------------------- Editar DetalleReceta ------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_EditarDetalleReceta(in codDetalleReceta int, in dsi varchar(100), in codReceta int, in codMedicamento int)
		BEGIN
			Update DetalleReceta DC
            set
                DC.dosis = dsi,
                DC.codigoReceta = codReceta,
                DC.codigoMedicamento = codMedicamento
			where codigoDetalleReceta = codDetalleReceta;
		END $$	
DELIMITER ;

CALL sp_EditarDetalleReceta(2, '1 pastilla cada 6 horas', 2, 2);


-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';



Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

-- ------------------------------------------------------------ agregar usuario ------------------------------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
		BEGIN
			insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
            values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
		END$$	
DELIMITER ;

-- ------------------------------------------------------------ listar usuario ------------------------------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_ListarUsuario()
		BEGIN
			Select
				U.codigoUsuario, 
                U.nombreUsuario, 
                U.apellidoUsuario, 
                U.usuarioLogin, 
                U.contrasena
                from Usuario U;
		END$$	
DELIMITER ;

call sp_AgregarUsuario('Cristian', 'Lopez', 'clopez', '@123');
call sp_ListarUsuario();

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

