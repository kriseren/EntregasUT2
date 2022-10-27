package com.diego;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
//TODO QUITAR LOS CLOSE DE LAS CONEXIONES
public class OperacionesResultSet
{
    //Definición de atributos.
    private static final Scanner scan = new Scanner(System.in);
    private static Connection conn;
    private static String sql = "SELECT * FROM ARTISTAS";

    public static void main(String[] args)
    {
        //Inicializo la conexión.
        conn = ConnectionPool.getInstance().getConnection();
        //Llama al método Menu.
        menu();
    }

    public static void menu()
    {
        int op=1;
        while(op>=0)
        {
            System.out.print("\n[OPERACIONES CON RESULT SET]\n 1-. Consultar todos los artistas.\n 2-. Consultar un artista.\n 3-. Consultar el primer artista." +
                            "\n 4-. Consultar el último artista.\n 5-. Introducir un nuevo artista.\n 6-. Editar el último artista.\n 7-. Borrar el último artista." +
                            "\n 0-. Salir del programa\nSelecciona una opción: ");
            op = scan.nextInt();
            switch(op)
            {
                case 1:consultaGeneral();break;
                case 2:consultaEspecifica();break;
                case 3:consultaPrimera();break;
                case 4:consultaUltima();break;
                case 5:insertaNuevoArtista();break;
                case 6:editaUltimoArtista();break;
                case 7:
                    eliminaUltimoArtista();break;
                case 0:exit();break;
                default:System.out.println("Introduce un número válido");
            }
        }
    }

    public static void consultaGeneral()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nRESULTADO DE LA CONSULTA GENERAL");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            while(rs.next())
                muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void consultaEspecifica()
    {
        //Pedimos al usuario un número de fila.
        System.out.print("\nIntroduce el número de fila a mostrar: ");
        int row = scan.nextInt();

        //Realizamos la consulta.
        String sql = "SELECT * FROM ARTISTAS";
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nRESULTADO DE LA CONSULTA ESPECÍFICA");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            rs.absolute(row);
            //Imprimo la línea entera.
            muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void consultaPrimera()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nRESULTADO DE LA PRIMERA FILA");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            rs.first();
            //Imprimo la línea entera.
            muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void consultaUltima()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nRESULTADO DE LA ÚLTIMA FILA");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            rs.last();
            //Imprimo la línea entera.
            muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertaNuevoArtista()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nINSERCIÓN DE UN ARTISTA NUEVO");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            //Me posiciono.
            rs.moveToInsertRow();
            rs.updateString(2,"Logan");
            rs.updateDouble(3,621.8);
            rs.updateInt(4,21);
            rs.updateString(5,"B");
            rs.insertRow();
            //Muestro la línea insertada.
            muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editaUltimoArtista()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nEDICIÓN DEL ÚLTIMO ARTISTA");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            //Me posiciono.
            rs.last();
            rs.updateString(2,"Alberto");
            rs.updateRow();
            //Muestro la línea insertada.
            muestraLinea(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void eliminaUltimoArtista()
    {
        //Realizamos la consulta.
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql))
        {
            System.out.println("\nELIMINACIÓN DEL ÚLTIMO ARTISTA");
            System.out.println("---------------------------------");
            System.out.println("ID\tNOMBRE\tCACHÉ\tEDAD\tTIPO");
            //Me posiciono.
            rs.last();
            rs.deleteRow();
            //Muestro la línea insertada.
            System.out.println("Artista eliminado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void muestraLinea(ResultSet rs)
    {
        //Imprimo la línea entera.
        try {
            System.out.print(rs.getString(1)+"\t");
            System.out.print(rs.getString(2)+"\t");
            System.out.print(rs.getString(3)+"\t");
            System.out.print(rs.getString(4)+"\t\t");
            System.out.println(rs.getString(5));
        }
        catch (SQLException e) {throw new RuntimeException(e);}
    }
    public static void exit()
    {
        try
        {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        catch(SQLException e){e.printStackTrace();}
        System.exit(0);
    }
}
