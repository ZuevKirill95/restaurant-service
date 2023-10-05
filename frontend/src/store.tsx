import {configureStore} from "@reduxjs/toolkit";
import userReducer from "./slices/userSlice";
import authReducer from "./slices/authSlice";

export default configureStore({
    reducer: {
        users: userReducer,
        auth: authReducer
    },
});