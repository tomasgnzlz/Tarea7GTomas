/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package tarea7g;

import java.time.LocalDate;

/**
 *
 * @author tomas
 */
public record ClasePojo(
        LocalDate fecha,
        String estacionMeteorologica,
        String provincia,
        double precipitacion) {
}
