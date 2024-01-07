import {configureStore} from "@reduxjs/toolkit";
import orderReducer from "./slices/orderSlice";
import dishReducer from "./slices/dishSlice";
import categoryReducer from "./slices/categorySlice";

const store = configureStore({
    reducer: {
        orders: orderReducer,
        dishes: dishReducer,
        category: categoryReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>
export default store;