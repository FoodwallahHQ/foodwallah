import React, {FC, useEffect, useState} from 'react';
import DynamicTable from "@/@core/document/editor/DynamicTable";
import {DynamicTableData, IngredientListColumn} from "@/@core/utils/types";
import {Ingredient} from "@/api";

export interface IngredientListProps {
  ingredients: Ingredient[],
  onIngredientChange: (DynamicTableData) => void
}

const columns: readonly IngredientListColumn[] = [
  { id: 'qt', label: 'Amount', minWidth: 10 },
  { id: 'units', label: 'Units', minWidth: 40 },
  { id: 'description', label: 'Recipe Description', minWidth: 600 }
];

const IngredientList: FC<IngredientListProps> = (props) => {
  const [data, setData] = useState<DynamicTableData[]>([]);
  const onAddItem = () => {
    const updatedRows = [
      ...data
    ];
    updatedRows.push({
      id: data.length,
      qt: 0,
      units: '',
      description: ''
    })
    setData(updatedRows);
    props.onIngredientChange(updatedRows)
  };

  const onInputChange = (index: number, field: string, event: any): void => {
    event.preventDefault()
    data[index][field] = event.target.value
    setData([...data])
    props.onIngredientChange(data)
  }

  const onRemoveItem = () => {
    const updatedRows = [...data];
    updatedRows.pop();
    setData(updatedRows);
    props.onIngredientChange(updatedRows)
  };

  const convertToData = (item: Ingredient, index: number): DynamicTableData => {
    return {
      id: index,
      qt: item.amount,
      units: item.units,
      description: item.description
    }
  }

  useEffect(() => {
    if (props.ingredients) {
      const newData: DynamicTableData[] = props.ingredients.map((it, index) => convertToData(it, index))
      setData(newData)
    }

  }, [props.ingredients])

  return <DynamicTable
      columns={columns}
      data={data}
      onAddItem={onAddItem}
      onRemoveItem={onRemoveItem}
      onInputChange={onInputChange}
  />
}
export default IngredientList;
