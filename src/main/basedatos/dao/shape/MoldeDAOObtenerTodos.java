/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.basedatos.dao.shape;

import java.util.List;
import main.comun.ExcepcionMostrableUsuario;

/**
 *
 * @author josem
 */
public interface MoldeDAOObtenerTodos<T> {
  List<T> getAll() throws ExcepcionMostrableUsuario;
}
