import React, {FC, useEffect, useState} from 'react';
import DynamicTable from "@/@core/document/editor/DynamicTable";
import {DynamicTableData, RecipeStepListColumn} from "@/@core/utils/types";
import {RecipeStep} from "@/api";

export interface RecipeStepListProps {
  recipe: RecipeStep[],
  onRecipeStepChange: (DynamicTableData) => void
}

const columns: readonly RecipeStepListColumn[] = [
  { id: 'step_number', label: 'Step Number', minWidth: 10 },
  { id: 'images', label: 'Image URLs', minWidth: 300 },
  { id: 'text', label: 'Description', minWidth: 400 },
];

const RecipeStepList: FC<RecipeStepListProps> = (props) => {
  const [data, setData] = useState<DynamicTableData[]>([]);

  const onAddItem = () => {
    const updatedRows = [
      ...data
    ];
    updatedRows.push({
      id: data.length - 1,
      step_number: data.length,
      images: '',
      text: ''
    })
    setData(updatedRows);
    props.onRecipeStepChange(updatedRows)
  };

  const onInputChange = (index: number, field: string, event: any): void => {
    event.preventDefault()
    data[index][field] = event.target.value
    setData([...data])
    props.onRecipeStepChange(data)
  }

  const onRemoveItem = () => {
    const updatedRows = [...data];
    updatedRows.pop();
    setData(updatedRows);
    props.onRecipeStepChange(updatedRows)
  };

  const convertToData = (item: RecipeStep, index: number): DynamicTableData => {
    return {
      id: index,
      step_number: index + 1,
      images: item.images?.join("\n"),
      text: item.text
    }
  }

  useEffect(() => {
    if (props.recipe) {
      const newData: DynamicTableData[] = props.recipe.map((it, index) => convertToData(it, index))
      setData(newData)
    }

  }, [props.recipe])

  return (
    <>
      <DynamicTable
          columns={columns}
          data={data}
          onAddItem={onAddItem}
          onRemoveItem={onRemoveItem}
          onInputChange={onInputChange}
      />
      </>
  );
}

export default RecipeStepList;
