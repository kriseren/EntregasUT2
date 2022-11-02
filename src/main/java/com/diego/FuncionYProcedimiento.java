package com.diego;

import java.sql.*;

public class FuncionYProcedimiento
{
    public static void main(String[] args)
    {
        String sqlProcedure = "call borraartista(?)";
        String sqlFunction = "SELECT cuentaartistas()";
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall(sqlProcedure);
            PreparedStatement pstmt = conn.prepareStatement(sqlFunction,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = pstmt.executeQuery())
        {
            //Ejecutamos el procedure.
            cstmt.setString(1,"Diego");
            cstmt.execute();
            System.out.println("El artista Diego se ha borrado correctamente.");
            //Ejecutamos la function.
            resultSet.first();
            if(resultSet.getInt(1)>0)
                System.out.println("Hay "+resultSet.getString(1)+" artistas.");
            else
                System.out.println("No hay artistas dados de alta");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
