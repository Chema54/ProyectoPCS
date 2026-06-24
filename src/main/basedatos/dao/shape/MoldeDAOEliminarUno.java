/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.basedatos.dao.shape;

import main.comun.ExcepcionMostrableUsuario;

/**
 *
 * @author josem
 */
public interface MoldeDAOEliminarUno<F> {
  void deleteOne(F filter) throws ExcepcionMostrableUsuario;
}
