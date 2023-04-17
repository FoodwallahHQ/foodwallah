export interface IngredientListColumn {
  id: 'id' | 'qt' | 'units' | 'ingredient';
  label: string;
  minWidth?: number;
  align?: 'right';
  format?: (value: number) => string;
}

export interface IngredientListData {
  id: number,
  qt: number;
  units: string,
  ingredient: string;
}

export interface RecipeStepListColumn {
  id: 'step_number' | 'images' | 'text';
  label: string;
  minWidth?: number;
  align?: 'right';
  format?: (value: number) => string;
}

export interface RecipeStepListData {
  id: number,
  step_number: number,
  images: string,
  text: string;
}

export type DynamicTableColumn = IngredientListColumn | RecipeStepListColumn;
export type DynamicTableData = IngredientListData | RecipeStepListData;
