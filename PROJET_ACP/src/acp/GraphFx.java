package acp;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/** @see https://stackoverflow.com/a/71362723/230513 */
public class GraphFx extends Application {
    private static final int PADDING = 150;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void start(Stage stage) {
        stage.setTitle("Analyse en composants principales(ACP)");
        //Charger le fichier .txt du dataset
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger le dataset");
        Label label = new Label("Choisissez un fichier:");
        Button singleButton = new Button("Parcourir");
        singleButton.setOnAction((ActionEvent t) -> {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
          //Déclarer les actions afin de visualiser les données
            try {
				double [][] data=Acp.uploadDataFx(fileChooser.showOpenDialog(stage));
				Acp dataFX=new Acp(data);
				//Initialisation de la fenêtre à afficher
		        stage.setTitle("Visualisation ACP");
		        //Definition des axes de données initiales
		        final NumberAxis xAxis = new NumberAxis();
		        final NumberAxis yAxis = new NumberAxis();
		        xAxis.setLabel("X (valeurs)");
		        yAxis.setLabel("Y (valeurs)");
		        //Visualisation du nuage de points pour les données initiales(avant ACP)
		        final ScatterChart<Number,Number> scatterChart =new ScatterChart<Number,Number>(xAxis,yAxis);
		        scatterChart.setTitle("Transformation ACP");
				XYChart.Series series = new XYChart.Series();
		        series.setName("Données initiales");
		        //Ajouter les données
		        for(int i=0;i<dataFX.matrice[0].length;i++) {
		        	     series.getData().add(new XYChart.Data(dataFX.matrice[0][i],dataFX.matrice[1][i]));
		        	}
		        Scene scene  = new Scene(scatterChart,800,600);
		        scatterChart.getData().add(series);
		        stage.setScene(scene);
		        stage.show();
		       
		        dataFX.centrer_reduire();
		        double [][] x_PROJ=Acp.analysecomposantsprincipales(dataFX.matrice, 2);
		        //definition des axes pour les données après ACP
		        final NumberAxis xAxis1 = new NumberAxis();
		        final NumberAxis yAxis1 = new NumberAxis();
		        xAxis1.setLabel("X (valeurs)");
		        yAxis1.setLabel("Y (valeurs)");
		        //Visualisation du nuage de points pour les données transformées(après ACP)
		        final ScatterChart<Number,Number> scatterChart1 = new ScatterChart<Number,Number>(xAxis1,yAxis1);
		        scatterChart1.setTitle("Transformation ACP");
				XYChart.Series series1 = new XYChart.Series();
		        series1.setName("Données après transformation ACP");
		        //Ajouter les données après ACP
		        for(int i=0;i<dataFX.matrice[0].length;i++) {
		        	     series1.getData().add(new XYChart.Data(x_PROJ[0][i],x_PROJ[1][i]));
		        	}
		        scatterChart.getData().add(series1);
		        //Afficher les résultats
		        stage.setScene(scene);
		        stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        });
        //Traitements de la fenêtre initiale
        VBox vBox = new VBox(PADDING, label, singleButton);
        vBox.getStyleClass().add("color-palette");
        vBox.setBackground(null);
        vBox.setPadding(new Insets(PADDING));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
        
    }
    public static void main(String args[]) {
        launch(args);
    }
}