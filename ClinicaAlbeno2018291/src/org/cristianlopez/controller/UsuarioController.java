package org.cristianlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import net.sf.jasperreports.export.PropertiesNoDefaultsConfigurationFactory;
import org.cristianlopez.bean.Usuario;
import org.cristianlopez.db.Conexion;
import org.cristianlopez.system.Principal;


public class UsuarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUser;
    @FXML private TextField txtContrasena;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
        
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/cristianlopez/image/Guardar.png"));
                imgEliminar.setImage(new Image("org/cristianlopez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/cristianlopez/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/cristianlopez/image/borrar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUser.getText());
        registro.setContrasena(txtContrasena.getText());
       
                try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?)}");
                    procedimiento.setString(1, registro.getNombreUsuario());
                    procedimiento.setString(2, registro.getApellidoUsuario());
                    procedimiento.setString(3, registro.getUsuarioLogin());
                    procedimiento.setString(4, registro.getContrasena());
                    procedimiento.execute();
                JOptionPane.showMessageDialog(null, "Creado Correctamente");
                ventanaLogin(); 
                } catch (Exception e) {
                    e.printStackTrace();
                } 
        
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/cristianlopez/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/cristianlopez/image/borrar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    
    
    
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUser.setEditable(false);
        txtContrasena.setEditable(false);
        btnEliminar.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUser.setEditable(true);
        txtContrasena.setEditable(true);
        btnEliminar.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUser.clear();
        txtContrasena.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
    
    
    
}
