import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Dish} from "../types/types";

interface DishState {
    allBranchDishes: Dish[];
    allDishes: Dish[];
}

const initialState: DishState = {
    allBranchDishes: [],
    allDishes: [],
};

const dishSlice = createSlice({
    name: 'dish',
    initialState,
    reducers: {
        setAllBranchDishes: (state, action: PayloadAction<Dish[]>) => {
            state.allBranchDishes = action.payload;
        }, setAllDish: (state, action: PayloadAction<Dish[]>) => {
            state.allDishes = action.payload;
        },
    },
});

export const {
    setAllBranchDishes,
    setAllDish
} = dishSlice.actions;

export default dishSlice.reducer;