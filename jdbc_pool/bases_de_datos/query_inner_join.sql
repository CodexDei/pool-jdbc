SELECT * FROM java_curso.productos as p
inner join java_curso.categorias as c on (p.categoria_id = c.idcategorias);