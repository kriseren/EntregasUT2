package com.diego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaccion
{
    public static void main(String[] args)
    {
        //Creamos las referencias a los objetos necesarios.
        Connection conn=null;
        PreparedStatement pstmt=null;
        PreparedStatement pstmt2=null;
        //Creamos las cadenas de caracteres de las sentencias.
        String sql1 = "INSERT INTO ARTISTAS(ID,NOMBRE,CACHE,EDAD,TIPO) VALUES (DEFAULT,?,?,?,?)";
        String sql2 = "UPDATE ARTISTAS SET NOMBRE=? WHERE NOMBRE=?";
        try
        {
            //Creamos los objetos necesarios.
            conn = ConnectionPool.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql1);
            pstmt2 = conn.prepareStatement(sql2);
            //Desactivar el autocommit.
            conn.setAutoCommit(false);
            //Realizamos la primera operación --> Insertar un nuevo artista.
            pstmt.setString(1,"Juan José");
            pstmt.setDouble(2,213.7);
            pstmt.setInt(3,43);
            pstmt.setString(4,"B");
            pstmt.executeUpdate();
            //Realizamos la segunda operación --> Cambiar el nombre de un artista por otro.
                pstmt2.setString(1,"Alberto");
            pstmt2.setString(2,"Ramón");
            pstmt2.executeUpdate();
            //Si al final no da error la ejecución, se hace un commit total.
            conn.commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Tanto como si ha dado error como si no:
                conn.rollback(); //Se deshacen los cambios no actualizados.
                conn.setAutoCommit(true); //Se establece el AutoCommit a true.
                pstmt.close(); //Cerramos el primer PreparedStatement.
                pstmt2.close(); //Cerramos el segundo PreparedStatement.
                ConnectionPool.getInstance().closeConnection(conn); //Cerramos la conexión.
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
