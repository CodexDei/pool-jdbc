package org.codexdei.java.jdbc.repositorio;

import org.codexdei.java.jdbc.modelo.Categoria;
import org.codexdei.java.jdbc.modelo.Producto;
import org.codexdei.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImpl implements Repositorio<Producto>{

    //Implementamos una clase privada para la conexion a la base de datos
    private Connection getInstance() throws SQLException {

        return ConexionBaseDatos.getConnection();
    }

    @Override
    public List<Producto> listar() {

        List<Producto> productos = new ArrayList<>();

        try(Connection cnn = getInstance();
            Statement stmt = cnn.createStatement();
            ResultSet rs = stmt.executeQuery(
            "SELECT p.*, c.nombre_categoria as categoria FROM productos as p " +
                "INNER JOIN categorias as c ON (p.categoria_id = c.idcategorias)")){

            while (rs.next()){

                Producto p = crearProducto(rs);

                productos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return productos;
    }


    @Override
    public Producto buscarId(Long id) {

        Producto producto = null;

        try(Connection cnn = getInstance();
            PreparedStatement psts = cnn.prepareStatement(
            "SELECT p.*, c.nombre_categoria as categoria FROM productos as p " +
                "INNER JOIN categorias as c ON (p.categoria_id = c.idcategorias) WHERE idproductos = ?")
            ){

            psts.setLong(1,id);
            ResultSet rst = psts.executeQuery();

            if(rst.next()){

                producto = crearProducto(rst);

            }
            rst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    @Override
    public void guardar(Producto producto) {

        String sql;
        if (producto.getId() != null && producto.getId()>0) {
            sql = "UPDATE productos SET nombre=?, precio=?, categoria_id=? WHERE idproductos=?";
        } else {
            sql = "INSERT INTO productos(nombre, precio, categoria_id, fecha_registro) VALUES(?,?,?,?)";
        }
       try (Connection cnn = getInstance();
            PreparedStatement stmt = cnn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setLong(2, producto.getPrecio());
            stmt.setLong(3,producto.getCategoria().getIdCategoria());

            if (producto.getId() != null && producto.getId() > 0) {
                stmt.setLong(4, producto.getId());
            } else {
                stmt.setDate(4, new Date(producto.getFechaRegistro().getTime()));
            }

            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void eliminar(Long id) {

       try (Connection cnn = getInstance();
            PreparedStatement stmt = cnn.prepareStatement("DELETE FROM productos WHERE idproductos=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static Producto crearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("idproductos"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getInt("precio"));
        p.setFechaRegistro(rs.getDate("fecha_registro"));
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getLong("categoria_id"));
        c.setNombreCategoria(rs.getString("categoria"));
        p.setCategoria(c);
        return p;
    }
}
