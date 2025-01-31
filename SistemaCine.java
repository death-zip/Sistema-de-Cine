import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class SistemaCine {
    private final ArrayList<Pelicula> peliculas = new ArrayList<>();
    private final ArrayList<Funcion> funciones = new ArrayList<>();
    private final ArrayList<Administrador> administradores = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SistemaCine cine = new SistemaCine();
        cine.cargarDatos();
        cine.iniciarSistema();
    }

    private void iniciarSistema() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE CINE ===");
            System.out.println("1. Administrador");
            System.out.println("2. Comprar boletos");
            System.out.println("3. Mostrar Cartelera");
            System.out.println("4. Salir");
            System.out.print("Seleccione: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> menuAdmin();
                case 2 -> comprarBoletos();
                case 3 -> mostrarCartelera();
            }
            
        } while(opcion != 4);

        guardarDatos();
    }

    private void menuAdmin() {
        System.out.print("Usuario: ");
        String usuario = scanner.next();
        System.out.print("Contraseña: ");
        String contrasena = scanner.next();

        for (Administrador admin : administradores) {
            if (admin.autenticar(usuario, contrasena)) {
                int opcion;
                do {
                    System.out.println("\n=== PANEL ADMINISTRADOR ===");
                    System.out.println("1. Agregar película");
                    System.out.println("2. Modificar película");
                    System.out.println("3. Agregar función");
                    System.out.println("4. Generar reportes");
                    System.out.println("5. Agregar Administrador");
                    System.out.println("6. Regresar");
                    System.out.print("Seleccione: ");
                    opcion = scanner.nextInt();

                    switch (opcion) {
                        case 1 -> agregarPelicula();
                        case 2 -> modificarPelicula();
                        case 3 -> agregarFuncion();
                        case 4 -> generarReportes();
                        case 5 -> agregarAdministrador();
                    }
                    
                } while(opcion != 6);
                return;
            }
        }
        System.out.println("Credenciales incorrectas!");
    }

    private void agregarPelicula() {
        System.out.println("\nNueva Película:");
        scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Género: ");
        String genero = scanner.nextLine();
        System.out.print("Duración (min): ");
        int duracion = scanner.nextInt();
    
        // Crear la nueva película y agregarla
        Pelicula nuevaPelicula = new Pelicula(titulo, genero, duracion);
        peliculas.add(nuevaPelicula);
    
        System.out.println("Película agregada con éxito!");
        mostrarCartelera(); // Esto también mostrará las películas con sus IDs
    }
    

    private void modificarPelicula() {
        mostrarCartelera();
        System.out.print("Ingrese el ID de la película a modificar: ");
        int id = scanner.nextInt();
        if (id >= 0 && id < peliculas.size()) {
            scanner.nextLine();
            System.out.print("Nuevo título: ");
            peliculas.get(id).setTitulo(scanner.nextLine());
            System.out.print("Nuevo género: ");
            peliculas.get(id).setGenero(scanner.nextLine());
            System.out.print("Nueva duración (min): ");
            peliculas.get(id).setDuracion(scanner.nextInt());
            System.out.println("Película modificada!");
        } else {
            System.out.println("ID inválido!");
        }
    }

    private void agregarFuncion() {
        mostrarCartelera();
        System.out.print("Seleccione la película (ID): ");
        int id = scanner.nextInt();
        if (id >= 0 && id < peliculas.size()) {
            scanner.nextLine();
            System.out.print("Horario: ");
            String horario = scanner.nextLine();
            System.out.print("Capacidad: ");
            int capacidad = scanner.nextInt();
            funciones.add(new Funcion(peliculas.get(id), horario, capacidad));
            System.out.println("Función agregada!");
        } else {
            System.out.println("ID inválido!");
        }
    }

    private void generarReportes() {
        System.out.println("Generando reportes...");
        for (Funcion f : funciones) {
            System.out.println(f);
        }
    }

    private void comprarBoletos() {
        mostrarCartelera();
    System.out.print("Seleccione la película (ID): ");
    int id = scanner.nextInt();
    if (id >= 0 && id < funciones.size()) {
        System.out.print("Cantidad de boletos: ");
        int cantidad = scanner.nextInt();
        Funcion funcion = funciones.get(id);

        // Verifica si hay suficientes boletos disponibles
        if (cantidad <= funcion.getCapacidadDisponible()) {
            funcion.venderBoletos(cantidad);
            System.out.println("Compra realizada para " + funcion.getPelicula().getTitulo() + " en horario " + funcion.getHorario());
        } else {
            System.out.println("No hay suficientes boletos disponibles. Solo hay " + funcion.getCapacidadDisponible() + " boletos disponibles.");
        }
    } else {
        System.out.println("ID inválido!");
    }
}
    private void cargarDatos() {
        System.out.println("Cargando datos...");
        cargarPeliculas();
        cargarFunciones();
        cargarAdministrador();  // Añadimos este método para cargar los administradores
    }
    
private void cargarAdministrador() {
    administradores.clear();
    try (BufferedReader br = new BufferedReader(new FileReader("administradores.csv"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 2) {
                administradores.add(new Administrador(datos[0], datos[1]));
            }
        }
        System.out.println("Administradores cargados correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo cargar administradores.csv");
    }
}

private void guardarAdministrador() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("administradores.csv"))) {
        for (Administrador admin : administradores) {
            pw.println(admin.getUsuario() + "," + admin.getContrasena());
        }
        System.out.println("Administradores guardados correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo guardar administradores.csv");
    }
}

private void cargarPeliculas() {
    peliculas.clear();
    try (BufferedReader br = new BufferedReader(new FileReader("peliculas.csv"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 3) {
                String titulo = datos[0];
                String genero = datos[1];
                int duracion = Integer.parseInt(datos[2]);
                peliculas.add(new Pelicula(titulo, genero, duracion));
            }
        }
        System.out.println("Películas cargadas correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo cargar peliculas.csv");
    }
} 
private void guardarPeliculas() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("peliculas.csv"))) {
        for (Pelicula p : peliculas) {
            pw.println(p.getTitulo() + "," + p.getGenero() + "," + p.getDuracion());
        }
        System.out.println("Películas guardadas correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo guardar peliculas.csv");
    }
}
private void cargarFunciones() {
    funciones.clear();
    try (BufferedReader br = new BufferedReader(new FileReader("funciones.csv"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 3) {
                String tituloPelicula = datos[0];
                String horario = datos[1];
                int capacidad = Integer.parseInt(datos[2]);

                // Buscar la película por título en la lista de películas
                Pelicula pelicula = buscarPeliculaPorTitulo(tituloPelicula);
                if (pelicula != null) {
                    funciones.add(new Funcion(pelicula, horario, capacidad));
                } else {
                    System.out.println("Película no encontrada: " + tituloPelicula);
                }
            }
        }
        System.out.println("Funciones cargadas correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo cargar funciones.csv");
    }
}

private void guardarFunciones() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("funciones.csv"))) {
        for (Funcion f : funciones) {
            pw.println(f.getPelicula().getTitulo() + "," + f.getHorario() + "," + f.getCapacidad());
        }
        System.out.println("Funciones guardadas correctamente.");
    } catch (IOException e) {
        System.out.println("No se pudo guardar funciones.csv");
    }
}

// Método auxiliar para buscar una película por título
private Pelicula buscarPeliculaPorTitulo(String titulo) {
    for (Pelicula p : peliculas) {
        if (p.getTitulo().equalsIgnoreCase(titulo)) {
            return p;
        }
    }
    return null;
}



private void guardarDatos() {
    guardarPeliculas();
    guardarFunciones();
    guardarAdministrador();  // Añadimos este método para guardar los administradores
    System.out.println("Guardando todos los datos...");
}

    private void agregarAdministrador() {
    System.out.print("Nuevo usuario: ");
    String usuario = scanner.next();
    System.out.print("Nueva contraseña: ");
    String contrasena = scanner.next();

    administradores.add(new Administrador(usuario, contrasena));
    System.out.println("Administrador agregado!");
}

private void mostrarCartelera() {
    System.out.println("\n=== CARTELERA ===");
    for (Pelicula pelicula : peliculas) {
        System.out.println("\nPelícula: " + pelicula.getTitulo() + " - Género: " 
            + pelicula.getGenero() + " - Duración: " + pelicula.getDuracion() + " minutos");
        
        // Mostrar las funciones de la película
        boolean hayFunciones = false; // Bandera para saber si la película tiene funciones
        for (Funcion funcion : funciones) {
            if (funcion.getPelicula().equals(pelicula)) {  // Verificar si la función corresponde a esta película
                hayFunciones = true;
                int capacidadDisponible = funcion.getCapacidadDisponible();
                if (capacidadDisponible > 0) {
                    System.out.println("  - Horario: " + funcion.getHorario() + " - Boletos disponibles: " + capacidadDisponible);
                } else {
                    System.out.println("  - Horario: " + funcion.getHorario() + " - AGOTADO");
                }
            }
        }

        if (!hayFunciones) {
            System.out.println("  - No hay funciones disponibles.");
        }
    }
}


    private class Pelicula {
        private String titulo;
        private String genero;
        private int duracion;

        public Pelicula(String titulo, String genero, int duracion) {
            this.titulo = titulo;
            this.genero = genero;
            this.duracion = duracion;
        }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }
        public int getDuracion() { return duracion; }
        public void setDuracion(int duracion) { this.duracion = duracion; }
    }

    private class Funcion {
        private final Pelicula pelicula; 
        private final String horario;    
        private final int capacidad;  // Capacidad total de la función
        private int boletosVendidos = 0;  // Contador de boletos vendidos
    
        public Funcion(Pelicula pelicula, String horario, int capacidad) {
            this.pelicula = pelicula;
            this.horario = horario;
            this.capacidad = capacidad;
        }
    
        public Pelicula getPelicula() {
            return pelicula;
        }
    
        public String getHorario() {
            return horario;
        }
    
        // Este método devuelve la capacidad disponible, es decir, la capacidad total menos los boletos vendidos.
        public int getCapacidadDisponible() {
            return capacidad - boletosVendidos; 
        }
    
        // Método para vender boletos, actualiza los boletos vendidos.
        public void venderBoletos(int cantidad) {
            if (cantidad <= getCapacidadDisponible()) {
                boletosVendidos += cantidad;
            }
        }
    
        // Método para obtener la capacidad total, si alguna vez la necesitas.
        public int getCapacidad() {
            return capacidad;
        }
    }
  private class Administrador {
    private final String usuario;
    private final String contrasena; 

    public Administrador(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean autenticar(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    }
}   
