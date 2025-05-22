import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;

public class FormularioTramite {

    public static void main(String[] args) {
        // Crear el marco principal
        JFrame frame = new JFrame("Formulario de Trámite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setResizable(false); // Desactivar el botón maximizar

        // Centrar el marco en la pantalla
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Crear un panel principal con márgenes y diseño GridBagLayout
       JPanel panel = new JPanel(new GridBagLayout());
       panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes alrededor del panel
       GridBagConstraints gbc = new GridBagConstraints();

        // Configuración inicial de los componentes
        gbc.insets = new Insets(10, 10, 10, 100); // Márgenes entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Crear y agregar los campos de entrada
        JLabel labelNombreSolicitud = new JLabel("Nombre de la solicitud (100 caracteres):");
        JTextField textNombreSolicitud = new JTextField(100);
        panel.add(labelNombreSolicitud, gbc);
        gbc.gridx = 1;
        panel.add(textNombreSolicitud, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelNombres = new JLabel("Nombres (50 caracteres):");
        JTextField textNombres = new JTextField(50);
        panel.add(labelNombres, gbc);
        gbc.gridx = 1;
        panel.add(textNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelApellidos = new JLabel("Apellidos (50 caracteres):");
        JTextField textApellidos = new JTextField(50);
        panel.add(labelApellidos, gbc);
        gbc.gridx = 1;
        panel.add(textApellidos, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelDNI = new JLabel("DNI (8 caracteres numéricos):");
        JTextField textDNI = new JTextField(8);
        panel.add(labelDNI, gbc);
        gbc.gridx = 1;
        panel.add(textDNI, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelCelular = new JLabel("Celular (12 caracteres numéricos):");
        JTextField textCelular = new JTextField(12);
        panel.add(labelCelular, gbc);
        gbc.gridx = 1;
        panel.add(textCelular, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelCorreo = new JLabel("Correo (25 caracteres):");
        JTextField textCorreo = new JTextField(25);
        panel.add(labelCorreo, gbc);
        gbc.gridx = 1;
        panel.add(textCorreo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelFecha = new JLabel("Fecha actual:");
        JTextField textFecha = new JTextField(LocalDate.now().toString());
        textFecha.setEditable(false); // Hacer que el campo de fecha sea solo lectura
        panel.add(labelFecha, gbc);
        gbc.gridx = 1;
        panel.add(textFecha, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel labelSustentoSolicitud = new JLabel("Sustento de la solicitud (200 caracteres):");
        JTextArea textSustentoSolicitud = new JTextArea(5, 30);
        textSustentoSolicitud.setLineWrap(true);
        textSustentoSolicitud.setWrapStyleWord(true);
        JScrollPane scrollSustento = new JScrollPane(textSustentoSolicitud);
        panel.add(labelSustentoSolicitud, gbc);
        gbc.gridx = 1;
        panel.add(scrollSustento, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        JButton generarPDF = new JButton("Generar PDF");
        generarPDF.addActionListener(e -> {
            try {
                // Validar que los campos obligatorios no estén vacíos
                if (textDNI.getText().isEmpty() || textCelular.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Los campos DNI y Celular son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que el DNI sea numérico y tenga 8 caracteres
                if (!textDNI.getText().matches("\\d{8}")) {
                    JOptionPane.showMessageDialog(frame, "El DNI debe ser numérico y tener 8 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que el celular sea numérico y tenga hasta 12 caracteres
                if (!textCelular.getText().matches("\\d{1,12}")) {
                    JOptionPane.showMessageDialog(frame, "El celular debe ser numérico y tener hasta 12 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear el nombre del archivo PDF
                String dni = textDNI.getText();
                String anoActual = String.valueOf(Year.now().getValue());
                String nombreArchivo = "D:/solicitud/" + dni + "_" + anoActual + "_IE_Leonardo_Davinci.pdf";

                // Crear la carpeta si no existe
                File carpeta = new File("D:/solicitud");
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }

                // Crear el documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
                document.open();

                // Agregar contenido al PDF
                document.add(new Paragraph("Nombre de la solicitud: " + textNombreSolicitud.getText()));
                document.add(new Paragraph("Nombres: " + textNombres.getText()));
                document.add(new Paragraph("Apellidos: " + textApellidos.getText()));
                document.add(new Paragraph("DNI: " + textDNI.getText()));
                document.add(new Paragraph("Celular: " + textCelular.getText()));
                document.add(new Paragraph("Correo: " + textCorreo.getText()));
                document.add(new Paragraph("Fecha: " + textFecha.getText()));
                document.add(new Paragraph("Sustento de la solicitud: " + textSustentoSolicitud.getText()));

                document.close();
                JOptionPane.showMessageDialog(frame, "PDF generado exitosamente en: " + nombreArchivo);
            } catch (IOException | DocumentException ex) {
                JOptionPane.showMessageDialog(frame, "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(generarPDF, gbc);

        // Agregar el panel al marco y mostrar
        frame.add(panel);
        frame.setVisible(true);
    }
}