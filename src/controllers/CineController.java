package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CineController {
    @FXML
    private ListView<String> listViewPeliculas;

    @FXML
    private Label labelMensaje;

    @FXML
    private Button btnReservar;

    @FXML
    public void initialize() {
        listViewPeliculas.getItems().addAll(
                "Oppenheimer",
                "Spider-Man: Across the Spider-Verse",
                "Dune: Part Two",
                "Godzilla x Kong: The New Empire",
                "The Batman 2",
                "Skibidi toilet: La película"
        );
    }

    @FXML
    public void reservarBoletos() {
        String peliculaSeleccionada = listViewPeliculas.getSelectionModel().getSelectedItem();
        if (peliculaSeleccionada != null) {
            labelMensaje.setText("🎟 Boletos reservados para: " + peliculaSeleccionada);
            mostrarAlerta("Reserva Confirmada", "Tu reserva para " + peliculaSeleccionada + " ha sido realizada con éxito.");
        } else {
            labelMensaje.setText("⚠️ Por favor, selecciona una película.");
            mostrarAlerta("Error", "Debes seleccionar una película antes de reservar.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
