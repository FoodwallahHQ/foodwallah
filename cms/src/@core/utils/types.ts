export interface IngredientListColumn {
  id: 'id' | 'qt' | 'units' | 'description';
  label: string;
  minWidth?: number;
  align?: 'right';
  format?: (value: number) => string;
}

export interface IngredientListData {
  id: number,
  qt: number;
  units: string,
  description: string;
}

export interface RecipeStepListColumn {
  id: 'step_number' | 'image_url' | 'description' | 'is_cover';
  label: string;
  minWidth?: number;
  align?: 'right';
  format?: (value: number) => string;
}

export interface RecipeStepListData {
  id: number,
  step_number: number,
  image_url: string,
  description: string;
}

export type DynamicTableColumn = IngredientListColumn | RecipeStepListColumn;
export type DynamicTableData = IngredientListData | RecipeStepListData;
