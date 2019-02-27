import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @class GUI
 * @brief És la inerfície gràfica amb la qual l'usuari interactuarà per fer servir el programa (Graphical User Interface).
 * @details Quan executem el programa se'ns obre una finestra que anomenarem FINESTRA INICI.
 *				-FINESTRA INICI conté:	- Selector de fitxer.txt per seleccionar el joc de proves.
 *										  Una vegada seleccionat el fitxer.txt el programa el valida i ens mostra una finestra
 *										  amb un missatge d'error si no s'ha pogut obrir. Altrament, ens mostra missatge de correcte
 *										  i podem passar a fer servir el següent:
 *										- Botó executa Backtracking. Aquest botó ens calcularà les rutes desitjades segons li haguem expressat
 *										  a la comanda. Si no hi ha hagut cap error ens portarà a una finestra que anomenarem FINESTRA BACKTRACKING.
 *										- Botó executa Voraç. Aquest botó ens calcularà les rutes desitjades segons li haguem expressat a la comanda.
 *										  Si no hi ha hagut cap error ens portarà a una finestra que anomenarem FINESTRA VORAÇ.
 *				-FINESTRA BACKTRACKING conté:	- Segons les rutes que li haguem demanat ens mostrarà 2, 4 o 6 botons (TXT i KML per cada ruta barata, satisfactòria o curta).
 *												- Botó tornar que ens retorna a la FINESTRA INICI.
 *				-FINESTRA VORAÇ conté:	- Segons les rutes que li haguem demanat ens mostrarà 2, 4 o 6 botons (TXT i KML per cada ruta barata, satisfactòria o curta).
 *										- Botó tornar que ens retorna a la FINESTRA INICI.
 * 				-BOTONS TXT i KML: - Cada un d'aquests botons ens obriran una finestra de selecció de directori on guardar el fitxer que ens especifica el botó (barata, satisfactòria o curta) respectivament i el seu nom.
 *
 * @author Adrià Orellana Cruañas
 * @version 2017.4.5
 */

public class GUI extends Application {

    private DadesAgencia dades;
    private Scene escenaINICI;
    private Scene escenaBACK;
    private Scene escenaVORAC;
    private Ruta b;
    private Ruta c;
    private Ruta s;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //CREEM EL SELECTOR DE FITXER D'ENTRADA
        FileChooser fileChooser = new FileChooser();
        Button botoSeleccio = new Button("Selecciona fitxer entrada");
        botoSeleccio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File fitxerEntrada = fileChooser.showOpenDialog(primaryStage);
                if(fitxerEntrada != null){
                    try {
                        dades = new DadesAgencia();
                        dades.llegirDadesFitxer(fitxerEntrada);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Dades d'entrada");
                        alert.setHeaderText("Les dades s'han entrat correctament");
                        alert.setContentText("Ja pots executar el Backtracking o el Voraç");
                        alert.showAndWait();
                    } catch (FileNotFoundException e) {
                        System.err.println("Fitxer incorrecte!");
                    }
                }
            }
        });



        //CREEM EL PANELL
        GridPane elementsGUI = new GridPane();
        elementsGUI.setAlignment(Pos.CENTER);
        elementsGUI.setVgap(10); elementsGUI.setHgap(10);

        //CREEM TEXT DE BENVINGUDA
        Text scenetitle = new Text("Benvinguts a ProActive Travel.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        elementsGUI.add(scenetitle, 0, 0, 1, 2);

        // CREEEM TEXT PER ENTRAR FITXER
        Label userName = new Label("Selecciona el fitxer d'entrada de dades:");
        userName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        elementsGUI.add(userName, 0, 5);

        // AFEGIM BOTÓ DE SELECCIO DE FITXER A PANELL.
        elementsGUI.add(botoSeleccio,0,6);

        // AFEGIM LOGO
        Image imatgeLogo = new Image("logo.jpg");
        ImageView logo = new ImageView();
        logo.setImage(imatgeLogo);
        logo.setFitHeight(75);
        logo.setPreserveRatio(true);
        elementsGUI.add(logo,5,0);

        //CREEM ELS DOS BOTONS D'EXECUCIÓ D'ALGORISMES
        Button executaBack = new Button("Executa Backtracking");
        Button executaVorac = new Button("Executa Voraç");
        GridPane botons = new GridPane();
        botons.setAlignment(Pos.CENTER);
        botons.setHgap(10); botons.setVgap(10);
        botons.add(executaBack,0,0);
        botons.add(executaVorac,7,0);
        elementsGUI.add(botons,0,15);

        ByteArrayOutputStream bar = new ByteArrayOutputStream();
        PrintStream barat = new PrintStream(bar);
        ByteArrayOutputStream sat = new ByteArrayOutputStream();
        PrintStream satisf = new PrintStream(sat);
        ByteArrayOutputStream cur = new ByteArrayOutputStream();
        PrintStream curt = new PrintStream(cur);
        Button botoBarataKML = new Button("Guardar arxiu KML (Barata)");
        Button botoBarataTXT = new Button("Guardar arxiu Sortida TXT (Barata)");
        Button botoCurtaKML = new Button("Guardar arxiu KML (Curta)");
        Button botoCurtaTXT = new Button("Guardar arxiu Sortida TXT (Curta)");
        Button botoSatisfactoriaKML = new Button("Guardar arxiu KML (Satisfact)");
        Button botoSatisfactoriaTXT = new Button("Guardar arxiu Sortida TXT(Satisfact)");
        PrintStream old = System.out;
        ByteArrayOutputStream barKML = new ByteArrayOutputStream();
        PrintStream baratKML = new PrintStream(barKML);
        ByteArrayOutputStream satKML = new ByteArrayOutputStream();
        PrintStream satisfKML = new PrintStream(satKML);
        ByteArrayOutputStream curKML = new ByteArrayOutputStream();
        PrintStream curtKML = new PrintStream(curKML);







        //BOTÓ EXECUTA BACKTRACKING
        executaBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CalculadorRutes calculador = new CalculadorRutes(dades.obtComanda());
                GridPane elementsBACK = new GridPane();
                elementsBACK.setAlignment(Pos.CENTER);
                escenaBACK = new Scene(elementsBACK,500,450);
                primaryStage.setScene(escenaBACK);

                GridPane rutes = new GridPane();
                rutes.setAlignment(Pos.CENTER);

                if(dades.obtComanda().esVolRuta('b')){
                    b = calculador.camiMinim('b');
                    elementsBACK.add(botoBarataKML,0,0);
                    elementsBACK.add(botoBarataTXT,5,0);
                    System.setOut(barat);
                    System.out.println("RUTA BARATA:");
                    if(b != null) {b.mostraPantalla();}
                    else System.out.println("No s'ha trobat cap ruta");
                    System.setOut(old);
                }
                if(dades.obtComanda().esVolRuta('s')){
                    s = calculador.camiMinim('s');
                    elementsBACK.add(botoSatisfactoriaKML,0,1);
                    elementsBACK.add(botoSatisfactoriaTXT,5,1);
                    System.setOut(satisf);
                    System.out.println("RUTA SATISFACTORIA:");
                    if(s != null) s.mostraPantalla();
                    System.setOut(old);
                }
                if(dades.obtComanda().esVolRuta('c')){
                    c = calculador.camiMinim('c');
                    elementsBACK.add(botoCurtaKML,0,2);
                    elementsBACK.add(botoCurtaTXT,5,2);
                    System.setOut(curt);
                    System.out.println("RUTA CURTA:");
                    if(c != null) c.mostraPantalla();
                    System.setOut(old);
                }

                //BOTO TORNAR
                Button tornar = new Button("Tornar a l'inici");
                tornar.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        primaryStage.setScene(escenaINICI);
                    }
                });
                elementsBACK.add(tornar,0,35);

            }
        });



        //BOTO EXECUTA VORAÇ
        executaVorac.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CalculadorRutes calculador = new CalculadorRutes(dades.obtComanda());
                GridPane elementsVORAC = new GridPane();
                elementsVORAC.setAlignment(Pos.CENTER);
                escenaVORAC = new Scene(elementsVORAC,500,450);
                primaryStage.setScene(escenaVORAC);

                GridPane rutes = new GridPane();
                rutes.setAlignment(Pos.CENTER);

                if(dades.obtComanda().esVolRuta('b')){
                    b = calculador.Vorac();
                    elementsVORAC.add(botoBarataKML,0,0);
                    elementsVORAC.add(botoBarataTXT,5,0);
                    System.setOut(barat);
                    System.out.println("RUTA BARATA:");
                    if(b != null) {b.mostraPantalla();}
                    else System.out.println("No s'ha trobat cap ruta");
                    System.setOut(old);
                }
                if(dades.obtComanda().esVolRuta('s')){
                    s = calculador.Vorac();
                    elementsVORAC.add(botoSatisfactoriaKML,0,1);
                    elementsVORAC.add(botoSatisfactoriaTXT,5,1);
                    System.setOut(satisf);
                    System.out.println("RUTA SATISFACTORIA:");
                    if(s != null) s.mostraPantalla();
                    System.setOut(old);
                }
                if(dades.obtComanda().esVolRuta('c')){
                    c = calculador.Vorac();
                    elementsVORAC.add(botoCurtaKML,0,2);
                    elementsVORAC.add(botoCurtaTXT,5,2);
                    System.setOut(curt);
                    System.out.println("RUTA CURTA:");
                    if(c != null) c.mostraPantalla();
                    System.setOut(old);
                }

                //BOTO TORNAR
                Button tornar = new Button("Tornar a l'inici");
                tornar.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        primaryStage.setScene(escenaINICI);
                    }
                });
                elementsVORAC.add(tornar,0,35);
            }
        });


        escenaINICI = new Scene(elementsGUI, 500, 450);
        primaryStage.setTitle("ProActive Travel");
        primaryStage.setScene(escenaINICI);
        primaryStage.show();



		//BOTÓ BARATA TXT
        botoBarataTXT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        FileWriter fileWriter = null;
                        fileWriter = new FileWriter(file);
                        fileWriter.write(bar.toString());
                        fileWriter.close();
                    } 
					catch (IOException ex) {

                    }
                }
            }
        });
		
		//BOTÓ SATISFACTÒRIA TXT
        botoSatisfactoriaTXT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        FileWriter fileWriter = null;
                        fileWriter = new FileWriter(file);
                        fileWriter.write(sat.toString());
                        fileWriter.close();
                    } 
					catch (IOException ex) {

                    }
                }
            }
        });

		//BOTÓ CURTA TXT
        botoCurtaTXT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        FileWriter fileWriter = null;
                        fileWriter = new FileWriter(file);
                        fileWriter.write(cur.toString());
                        fileWriter.close();
                    } 
					catch (IOException ex) {

                    }
                }
            }
        });
	
		//BOTÓ BARATA KML
        botoBarataKML.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KML files (*.kml)", "*.kml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        b.generarKML(file);
                    } 
					catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

		//BOTÓ SATISFACTÒRIA KML
        botoSatisfactoriaKML.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KML files (*.kml)", "*.kml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        s.generarKML(file);
                    } 
					catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
		
		//BOTÓ CURTA KML
        botoCurtaKML.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KML files (*.kml)", "*.kml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    try {
                        c.generarKML(file);
                    } 
					catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }






    public static void main(String[] args) {
        launch(args);
    }
}



