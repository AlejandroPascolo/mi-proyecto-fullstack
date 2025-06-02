import React, { useEffect, useState } from 'react';
import './App.css';
import { Categoria, Producto } from './types/types';
import {
  fetchCategorias,
  fetchProductos,
  fetchProductosConFiltros,
  crearCategoria,
  crearProducto,
  actualizarProducto,
  eliminarProducto,
} from './api/api';

import FilterBar from './components/FilterBar';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';

const App: React.FC = () => {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [productos, setProductos] = useState<Producto[]>([]);

  const [filtroNombre, setFiltroNombre] = useState<string>('');
  const [filtroCategoriaId, setFiltroCategoriaId] = useState<number | undefined>(undefined);
  const [filtroMinPrecio, setFiltroMinPrecio] = useState<number | undefined>(undefined);
  const [filtroMaxPrecio, setFiltroMaxPrecio] = useState<number | undefined>(undefined);

  useEffect(() => {
    cargarCategorias();
    cargarProductos();
  }, []);

  const cargarCategorias = async () => {
    try {
      const data = await fetchCategorias();
      setCategorias(data);
    } catch (err) {
      console.error(err);
      alert('Error al cargar categorías');
    }
  };

  const cargarProductos = async () => {
    try {
      const data = await fetchProductos();
      setProductos(data);
    } catch (err) {
      console.error(err);
      alert('Error al cargar productos');
    }
  };

  const manejarFiltrado = async () => {
    try {
      const data = await fetchProductosConFiltros(
        filtroNombre,
        filtroCategoriaId,
        filtroMinPrecio,
        filtroMaxPrecio
      );
      setProductos(data);
    } catch (err) {
      console.error(err);
      alert('Error al filtrar productos');
    }
  };

  const resetearFiltros = () => {
    setFiltroNombre('');
    setFiltroCategoriaId(undefined);
    setFiltroMinPrecio(undefined);
    setFiltroMaxPrecio(undefined);
    cargarProductos();
  };

  const manejarCrearCategoria = async (nombre: string, descripcion: string) => {
    try {
      await crearCategoria({ nombre, descripcion });
      cargarCategorias();
      alert('Categoría creada con éxito');
    } catch (err) {
      console.error(err);
      alert('Error al crear categoría');
    }
  };

  const manejarCrearProducto = async (
    nombre: string,
    precio: number,
    stock: number,
    categoriaId: number
  ) => {
    try {
      await crearProducto({
        nombre,
        precio,
        stock,
        categoria: { id: categoriaId },
      });
      cargarProductos();
      alert('Producto creado con éxito');
    } catch (err) {
      console.error(err);
      alert('Error al crear producto');
    }
  };

  const manejarActualizarProducto = async (
    id: number,
    nombre: string,
    precio: number,
    stock: number,
    categoriaId: number
  ) => {
    try {
      await actualizarProducto(id, {
        nombre,
        precio,
        stock,
        categoria: { id: categoriaId },
      });
      cargarProductos();
      alert('Producto actualizado con éxito');
    } catch (err) {
      console.error(err);
      alert('Error al actualizar producto');
    }
  };

  const manejarEliminarProducto = async (id: number) => {
    if (!window.confirm('¿Estás seguro de eliminar este producto?')) {
      return;
    }
    try {
      await eliminarProducto(id);
      cargarProductos();
      alert('Producto eliminado');
    } catch (err) {
      console.error(err);
      alert('Error al eliminar producto');
    }
  };

  return (
    <div style={{ margin: '20px' }}>
      <h1>Gestión de Productos y Categorías</h1>

      <section style={{ marginBottom: '40px' }}>
        <h2>Crear Categoría</h2>
        <ProductForm
          categorias={[]}
          onSubmitCategoria={manejarCrearCategoria}
          isCategoriaForm={true}
        />
      </section>

      <section style={{ marginBottom: '20px' }}>
        <h2>Filtros</h2>
        <FilterBar
          categorias={categorias}
          filtroNombre={filtroNombre}
          setFiltroNombre={setFiltroNombre}
          filtroCategoriaId={filtroCategoriaId}
          setFiltroCategoriaId={setFiltroCategoriaId}
          filtroMinPrecio={filtroMinPrecio}
          setFiltroMinPrecio={setFiltroMinPrecio}
          filtroMaxPrecio={filtroMaxPrecio}
          setFiltroMaxPrecio={setFiltroMaxPrecio}
          onFiltrar={manejarFiltrado}
          onReset={resetearFiltros}
        />
      </section>

      <section style={{ marginBottom: '40px' }}>
        <h2>Crear Producto</h2>
        <ProductForm
          categorias={categorias}
          onSubmitProducto={manejarCrearProducto}
          isCategoriaForm={false}
        />
      </section>

      <section>
        <h2>Productos</h2>
        <ProductList
          productos={productos}
          categorias={categorias}
          onActualizar={manejarActualizarProducto}
          onEliminar={manejarEliminarProducto}
        />
      </section>
    </div>
  );
};

export default App;
