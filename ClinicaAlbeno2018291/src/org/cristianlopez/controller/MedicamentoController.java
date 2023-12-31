package org.cristianlopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.cristianlopez.bean.Medicamento;
import org.cristianlopez.db.Conexion;
import org.cristianlopez.report.GenerarReporte;
import org.cristianlopez.system.Principal;


public class MedicamentoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento> listaMedicamento;
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
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
    
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarMedicamento}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                    resultado.getString("nombreMedicamento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombreMedicamento"));
    }
    
    
    public void seleccionarElemento(){
        if (tblMedicamentos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoMedicamento.setText( String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtNombreMedicamento.setText(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());
        }else{
            JOptionPane.showMessageDialog(null, "No hay ningún dato en la tabla");
        }
    }
    
    
    public void guardar(){
        Medicamento registro = new Medicamento();
//        registro.setCodigoMedicamento(Integer.parseInt(txtCodigoMedicamento.getText()));
        registro.setNombreMedicamento(txtNombreMedicamento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, registro.getNombreMedicamento());
            procedimiento.execute();
            listaMedicamento.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                txtCodigoMedicamento.setEditable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgEliminar.setImage(new Image("org/cristianlopez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
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
                if (tblMedicamentos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Medicamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarMedicamento(?)}");
                            procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                            procedimiento.execute();
                            listaMedicamento.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
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
                break;
        }
    }
    
    public void actualizar(){
          try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarMedicamento(?, ?)}");
                Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
                registro.setNombreMedicamento(txtNombreMedicamento.getText());
                procedimiento.setInt(1, registro.getCodigoMedicamento());
                procedimiento.setString(2, registro.getNombreMedicamento());
                procedimiento.execute();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    
    public void  editar(){
          switch(tipoDeOperacion){
              case NINGUNO:
                  if (tblMedicamentos.getSelectionModel().getSelectedItem() != null) {
                      activarControles();
                      btnEditar.setText("Actualizar");
                      btnReporte.setText("Cancelar");
                      btnNuevo.setDisable(true);
                      btnEliminar.setDisable(true);
                      imgReporte.setImage(new Image("/org/cristianlopez/image/Cancelar.png"));
                      txtCodigoMedicamento.setEditable(false);
                      tipoDeOperacion = operaciones.ACTUALIZAR;
                  }else 
                      JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                  break;
                  
              case ACTUALIZAR:
                  actualizar();
                  desactivarControles();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnNuevo.setDisable(false);
                  btnEliminar.setDisable(false);
                  imgReporte.setImage(new Image("/org/cristianlopez/image/reporteDocumento.png")); 
                  limpiarControles();
                  cargarDatos();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
          
      }
    
    public void reporte(){
          switch(tipoDeOperacion){
              case NINGUNO:
                  imprimirReporte();
                  break;
              case ACTUALIZAR:
                  desactivarControles();
                  limpiarControles();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnNuevo.setDisable(false);
                  btnEliminar.setDisable(false);
                  imgEditar.setImage(new Image("/org/cristianlopez/image/editarDocumento.png"));
                  imgReporte.setImage(new Image("/org/cristianlopez/image/reporteDocumento.png"));
                  tblMedicamentos.getSelectionModel().clearSelection();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
      }
    
    public void imprimirReporte(){
          Map parametros = new HashMap();
          parametros.put("codigoMedicamento", null);
          GenerarReporte.mostrarReporte("ReporteMedicamento.jasper", "Reporte de Medicamentos", parametros);
      }
    
   
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoMedicamento.setEditable(true);
        txtNombreMedicamento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
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
