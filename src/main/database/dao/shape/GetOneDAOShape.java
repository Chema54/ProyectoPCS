/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database.dao.shape;

import main.common.NotFoundException;
import main.common.UserDisplayableException;

/**
 *
 * @author josem
 */
public interface GetOneDAOShape<T, F> {
  T getOne(F filter) throws UserDisplayableException, NotFoundException;
}
