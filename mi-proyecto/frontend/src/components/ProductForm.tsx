import React, { useState, useEffect } from "react";
import { Categoria, Producto } from "../types/types";
import CategorySelect from "./CategorySelect";

interface ProductFormProps {
  categorias: Categoria[];
  isCategoriaForm: boolean;
  onSubmitCategoria?: (nombre: string, descripcion: string) => void;
  onSubmitProducto?: (nombre: string, precio: number, stock: number, categoriaId: number) => void;
  onUpdateProducto?: (producto: Producto) => void;
  productoEdit?: Producto;
}

const ProductForm: React.FC<ProductFormProps> = ({
  categorias,
  isCategoriaForm,
  onSubmitCategoria,
  onSubmitProducto,
  onUpdateProducto,
  productoEdit,
}) => {
  const [catNombre, setCatNombre] = useState<string>("");
  const [catDescripcion, setCatDescripcion] = useState<string>("");

  const [prodNombre, setProdNombre] = useState<string>("");
  const [prodPrecio, setProdPrecio] = useState<number>(0);
  const [prodStock, setProdStock] = useState<number>(0);
  const [prodCategoriaId, setProdCategoriaId] = useState<number | undefined>(
    undefined
  );

  useEffect(() => {
    if (productoEdit) {
      setProdNombre(productoEdit.nombre);
      setProdPrecio(productoEdit.precio);
      setProdStock(productoEdit.stock);
      setProdCategoriaId(productoEdit.categoria.id);
    }
  }, [productoEdit]);

  const handleSubmitCategoria = (e: React.FormEvent) => {
    e.preventDefault();
    if (catNombre.trim() === "") {
      alert("El nombre de la categoría no puede estar vacío");
      return;
    }
    onSubmitCategoria && onSubmitCategoria(catNombre.trim(), catDescripcion.trim());
    setCatNombre("");
    setCatDescripcion("");
  };

  const handleSubmitProducto = (e: React.FormEvent) => {
    e.preventDefault();
    if (!prodCategoriaId) {
      alert("Selecciona una categoría para el producto");
      return;
    }
    if (prodNombre.trim() === "" || prodPrecio <= 0 || prodStock < 0) {
      alert("Verifica los datos del producto");
      return;
    }
    if (productoEdit && onUpdateProducto) {
      const updated: Producto = {
        ...productoEdit,
        nombre: prodNombre,
        precio: prodPrecio,
        stock: prodStock,
        categoria: { id: prodCategoriaId, nombre: "", descripcion: "" },
      };
      onUpdateProducto(updated);
    } else {
      onSubmitProducto &&
        onSubmitProducto(prodNombre.trim(), prodPrecio, prodStock, prodCategoriaId);
      setProdNombre("");
      setProdPrecio(0);
      setProdStock(0);
      setProdCategoriaId(undefined);
    }
  };

  return (
    <form onSubmit={isCategoriaForm ? handleSubmitCategoria : handleSubmitProducto}>
      {isCategoriaForm ? (
        <>
          <label>Nombre de Categoría:</label>
          <input
            type="text"
            value={catNombre}
            onChange={(e) => setCatNombre(e.target.value)}
            required
          />
          <label>Descripción (opcional):</label>
          <input
            type="text"
            value={catDescripcion}
            onChange={(e) => setCatDescripcion(e.target.value)}
          />
          <button type="submit">Guardar Categoría</button>
        </>
      ) : (
        <>
          <label>Nombre de Producto:</label>
          <input
            type="text"
            value={prodNombre}
            onChange={(e) => setProdNombre(e.target.value)}
            required
          />

          <label>Precio:</label>
          <input
            type="number"
            step="0.01"
            value={prodPrecio}
            onChange={(e) => setProdPrecio(parseFloat(e.target.value))}
            required
            min="0.01"
          />

          <label>Stock:</label>
          <input
            type="number"
            value={prodStock}
            onChange={(e) => setProdStock(parseInt(e.target.value))}
            required
            min="0"
          />

          <label>Categoría:</label>
          <CategorySelect
            categorias={categorias}
            selectedId={prodCategoriaId}
            onChange={setProdCategoriaId}
          />

          <button type="submit">
            {productoEdit ? "Actualizar Producto" : "Guardar Producto"}
          </button>
        </>
      )}
    </form>
  );
};

export default ProductForm;
