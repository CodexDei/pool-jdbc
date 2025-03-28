package org.codexdei.java.jdbc;

import org.codexdei.java.jdbc.modelo.Categoria;
import org.codexdei.java.jdbc.modelo.Producto;
import org.codexdei.java.jdbc.repositorio.ProductoRepositorioImpl;
import org.codexdei.java.jdbc.repositorio.Repositorio;
import org.codexdei.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.Date;

public class EjemploJdbc {

    public static void main(String[] args) {

            Repositorio<Producto> repositorio = new ProductoRepositorioImpl();
            System.out.println("============= listar =============");
            repositorio.listar().forEach(System.out::println);

            System.out.println("============= obtener por id =============");
            System.out.println(repositorio.buscarId(1L));

            System.out.println("============= insertar nuevo producto =============");
            Producto producto = new Producto();
            producto.setNombre("Bate de beisbol");
            producto.setPrecio(10);
            producto.setFechaRegistro(new Date());
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(1L);
            producto.setCategoria(categoria);
            repositorio.guardar(producto);
            System.out.println("Producto guardado con éxito");
            repositorio.listar().forEach(System.out::println);

    }
}
