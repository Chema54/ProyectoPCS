/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.basedatos.dao.shape;

/**
 *
 * @author josem
 */
public abstract class MoldeDAOCompleto<T, F>
  implements MoldeDAOCrearUno<T>, MoldeDAOObtenerUno<T, F>, MoldeDAOObtenerTodos<T>, MoldeDAOActualizarUno<T>, MoldeDAOEliminarUno<F> {
}
