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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.cristianlopez.bean.DetalleReceta;
import org.cristianlopez.bean.Medicamento;
import org.cristianlopez.bean.Receta;
import org.cristianlopez.db.Conexion;
import org.cristianlopez.system.Principal;


public class DetalleRecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta> listaDetalleReceta;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Medicamento> listaMedicamento;
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodReceta;
    @FXML private ComboBox cmbCodMedicamento;
    @FXML private TableView tblDetalleRecetas;
    @FXML private TableColumn colCodDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn ColCodReceta;
    @FXML private TableColumn colCodMedicamento;
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
        cmbCodReceta.setItems(getReceta());
        cmbCodReceta.setDisable(true);
        cmbCodMedicamento.setItems(getMedicamento());
        cmbCodMedicamento.setDisable(true);
    }
    
    
    public ObservableList<DetalleReceta> getDoctor(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDetalleReceta}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                                        resultado.getString("dosis"),
                                        resultado.getInt("codigoReceta"),
                                        resultado.getInt("codigoMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleReceta = FXCollections.observableArrayList(lista);      
    }
    
    public void cargarDatos(){
        tblDetalleRecetas.setItems(getDoctor());
        colCodDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta,String>("dosis"));
        ColCodReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoReceta"));
        colCodMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoMedicamento"));
    }
    
    public void seleccionarElemento(){
        if (tblDetalleRecetas.getSelectionModel().getSelectedItem() != null) {
            txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
            txtDosis.setText(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getDosis());
            cmbCodReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbCodMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        }else{
            JOptionPane.showMessageDialog(null, "No hay ningún dato en la tabla");
        }
        
    }
    
    
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Receta(registro.getInt("codigoReceta"),
                                            registro.getDate("fechaReceta"),
                                            registro.getInt("numeroColegiado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarReceta}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){                
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                                    resultado.getDate("fechaReceta"),
                                    resultado.getInt("numeroColegiado")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                                            registro.getString("nombreMedicamento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
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
    
    // -----------------------------------------------------------------------------------------------------------------------------------
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                txtCodigoDetalleReceta.setEditable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgEliminar.setImage(new Image("org/cristianlopez/image/Cancelar.png"));
                cargarDatos();
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    
    
    
    public void guardar(){
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDetalleReceta(?,?,?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
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
                if (tblDetalleRecetas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Detalle Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDetalleReceta(?)}");
                            procedimiento.setInt(1, ((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                            procedimiento.execute();
                            listaDetalleReceta.remove(tblDetalleRecetas.getSelectionModel().getSelectedIndex());
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
    
      
      public void  editar(){
          switch(tipoDeOperacion){
              case NINGUNO:
                  if (tblDetalleRecetas.getSelectionModel().getSelectedItem() != null) {
                      btnEditar.setText("Actualizar");
                      btnReporte.setText("Cancelar");
                      btnNuevo.setDisable(true);
                      btnEliminar.setDisable(true);
                      imgReporte.setImage(new Image("/org/cristianlopez/image/Cancelar.png"));
                      activarControles();
                      txtCodigoDetalleReceta.setEditable(false);
                      cmbCodReceta.setDisable(true);
                      cmbCodMedicamento.setDisable(true);
                      tipoDeOperacion = operaciones.ACTUALIZAR;
                  }else 
                      JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                  break;
                  
              case ACTUALIZAR:
                  actualizar();
                  cmbCodReceta.setDisable(false);
                  cmbCodMedicamento.setDisable(false);
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
      
      
      public void actualizar(){
          try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDetalleReceta(?, ?, ?, ?)}");
                DetalleReceta registro = (DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem();
                registro.setDosis(txtDosis.getText());
                registro.setCodigoReceta(((Receta)cmbCodReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
                registro.setCodigoMedicamento(((Medicamento)cmbCodMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                procedimiento.setInt(1, registro.getCodigoDetalleReceta());
                procedimiento.setString(2, registro.getDosis());
                procedimiento.setInt(3, registro.getCodigoReceta());
                procedimiento.setInt(4, registro.getCodigoMedicamento());
                procedimiento.execute();
          } catch (Exception e) {
              e.printStackTrace();
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
                  imgReporte.setImage(new Image("/org/cristianlopez/image/reporteDocumento.png"));
                  tblDetalleRecetas.getSelectionModel().clearSelection();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
      }

    
    public void desactivarControles(){
        txtCodigoDetalleReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodReceta.setDisable(true);
        cmbCodMedicamento.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoDetalleReceta.setEditable(true);
        txtDosis.setEditable(true);
        cmbCodReceta.setDisable(false);
        cmbCodMedicamento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        cmbCodReceta.setValue(null);
        cmbCodMedicamento.setValue(null);
        tblDetalleRecetas.getSelectionModel().clearSelection();
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
