import React from 'react';
import { Categoria } from '../types/types';
import CategorySelect from './CategorySelect';

interface FilterBarProps {
  categorias: Categoria[];
  filtroNombre: string;
  setFiltroNombre: (v: string) => void;
  filtroCategoriaId: number | undefined;
  setFiltroCategoriaId: (v: number | undefined) => void;
  filtroMinPrecio: number | undefined;
  setFiltroMinPrecio: (v: number | undefined) => void;
  filtroMaxPrecio: number | undefined;
  setFiltroMaxPrecio: (v: number | undefined) => void;
  onFiltrar: () => void;
  onReset: () => void;
}

const FilterBar: React.FC<FilterBarProps> = ({
  categorias,
  filtroNombre,
  setFiltroNombre,
  filtroCategoriaId,
  setFiltroCategoriaId,
  filtroMinPrecio,
  setFiltroMinPrecio,
  filtroMaxPrecio,
  setFiltroMaxPrecio,
  onFiltrar,
  onReset,
}) => {
  return (
    <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
      <input
        type="text"
        placeholder="Buscar por nombre"
        value={filtroNombre}
        onChange={(e) => setFiltroNombre(e.target.value)}
      />

      <CategorySelect
        categorias={categorias}
        selectedId={filtroCategoriaId}
        onChange={setFiltroCategoriaId}
      />

      <input
        type="number"
        placeholder="Precio min"
        step="0.01"
        value={filtroMinPrecio !== undefined ? filtroMinPrecio : ''}
        onChange={(e) =>
          setFiltroMinPrecio(e.target.value ? parseFloat(e.target.value) : undefined)
        }
      />

      <input
        type="number"
        placeholder="Precio max"
        step="0.01"
        value={filtroMaxPrecio !== undefined ? filtroMaxPrecio : ''}
        onChange={(e) =>
          setFiltroMaxPrecio(e.target.value ? parseFloat(e.target.value) : undefined)
        }
      />

      <button onClick={onFiltrar}>Filtrar</button>
      <button onClick={onReset}>Reiniciar</button>
    </div>
  );
};

export default FilterBar;
