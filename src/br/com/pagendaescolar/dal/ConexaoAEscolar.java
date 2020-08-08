package br.com.pagendaescolar.dal;


//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author franz
 */
public class ConexaoAEscolar {
    
    static Connection conexao = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;
    
    //metodo responsavel por estabelecer a conexao com o BD
    public static Connection conector() {

        Connection conn = null;
        
        try {
            
            String url = "jdbc:sqlite:/home/franz/NetBeansProjects/pAgendaEscolar/sql_Agenda_Escolar";
            
            Connection conexao = DriverManager.getConnection(url);
            
            System.out.println("Conexão estabelecida!");
            
            return conexao;
            
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }


}
