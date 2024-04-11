/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package tarea7g;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tomas
 */
public class Tarea7GTomas {

    public static void main(String[] args) throws IOException {
        // Lectura de datos. Enseño por consola
        System.out.println("---- Lista de Datos ----");
        List<ClasePojo> listaAplicaciones = new ArrayList<>();
        listaAplicaciones = lecturaFicheroJSON("precipitacionesBadajoz.json");
        listaAplicaciones.forEach(System.out::println);

        //
        Map<String, Double> precipitaciones = new HashMap<>();
        precipitaciones = mapPrecipitaciones(listaAplicaciones);
        int contador = 0;
        for (Map.Entry<String, Double> entry : precipitaciones.entrySet()) {
            System.out.println(contador++ + " --> Estación: " + entry.getKey() + ", Precipitación: " + entry.getValue());
        }

        //
        generarFicherosJSON(precipitaciones);

        // 
        estacionMayorPrecipitacion(listaAplicaciones);
        // 
        System.out.println("Entre esas fehcas: " + lecturasOctibre(listaAplicaciones));

        // 
        System.out.println("Media lecturas coturbre: " + mediaLecturasOctubre(listaAplicaciones));
    }

    //
    public static List<ClasePojo> lecturaFicheroJSON(String nombreFichero) throws IOException {
        List<ClasePojo> listaAplicaciones = new ArrayList<>();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());

        listaAplicaciones = mapeador.readValue(new File(nombreFichero),
                mapeador.getTypeFactory().constructCollectionType(List.class, ClasePojo.class));
        return listaAplicaciones;
    }

    // Obtén una estructura Map donde para cada nombre de estación meteorológica 
    // se pueda consultar la precipitación acumulada de la misma. Muestra por consola 
    // el resultado de la estructura map obtenida.
    public static Map<String, Double> mapPrecipitaciones(List<ClasePojo> listaJSON) {
        Map<String, Double> precipitaciones = new HashMap<>();
        for (ClasePojo pojo : listaJSON) {
            // falta que si hay dos estaciones repetidas, ss sumen los valores de eso.
            precipitaciones.put(pojo.estacionMeteorologica(), pojo.precipitacion());
        }
        return precipitaciones;
    }

    // Método para generar ficheros json
    public static void generarFicherosJSON(Map<String, Double> precipitaciones) throws IOException {
        //SistemasFicheros.crearDirectorio(rutaDirectorio);
        ObjectMapper mapeador = new ObjectMapper();

        // Formato JSON bien formateado. Si se comenta, el fichero queda minificado
        mapeador.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapeador.writeValue(new File("catalogoApps.json"), precipitaciones);
    }

    // Método que dice que estacion tiene mayor precipitación
    public static void estacionMayorPrecipitacion(List<ClasePojo> lista) {
        double mayorPrecipitacion = lista.stream().mapToDouble(p -> p.precipitacion()).max().getAsDouble();
        for (ClasePojo clasePojo : lista) {
            if (clasePojo.precipitacion() == mayorPrecipitacion) {
                System.out.println("La mayor precipitación la tiene: " + clasePojo.estacionMeteorologica());
                break;
            }
        }
    }

    // Usando API Stream, muestra por consola el número de estaciones meteorológicas 
    // que han sido leídas entre el 10 de octubre de 2017 y el 20 de octubre de 2017, 
    // ambos días incluidos.
    public static int lecturasOctibre(List<ClasePojo> lista) {
        return (int) lista.stream()
                .filter(p
                        -> p.fecha().isAfter(LocalDate.of(2017, Month.OCTOBER, 10))
                && p.fecha().isBefore(LocalDate.of(2017, Month.OCTOBER, 20))).count();
    }

    // Usando API Stream, calcula la media de precipitaciones de aquellas estaciones 
    // meteorológicas leídas entre el 10 de octubre de 2017 y el 20 de octubre de 2017, 
    // ambos días incluidos.
    public static Double mediaLecturasOctubre(List<ClasePojo> lista) {
        return lista.stream()
                .filter(p
                        -> p.fecha().isAfter(LocalDate.of(2017, Month.OCTOBER, 10))
                && p.fecha().isBefore(LocalDate.of(2017, Month.OCTOBER, 20)))
                .mapToDouble(p -> p.precipitacion())
                .average()
                .getAsDouble();

    }

}
