import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Order} from "../types/types";

interface OrderState {
    allOrders: Order[];
}

const initialState: OrderState = {
    allOrders: [],
};

const orderSlice = createSlice({
    name: 'order',
    initialState,
    reducers: {
        setAllOrders: (state, action: PayloadAction<Order[]>) => {
            state.allOrders = action.payload;
        },
        updateOrders: (state, action: PayloadAction<Order[]>) => {
            const addOrders: Order[] = state.allOrders.filter((order: Order) =>
                !action.payload.some((item: Order) => item.id === order.id));

            state.allOrders = [...addOrders, ...action.payload];

            console.log(action.payload);
            console.log(state.allOrders);
        },
    },
});

export const {
    setAllOrders,
    updateOrders
} = orderSlice.actions;

export default orderSlice.reducer;