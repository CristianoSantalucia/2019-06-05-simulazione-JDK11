/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.MonthDay;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxAnno"
	private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader
	@FXML // fx:id="boxMese"
	private ComboBox<Integer> boxMese; // Value injected by FXMLLoader
	@FXML // fx:id="boxGiorno"
	private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaReteCittadina"
	private Button btnCreaReteCittadina; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="txtN"
	private TextField txtN; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private final String ERRORE = "\n\nERRORE! controllare che i dati inseriti siano corretti";

	@FXML void OnYearSelected(ActionEvent event)
	{
		//cliccabili
		this.btnCreaReteCittadina.setDisable(false);
		this.boxMese.setDisable(false);

		//riempio mesi
		this.boxMese.getItems().clear();
		Integer year = this.boxAnno.getValue();
		if (year != null)
		{
			for (int m = 1; m <= 12; m++)
				this.boxMese.getItems().add(m);
		}
		else return; 
	}
	@FXML void OnMonthSelection(ActionEvent event)
	{
		this.boxGiorno.setDisable(false);
		this.boxGiorno.getItems().clear();
		// riempio giorni in base ad anno e mese
		Integer year = this.boxAnno.getValue();
		Integer month = this.boxMese.getValue();
		if (year != null && month != null) 
			this.boxGiorno.getItems().addAll(this.model.getDays(year, month));
		else return; 
	}


	@FXML void doCreaReteCittadina(ActionEvent event)
	{
		try
		{ 
			Integer year = this.boxAnno.getValue(); 
			if (year == null)
			{
				this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
				return;
			} 

			//resetto testo
			this.txtResult.clear();
			this.txtResult.appendText("Crea grafo...\n");

			//creo grafo
			this.model.creaGrafo(year);
			txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
					this.model.getNumVertici(),
					this.model.getNumArchi()));

			//cliccabili
			this.btnSimula.setDisable(false);
			this.txtN.setDisable(false);

			//stampa 
			txtResult.appendText("\n\n***STAMPA GRAFO***"+this.model.stampaGrafo());

		}
		catch(Exception e)
		{
			this.txtResult.appendText(this.ERRORE);
			e.printStackTrace();
		} 
	}

	@FXML void doSimula(ActionEvent event)
	{
		//controlli 
		Integer year = this.boxAnno.getValue(); 
		Integer month = this.boxMese.getValue(); 
		Integer day = this.boxGiorno.getValue(); 
		if (year == null || month == null || day == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento/i dalla/e lista/e");
			return;
		} 
		try
		{
			String N = this.txtN.getText();
			if (N != null)
			{
				Integer numPoliziotti = Integer.parseInt(N);
				if(numPoliziotti < 0)
				{
					this.txtResult.appendText("\n\nINSERIRE UN NUMERO INTERNO TRA 1 E 10");
					return; 
				}
				
				
				//simula
				this.txtResult.clear();
				this.txtResult.appendText("\nSIMULAZIONE IN CORSO... ");
				this.model.simula(year, month, day, numPoliziotti); 
			}
		}
		catch (Exception e)
		{
			this.txtResult.appendText("\n\nERRORE SIMULAZIONE");
			e.printStackTrace(); 
		} 
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
	}

	public void setModel(Model model)
	{
		this.model = model;

		// riempio anni 
		this.boxAnno.getItems().addAll(this.model.getYears());
	}
}
