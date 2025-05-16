import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

// Clase Contacto
class Contacto {
    private String nombre;
    private String telefono;

    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Teléfono: " + telefono;
    }
}

// Clase principal
public class AgendaTelefonicaConExcel {
    private ArrayList<Contacto> contactos;
    private final String archivoExcel = "d:/vscode/agenda_telefonica.xlsx";

    public AgendaTelefonicaConExcel() {
        contactos = new ArrayList<>();
        leerDesdeExcel();
    }

    // Método para leer contactos desde el archivo Excel
    private void leerDesdeExcel() {
        try (FileInputStream fis = new FileInputStream(archivoExcel);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell nombreCell = row.getCell(0);
                Cell telefonoCell = row.getCell(1);

                if (nombreCell != null && telefonoCell != null) {
                    String nombre = nombreCell.getStringCellValue();
                    String telefono = telefonoCell.getStringCellValue();
                    contactos.add(new Contacto(nombre, telefono));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo de Excel no existe. Se creará uno nuevo al guardar.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de Excel: " + e.getMessage());
        }
    }

    // Método para guardar contactos en el archivo Excel
    private void guardarEnExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contactos");

            int rowIndex = 0;
            for (Contacto contacto : contactos) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(contacto.getNombre());
                row.createCell(1).setCellValue(contacto.getTelefono());
            }

            try (FileOutputStream fos = new FileOutputStream(archivoExcel)) {
                workbook.write(fos);
            }
            System.out.println("Contactos guardados en el archivo Excel.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de Excel: " + e.getMessage());
        }
    }

    // Método para agregar un contacto
    public void agregarContacto(String nombre, String telefono) {
        contactos.add(new Contacto(nombre, telefono));
        System.out.println("Contacto agregado.");
    }

    // Método para listar todos los contactos
    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("La agenda está vacía.");
        } else {
            for (Contacto contacto : contactos) {
                System.out.println(contacto);
            }
        }
    }

    // Método para buscar un contacto por nombre
    public void buscarContacto(String nombre) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Contacto encontrado: " + contacto);
                return;
            }
        }
        System.out.println("Contacto no encontrado.");
    }

    // Método para actualizar un contacto
    public void actualizarContacto(String nombre, String nuevoTelefono) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)) {
                contacto.setTelefono(nuevoTelefono);
                System.out.println("Contacto actualizado.");
                return;
            }
        }
        System.out.println("Contacto no encontrado.");
    }

    // Método para eliminar un contacto
    public void eliminarContacto(String nombre) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)) {
                contactos.remove(contacto);
                System.out.println("Contacto eliminado.");
                return;
            }
        }
        System.out.println("Contacto no encontrado.");
    }

    // Método principal
    public static void main(String[] args) {
        AgendaTelefonicaConExcel agenda = new AgendaTelefonicaConExcel();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Agenda Telefónica ---");
            System.out.println("1. Agregar contacto");
            System.out.println("2. Listar contactos");
            System.out.println("3. Buscar contacto");
            System.out.println("4. Actualizar contacto");
            System.out.println("5. Eliminar contacto");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese el teléfono: ");
                    String telefono = scanner.nextLine();
                    agenda.agregarContacto(nombre, telefono);
                    break;
                case 2:
                    agenda.listarContactos();
                    break;
                case 3:
                    System.out.print("Ingrese el nombre a buscar: ");
                    String nombreBuscar = scanner.nextLine();
                    agenda.buscarContacto(nombreBuscar);
                    break;
                case 4:
                    System.out.print("Ingrese el nombre del contacto a actualizar: ");
                    String nombreActualizar = scanner.nextLine();
                    System.out.print("Ingrese el nuevo teléfono: ");
                    String nuevoTelefono = scanner.nextLine();
                    agenda.actualizarContacto(nombreActualizar, nuevoTelefono);
                    break;
                case 5:
                    System.out.print("Ingrese el nombre del contacto a eliminar: ");
                    String nombreEliminar = scanner.nextLine();
                    agenda.eliminarContacto(nombreEliminar);
                    break;
                case 6:
                    System.out.println("Guardando contactos y saliendo de la agenda...");
                    agenda.guardarEnExcel();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        scanner.close();
    }
}