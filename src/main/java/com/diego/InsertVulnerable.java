package com.diego;

import java.sql.*;
import java.util.Scanner;

public class InsertVulnerable
{
    private static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args)
    {
        //Inicializamos la tabla.
        iniciaTabla();
        System.out.println("Hemos creado una tabla vulnerable. A continuación vamos a consultar la tabla y probar a hacer una SQL Injection. Ejecuta 'Diego';drop table vulnerable;");
        //Hacemos un select en la tabla.
        consultaTablaVulnerable();
        System.out.println("Sentencia ejecutada correctamente.\nAhora, comprueba que la tabla ha sido eliminada y pulsa Intro.");
        scan.nextLine();
        //Inicializamos de nuevo la tabla.
        iniciaTabla();
        System.out.println("Hemos creado la tabla de nuevo. Ahora prueba a consultarla al igual que antes y hacer una SQL Injection.");
        //Hacemos la misma consulta, esta vez sin vulnerabilidad.
        consultaTablaInvulnerable();
        System.out.println("Sentencia ejecutada correctamente. Ahora, comprueba que la tabla no se ha eliminado.");
    }

    public static void iniciaTabla()
    {
        //CREAMOS UNA TABLA VULNERABLE E INSERTAMOS DATOS
        String sql = "CREATE TABLE VULNERABLE("+
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
            //Creo la tabla e inserto datos.
            stmt.executeUpdate(sql);
            String insertSQL = "INSERT INTO VULNERABLE(ID,NOMBRE,CACHE,EDAD,TIPO) VALUES(DEFAULT,?,?,?,?);";
            stmt.close();
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1,"Lucas");
            pstmt.setDouble(2,234.8);
            pstmt.setInt(3,23);
            pstmt.setString(4,"s");
            pstmt.executeUpdate();
            pstmt.close();
            ConnectionPool.getInstance().closeConnection(conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void consultaTablaInvulnerable()
    {
        //HACEMOS UN SELECT A LA TABLA
        System.out.print("Vamos a mostrar los artistas que se llamen: ");
        String nombre = scan.nextLine();
        String sql = "SELECT * FROM VULNERABLE WHERE NOMBRE=?";
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,nombre);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.print(rs.getString(3)+"\t");
                System.out.print(rs.getString(4)+"\t\t");
                System.out.println(rs.getString(5));
            }
            rs.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void consultaTablaVulnerable()
    {
        //HACEMOS UN SELECT A LA TABLA
        System.out.print("Vamos a mostrar los artistas que se llamen: ");
        String nombre = scan.nextLine();
        try(Connection conn = ConnectionPool.getInstance().getConnection();
            Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM VULNERABLE WHERE NOMBRE="+nombre;
            System.out.println(sql);
            stmt.execute(sql);
            ConnectionPool.getInstance().closeConnection(conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
