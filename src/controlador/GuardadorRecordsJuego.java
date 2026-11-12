package src.controlador;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import src.modelo.entities.Record;

public class GuardadorRecordsJuego {

    public void guardar(ControladorJuego controlador, int nivelAlcanzado) {
        if (controlador.getRecordDAO() == null) return;

        long tiempoTotal = System.currentTimeMillis() - controlador.getTiempoInicioPartida();
        Record nuevoRecord = new Record(controlador.getNombreJugador(), tiempoTotal, nivelAlcanzado);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return controlador.getRecordDAO().guardar(nuevoRecord);
            }

            @Override
            protected void done() {
                try {
                    if (!get()) {
                        System.err.println("No se pudo guardar el récord.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}