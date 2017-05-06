/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mulyadirestman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 *
 * @author user
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TableView<Entity> tvheader;
    @FXML
    private TableColumn<Entity, String> key;
    @FXML
    private TableColumn<Entity, String> value;
    @FXML
    private TextField turl;
    @FXML
    private Button baddvalue;
    @FXML
    private TextArea tresult;
    @FXML
    private Button bsend;
    @FXML
    private Button bremovevalue;

    ObservableList<Entity> ols = FXCollections.observableArrayList();
    int i;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        turl.setText("http://127.0.0.1:555/");
        tvheader.setEditable(true);
        sendpost();
        addvalue();
        setedittable();
        removevalue();
        System.out.println("active");
    }

    private void setedittable() {
        key.setCellFactory(TextFieldTableCell.forTableColumn());
        value.setCellFactory(TextFieldTableCell.forTableColumn());
        tvheader.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                i = newValue.intValue();
            }
        });
        key.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Entity, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Entity, String> event) {
                ols.get(i).setKey(event.getNewValue());
            }
        });

        value.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Entity, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Entity, String> event) {
                ols.get(i).setValue(event.getNewValue());
            }
        });
    }

    private void addvalue() {
        baddvalue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                ols.add(new Entity("key", "value"));
                tvheader.setItems(ols);
                key.setCellValueFactory(new PropertyValueFactory<>("key"));
                value.setCellValueFactory(new PropertyValueFactory<>("value"));
                i = -1;

            }
        });
    }

    private void removevalue() {
        bremovevalue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (i == -1) {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setHeaderText("NO ROW SELECTED");
                    al.setContentText("Please Select Row");
                } else {
                    ols.remove(i);
                    tvheader.setItems(ols);
                    key.setCellValueFactory(new PropertyValueFactory<>("key"));
                    value.setCellValueFactory(new PropertyValueFactory<>("value"));
                }
            }
        });
    }

    private void sendpost() {
        bsend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ols.size() == 0) {
                    try {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        URL url = new URL(turl.getText());
                        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                        huc.setDoInput(true);
                        huc.setDoOutput(true);
                        huc.setRequestMethod("GET");
                        huc.connect();
                        BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                        String line = "";
                        StringBuilder sb2 = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb2.append(line);
                        }
                        br.close();
                        tresult.setText(sb2.toString());
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        URL url = new URL(turl.getText());
                        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                        huc.setDoInput(true);
                        huc.setDoOutput(true);
                        huc.setRequestMethod("POST");
                        huc.connect();
                        //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream(),"UTF-8"));
                        OutputStream os = huc.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ols.size(); i++) {
                            sb.append(key.getCellData(i) + "=" + value.getCellData(i) + "&");

                        }
                        String param = sb.toString().substring(0, sb.toString().length() - 1);
                        os.write(param.getBytes());
                        BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                        String line = "";
                        StringBuilder sb2 = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb2.append(line);
                        }
                        br.close();
                        tresult.setText(sb2.toString());
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public class Entity {

        String key, value;

        public Entity(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
