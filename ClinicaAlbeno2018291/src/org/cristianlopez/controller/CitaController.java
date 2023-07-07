package org.cristianlopez.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.raven.event.EventTimePicker;
import com.raven.swing.TimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.cristianlopez.bean.Cita;
import org.cristianlopez.bean.Doctor;
//import org.cristianlopez.bean.Especialidad;
import org.cristianlopez.bean.Paciente;
import org.cristianlopez.db.Conexion;
import org.cristianlopez.system.Principal;


public class CitaController implements Initializable{
    
    private Principal escenarioPrincipal;
     private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fCita;
//    private JFXTimePicker abcd;
//    private TimePicker timePicker1;
    @FXML private GridPane grpFechaTiempo;
    @FXML private JFXTimePicker jfxHoraCita;
    @FXML private TextField txtCodigoCita;
    @FXML private ComboBox cmbNumColegiado;
    @FXML private ComboBox cmbCodPaciente;
    @FXML private TextField txtCondActual;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtHoraCita;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colCondActual;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colNumColegiado;
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
        cmbCodPaciente.setItems(getPaciente());
        cmbNumColegiado.setItems(getDoctor());
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechaTiempo.add(fCita, 4, 0);

//        jfxHoraCita.setValue(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita());

//        jfxHoraCita.setValue( Time.valueOf( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita())  );
//        jfxHoraCita.setConverter( ( (Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita() );
//        jfxHoraCita.setDisplayText( String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()) );
//        jfxHoraCita.setValue(LocalTime.ofNanoOfDay(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        
        
        
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colCondActual.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripcionCondActual"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
    }
    
    public void seleccionarElemento(){
        if (tblCitas.getSelectionModel().getSelectedItem() != null){
           txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
           fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
//           txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
           jfxHoraCita.setValue(((((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita())).toLocalTime());
           txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
           txtCondActual.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripcionCondActual());
           cmbCodPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
           cmbNumColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
           
           
//           jfxHoraCita.setValue( jfxHoraCita.setConverter((((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita())) );
//           jfxHoraCita.setValue( ( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita() ) );
        }else{
            JOptionPane.showMessageDialog(null, "No hay dato en la tabla");
        }
    }
    
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarPaciente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                    resultado.getString("nombresPaciente"),
                                    resultado.getString("apellidosPaciente"),
                                    resultado.getString("sexo"),
                                    resultado.getDate("fechaNacimiento"),
                                    resultado.getString("direccionPaciente"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getDate("fechaPrimeraVisita")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                            registro.getString("nombresPaciente"),
                                            registro.getString("apellidosPaciente"),
                                            registro.getString("sexo"),
                                            registro.getDate("fechaNacimiento"),
                                            registro.getString("direccionPaciente"),
                                            registro.getString("telefonoPersonal"),
                                            registro.getDate("fechaPrimeraVisita"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDoctor}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                        resultado.getString("nombresDoctor"),
                                        resultado.getString("apellidosDoctor"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);      
    }
    
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Doctor(registro.getInt("numeroColegiado"),
                                            registro.getString("nombresDoctor"),
                                            registro.getString("apellidosDoctor"),
                                            registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEspecialidad"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    
    public ObservableList<Cita> getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCita}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Cita(resultado.getInt("codigoCita"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getTime("horaCita"),
                                    resultado.getString("tratamiento"),
                                    resultado.getString("descripcionCondActual"),
                                    resultado.getInt("codigoPaciente"),
                                    resultado.getInt("numeroColegiado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
    }
    
    
    
    
    
    
    // -----------------------------------------------------------------------------------------------------------------------------------
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                txtCodigoCita.setEditable(false);
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
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita(java.sql.Time.valueOf(jfxHoraCita.getValue()));
        registro.setDescripcionCondActual(txtCondActual.getText());
        registro.setTratamiento(txtTratamiento.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{    
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCita(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripcionCondActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
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
                if (tblCitas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCita(?)}");
                            procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                            procedimiento.execute();
                            listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
                  if (tblCitas.getSelectionModel().getSelectedItem() != null) {
                      btnEditar.setText("Actualizar");
                      btnReporte.setText("Cancelar");
                      btnNuevo.setDisable(true);
                      btnEliminar.setDisable(true);
                      imgReporte.setImage(new Image("/org/cristianlopez/image/Cancelar.png"));
                      activarControles();
                      txtCodigoCita.setEditable(false);
                      cmbCodPaciente.setDisable(true);
                      cmbNumColegiado.setDisable(true);
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
      
      
      public void actualizar(){
          try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCita(?, ?, ?, ?, ?, ?, ?)}");
                Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
                registro.setFechaCita(fCita.getSelectedDate());
                registro.setHoraCita(java.sql.Time.valueOf(jfxHoraCita.getValue()));
                registro.setTratamiento(txtTratamiento.getText());
                registro.setDescripcionCondActual(txtCondActual.getText());
                registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                registro.setNumeroColegiado(((Doctor)cmbNumColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                procedimiento.setInt(1, registro.getCodigoCita());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
                procedimiento.setTime(3, registro.getHoraCita());
                procedimiento.setString(4, registro.getTratamiento());
                procedimiento.setString(5, registro.getDescripcionCondActual());
                procedimiento.setInt(6, registro.getCodigoPaciente());
                procedimiento.setInt(7, registro.getNumeroColegiado());
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
                  tblCitas.getSelectionModel().clearSelection();
                  tipoDeOperacion = operaciones.NINGUNO;
                  break;
          }
      }

 
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        jfxHoraCita.setDisable(true);
//        txtHoraCita.setEditable(false);
        cmbCodPaciente.setDisable(true);
        txtCondActual.setEditable(false);
        txtTratamiento.setEditable(false);
        cmbNumColegiado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoCita.setEditable(true);
        jfxHoraCita.setDisable(false);
//        txtHoraCita.setEditable(true);
        cmbCodPaciente.setDisable(false);
        txtCondActual.setEditable(true);
        txtTratamiento.setEditable(true);
        cmbNumColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCita.clear();
        jfxHoraCita.setValue(null);
//        txtHoraCita.clear();
        cmbCodPaciente.setValue(null);
        txtCondActual.clear();
        txtTratamiento.clear();
        cmbNumColegiado.setValue(null);
        tblCitas.getSelectionModel().clearSelection();
    }
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    
    
}
