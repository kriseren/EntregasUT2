package com.diego;
import java.sql.*;

public class InicializaBaseDeDatos
{
    public static void main(String[] args)
    {
        //Creamos la base de datos Diego.
        creaBaseDeDatos();
        //Creamos la tabla Artistas dentro de la base de datos Diego.
        creaTablaArtistas();
        //Insertamos datos de prueba en la tabla Artistas.
        insertaDatos();
        //Crea función y procedimiento.
        creaFuncionYProcedimiento();
    }

    public static void insertaDatos()
    {
        //Nos conectamos y creamos una base de datos.
        String sql = "INSERT INTO ARTISTAS(ID,NOMBRE,CACHE,EDAD,TIPO) VALUES (DEFAULT,?,?,?,?)";
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            insertaArtista(pstmt,"Diego",621.2,19,'B');
            insertaArtista(pstmt,"Ramón",319.4,31,'S');
            insertaArtista(pstmt,"Arturo",204,38,'S');
            insertaArtista(pstmt,"Javier",521.76,19,'B');
            insertaArtista(pstmt,"Jonatan",10,31,'S');
            insertaArtista(pstmt,"Samuel",512.5,23,'S');
            System.out.println("Datos insertados correctamente.");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void insertaArtista(PreparedStatement pstmt,String n,double c,int e,char t)
    {
        try {
            pstmt.setString(1,n);
            pstmt.setDouble(2,c);
            pstmt.setInt(3,e);
            pstmt.setString(4,String.valueOf(t));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void creaTablaArtistas()
    {
        //Nos conectamos y creamos una tabla artistas.
        String sql = "CREATE TABLE ARTISTAS ("+
                "ID SERIAL PRIMARY KEY,"+
                "NOMBRE VARCHAR(50),"+
                "CACHE DECIMAL(8,2)," +
                "EDAD SMALLINT," +
                "TIPO CHAR," +
                "CONSTRAINT CK_TIPO CHECK (UPPER(TIPO) IN ('S','B'))" +
                ");";
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            Statement stmt = conn.createStatement())
        {
            stmt.executeUpdate(sql);
            System.out.println("Tabla creada correctamente");
            ConnectionPool.getInstance().closeConnection(conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void creaBaseDeDatos()
    {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        //Nos conectamos y creamos una base de datos.
        try(Connection conn = DriverManager.getConnection(url,"root","root");
            Statement stmt = conn.createStatement())
        {
            String sql = "CREATE DATABASE DIEGO";
            stmt.executeUpdate(sql);
            System.out.println("Base de datos creada correctamente");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void creaFuncionYProcedimiento()
    {
        //Nos conectamos y creamos una base de datos.
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            Statement stmt = conn.createStatement())
        {
            String funcion = "CREATE OR REPLACE FUNCTION public.cuentaArtistas()\n" +
                    "RETURNS int\n" +
                    "\tLANGUAGE plpgsql\n" +
                    "AS $$\n" +
                    "declare\n" +
                    "contador integer;\n" +
                    "BEGIN\n" +
                    "    select count(*) into contador from artistas;\n" +
                    "return contador;\n" +
                    "END;";
            String procedimiento = "CREATE OR REPLACE PROCEDURE public.borraArtista(n varchar)\n" +
                    "\tLANGUAGE plpgsql\n" +
                    "AS $procedure$\n" +
                    "BEGIN\n" +
                    "    DELETE FROM artistas WHERE nombre = n;\n" +
                    "END;\n" +
                    "$procedure$\n" +
                    ";";
            stmt.executeUpdate(procedimiento);
            stmt.executeUpdate(funcion);
            System.out.println("Función y procedimiento creados correctamente.");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
