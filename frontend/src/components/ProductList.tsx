import React, { useState } from 'react';
import { Categoria, Producto } from '../types/types';
import ProductForm from './ProductForm';

interface ProductListProps {
  productos: Producto[];
  categorias: Categoria[];
  onActualizar: (
    id: number,
    nombre: string,
    precio: number,
    stock: number,
    categoriaId: number
  ) => void;
  onEliminar: (id: number) => void;
}

const ProductList: React.FC<ProductListProps> = ({
  productos,
  categorias,
  onActualizar,
  onEliminar,
}) => {
  const [editandoId, setEditandoId] = useState<number | null>(null);

  const productoParaEditar =
    editandoId !== null ? productos.find((p) => p.id === editandoId) : undefined;

  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>ID</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Nombre</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Precio</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Stock</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Categoría</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {productos.map((prod) => (
          <tr key={prod.id}>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>{prod.id}</td>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>{prod.nombre}</td>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>{prod.precio.toFixed(2)}</td>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>{prod.stock}</td>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>{prod.categoria.nombre}</td>
            <td style={{ border: '1px solid #ddd', padding: '8px' }}>
              <button onClick={() => setEditandoId(prod.id)}>Editar</button>
              <button onClick={() => onEliminar(prod.id)} style={{ marginLeft: '8px' }}>
                Eliminar
              </button>
            </td>
          </tr>
        ))}

        {productoParaEditar && (
          <tr>
            <td colSpan={6} style={{ border: '1px solid #ddd', padding: '8px' }}>
              <h3>Editar Producto ID {productoParaEditar.id}</h3>
              <ProductForm
                categorias={categorias}
                isCategoriaForm={false}
                productoEdit={productoParaEditar}
                onUpdateProducto={(prodActualizado) => {
                  onActualizar(
                    prodActualizado.id,
                    prodActualizado.nombre,
                    prodActualizado.precio,
                    prodActualizado.stock,
                    prodActualizado.categoria.id
                  );
                  setEditandoId(null);
                }}
              />
              <button onClick={() => setEditandoId(null)}>Cancelar</button>
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
};

export default ProductList;
