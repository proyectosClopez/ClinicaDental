/*
Nombre: Cristian David Lopez Albeño
Carnet: 2018291
Creacion: 05/04/2022
Modificacion:

 */
package org.cristianlopez.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.cristianlopez.controller.CitaController;
import org.cristianlopez.controller.DetalleRecetaController;
import org.cristianlopez.controller.DoctorController;
import org.cristianlopez.controller.MenuPrincipalController;
import org.cristianlopez.controller.EspecialidadController;
import org.cristianlopez.controller.LoginController;
import org.cristianlopez.controller.MedicamentoController;
import org.cristianlopez.controller.PacienteController;
import org.cristianlopez.controller.ProgramadorController;
import org.cristianlopez.controller.RecetaController;
import org.cristianlopez.controller.UsuarioController;


/**
 *
 * @author puchi
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/cristianlopez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Clinica Albeno 2022");
        escenarioPrincipal.getIcons().add(new Image("/org/cristianlopez/image/logoClinica.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/cristianlopez/view/VistaProgramadorView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
//        ventanaLogin();
        menuPrincipal();
        escenarioPrincipal.show();
        
        
    }
    
    
    public void ventanaLogin(){
        try {
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 522, 400);
            login.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 663, 428);
            usuario.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuPrincipal(){
            try {
                MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 401, 400);
                ventanaMenu.setEscenarioPrincipal(this);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
    
    
    public void ventanaProgramador(){
        try {
            ProgramadorController vistaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 507, 540);
            vistaProgramador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPacientes(){
        
        try {
            PacienteController vistaPaciente = (PacienteController)cambiarEscena("PacientesView.fxml", 943, 535);
            vistaPaciente.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }
    
    public void ventanaEspecialidad(){
        try {
            EspecialidadController vistaEspecialidad = (EspecialidadController)cambiarEscena("EspecialidadesView.fxml", 833, 379);
            vistaEspecialidad.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamento(){
            try {
                MedicamentoController vistaDoctor = (MedicamentoController)cambiarEscena("MedicamentosView.fxml", 829, 379);
                vistaDoctor.setEscenarioPrincipal(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    
    public void ventanaDoctor(){
        try {
           DoctorController vistaDoctor = (DoctorController)cambiarEscena("DoctoresView.fxml", 943, 535);
           vistaDoctor.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaReceta(){
        try {
            RecetaController vistaReceta = (RecetaController)cambiarEscena("RecetasView.fxml", 829, 379);
            vistaReceta.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleReceta(){
        try {
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController)cambiarEscena("DetalleRecetaView.fxml", 863, 379);
            vistaDetalleReceta.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCita(){
        try {
            CitaController vistaCita = ((CitaController)cambiarEscena("CitasView.fxml", 943, 535));
            vistaCita.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
  
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
    
    
    
    
}
