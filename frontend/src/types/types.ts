export interface Categoria {
  id: number;
  nombre: string;
  descripcion?: string;
}

export interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  categoria: Categoria;
}
