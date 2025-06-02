import { Categoria, Producto } from '../types/types';

const API_BASE = '/api';

export async function fetchCategorias(): Promise<Categoria[]> {
  const response = await fetch(`${API_BASE}/categorias`);
  if (!response.ok) {
    throw new Error('Error al obtener categorías');
  }
  return await response.json();
}

export async function crearCategoria(categoria: Omit<Categoria, 'id'>): Promise<Categoria> {
  const response = await fetch(`${API_BASE}/categorias`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(categoria),
  });
  if (!response.ok) {
    throw new Error('Error al crear categoría');
  }
  return await response.json();
}

export async function fetchProductos(): Promise<Producto[]> {
  const response = await fetch(`${API_BASE}/productos`);
  if (!response.ok) {
    throw new Error('Error al obtener productos');
  }
  return await response.json();
}

export async function fetchProductosConFiltros(
  nombre?: string,
  categoriaId?: number,
  minPrecio?: number,
  maxPrecio?: number
): Promise<Producto[]> {
  const params = new URLSearchParams();
  if (nombre && nombre.trim() !== '') {
    params.append('nombre', nombre.trim());
  }
  if (categoriaId != null) {
    params.append('categoriaId', categoriaId.toString());
  }
  if (minPrecio != null && maxPrecio != null) {
    params.append('minPrecio', minPrecio.toString());
    params.append('maxPrecio', maxPrecio.toString());
  }
  const queryString = params.toString();
  const url = queryString ? `${API_BASE}/productos?${queryString}` : `${API_BASE}/productos`;

  const response = await fetch(url);
  if (!response.ok) {
    throw new Error('Error al filtrar productos');
  }
  return await response.json();
}

export async function crearProducto(producto: {
  nombre: string;
  precio: number;
  stock: number;
  categoria: { id: number };
}): Promise<Producto> {
  const response = await fetch(`${API_BASE}/productos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto),
  });
  if (!response.ok) {
    throw new Error('Error al crear producto');
  }
  return await response.json();
}

export async function actualizarProducto(
  id: number,
  producto: {
    nombre: string;
    precio: number;
    stock: number;
    categoria: { id: number };
  }
): Promise<Producto> {
  const response = await fetch(`${API_BASE}/productos/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto),
  });
  if (!response.ok) {
    throw new Error('Error al actualizar producto');
  }
  return await response.json();
}

export async function eliminarProducto(id: number): Promise<void> {
  const response = await fetch(`${API_BASE}/productos/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Error al eliminar producto');
  }
  return;
}
