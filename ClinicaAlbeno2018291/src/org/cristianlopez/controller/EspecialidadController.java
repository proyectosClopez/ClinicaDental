package org.cristianlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.cristianlopez.bean.Especialidad;
import org.cristianlopez.db.Conexion;
import org.cristianlopez.system.Principal;

public class EspecialidadController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
     public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                    resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
     
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("descripcion"));
        
    }
    
    public void seleccionarElemento(){
        if (tblEspecialidades.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEspecialidad.setText(String.valueOf( ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            txtDescripcion.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "No hay ningún dato en la tabla");
        }
    }
    
    public void guardar(){
        Especialidad registro = new Especialidad();
//        registro.setCodigoEspecialidad(Integer.parseInt(txtCodigoEspecialidad.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarEspecialidad(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaEspecialidad.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                txtCodigoEspecialidad.setEditable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgEliminar.setImage(new Image("org/cristianlopez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                if (txtDescripcion.getText() != "") {
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgEliminar.setImage(new Image("/org/cristianlopez/image/eliminarDocumento.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos(); 
                }else{
                     JOptionPane.showMessageDialog(null, "No hay ningun dato en Descripcion");
                }
                
                break;
            
        }
        
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgEliminar.setImage(new Image("/org/cristianlopez/image/eliminarDocumento.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarEspecialidad(?)}");
                            procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                            procedimiento.execute();
                            listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        limpiarControles();
                    }else if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                limpiarControles();
                break;
        }
    }
    
    public void actualizar(){
          try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarEspecialidad(?, ?)}");
                Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoEspecialidad());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    
    public void  editar(){
          switch(tipoDeOperacion){
              case NINGUNO:
                  if (tblEspecialidades.getSelectionModel().getSelectedItem() != null) {
                      btnEditar.setText("Actualizar");
                      btnReporte.setText("Cancelar");
                      btnNuevo.setDisable(true);
                      btnEliminar.setDisable(true);
                      imgReporte.setImage(new Image("/org/cristianlopez/image/Cancelar.png"));
                      activarControles();
                      txtCodigoEspecialidad.setEditable(false);
                      tipoDeOperacion = operaciones.ACTUALIZAR;
                  }else 
                      JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                  break;
                  
              case ACTUALIZAR:
                  actualizar();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnNuevo.setDisable(false);
                  btnEliminar.setDisable(false);
                  imgReporte.setImage(new Image("/org/cristianlopez/image/reporteDocumento.png"));
                  desactivarControles();
                  limpiarControles();
                  cargarDatos();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
          
      }
    
    public void reporte(){
          switch(tipoDeOperacion){
              case ACTUALIZAR:
                  desactivarControles();
                  limpiarControles();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnNuevo.setDisable(false);
                  btnEliminar.setDisable(false);
                  imgEditar.setImage(new Image("/org/cristianlopez/image/editarDocumento.png"));
                  imgReporte.setImage(new Image("/org/cristianlopez/image/reporteDocumento.png"));
                  tblEspecialidades.getSelectionModel().clearSelection();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
      }
    
    
    
    
    public void desactivarControles(){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEspecialidad.setEditable(true);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEspecialidad.clear();
        txtDescripcion.clear();
        tblEspecialidades.getSelectionModel().clearSelection();
        
    }
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
    
    
    
    
}
