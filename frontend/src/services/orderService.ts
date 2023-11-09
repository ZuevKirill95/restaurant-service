import axios from "axios";
import {Dispatch} from "redux";
import authHeader from "./auth-header";
import {setAllOrders} from "../slices/orderSlice";

interface User {
    accessToken: string;
}

const API_URL_ORDER = "orders"

const updateOrderStatusById = async (id: number, status: string, branchId: number, branchAddress: string, dispatch: Dispatch) => {
    const headers = authHeader();
    try {
        const response = await axios.put(API_URL_ORDER + `/${id}`,
            {status: status, branchId: branchId, branchAddress: branchAddress},
            {headers});

        const updateOrder = response.data;
        return updateOrder;

    } catch (error) {
        console.error("Ошибка обновления заказа:", error);
        throw error;
    }
};

const cancelOrderById = async (id: number, message: string, dispatch: Dispatch): Promise<User> => {
    const headers = authHeader();

    try {
        const response = await axios.put(API_URL_ORDER + `/${id}/cancel`,
            {message: message},
            {headers});

        const updateOrder = response.data;
        return updateOrder;

    } catch (error) {
        console.error("Ошибка отмены заказа:", error);
        throw error;
    }
};

const getListOrders = async (dispatch: Dispatch) => {
    const headers = authHeader();

    try {
        const response = await axios.get(API_URL_ORDER, { headers });

        const orders = response.data;

        dispatch(setAllOrders(orders));

        return orders;
    } catch (error) {
        console.error("Ошибка получения заказов:", error);
        throw error;
    }
};

const orderService = {
    updateOrderStatusById,
    cancelOrderById,
    getListOrders,
};

export default orderService;