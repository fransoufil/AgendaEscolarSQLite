/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pagendaescolar.dal;

import java.sql.*;

/**
 *
 * @author franz
 */
public class ConexaoPostgresql {
    
    static Connection conexao = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;
    
    public static Connection conector() {

        Connection conn = null;

        try {

           String url = "jdbc:postgresql://localhost:5432/AgendaEscolar_teste";
           String user = "postgres";
           String senha = "postgres";
           
            Connection conexao = DriverManager.getConnection(url, user, senha );

            System.out.println("Conexão estabelecida!");
            
            return conexao;

            

       } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
