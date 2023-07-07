package org.cristianlopez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.cristianlopez.system.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
  
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaDoctor(){
        escenarioPrincipal.ventanaDoctor();
    }
    
    public void ventanaMedicamento(){
        escenarioPrincipal.ventanaMedicamento();
    }
    
    public void ventanaReceta(){
        escenarioPrincipal.ventanaReceta();
    }
    
    public void ventanaDetalleReceta(){
        escenarioPrincipal.ventanaDetalleReceta();
    }
    
    public void ventanaCita(){
        escenarioPrincipal.ventanaCita();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    
        
    }