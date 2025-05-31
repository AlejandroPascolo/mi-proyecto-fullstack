import React from "react";
import { Categoria } from "../types/types";

interface CategorySelectProps {
  categorias: Categoria[];
  selectedId: number | undefined;
  onChange: (categoriaId: number | undefined) => void;
}

const CategorySelect: React.FC<CategorySelectProps> = ({
  categorias,
  selectedId,
  onChange,
}) => {
  return (
    <select
      value={selectedId ?? ""}
      onChange={(e) => {
        const value = e.target.value;
        onChange(value ? parseInt(value) : undefined);
      }}
    >
      <option value="">-- Seleccione Categoría --</option>
      {categorias.map((cat) => (
        <option key={cat.id} value={cat.id}>
          {cat.nombre}
        </option>
      ))}
    </select>
  );
};

export default CategorySelect;
